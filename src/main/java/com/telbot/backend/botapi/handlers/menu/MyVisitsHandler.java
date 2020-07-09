package com.telbot.backend.botapi.handlers.menu;

import com.telbot.backend.MyTelegramBot;
import com.telbot.backend.botapi.BotState;
import com.telbot.backend.botapi.handlers.InputMessageHandler;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.Visit;
import com.telbot.backend.domain.VisitStatus;
import com.telbot.backend.service.KeyboardFactoryService;
import com.telbot.backend.service.ReplyMessageService;
import com.telbot.backend.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class MyVisitsHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private VisitService visitService;

    @Autowired
    private KeyboardFactoryService keyboardFactory;

    @Autowired
    @Lazy
    private MyTelegramBot myTelegramBot;

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_VISITS;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;

        List<Visit> userVisits = visitService.getUserVisits(chatId);

        if (userVisits.isEmpty()) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.NoVisits");
        } else {
            SendMessage allVisitMessage = messageService.getReplyMessage(chatId, "reply.myVisits");
            myTelegramBot.send(allVisitMessage);

            for (int i = 0; i < userVisits.size(); i++) {
                Visit visit = userVisits.get(i);

                SendMessage visitInfoMessage = messageService.getReplyMessage(chatId, "reply.visit",
                        i + 1, visit.getDate(), visit.getTime().getHourOfDay(),
                        visitService.getLocalizedStatus(visit.getStatus()));

                if (visit.getStatus().equals(VisitStatus.SCHEDULED)) {
                    visitInfoMessage.setReplyMarkup(keyboardFactory.getInlineButtonForVisit(visit.getId(), "Отменить"));
                }

                myTelegramBot.send(visitInfoMessage);
            }

            replyToUser = messageService.getReplyMessage(chatId, "reply.visitNumber", userVisits.size());
        }

        userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

        return replyToUser;
    }
}
