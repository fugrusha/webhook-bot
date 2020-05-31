package com.telbot.backend.botapi.handlers;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.service.ReplyMessageService;
import com.telbot.backend.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FillingProfileHandlers implements InputMessageHandler {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private ValidationService validationService;

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getCurrentBotState(message.getChatId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setNewBotState(message.getChatId(), BotState.ASK_EMAIL);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        long chatId = inputMsg.getChatId();

        TelegramUser profileData = userDataCache.getTelegramUser(chatId);
        BotState botState = userDataCache.getCurrentBotState(chatId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_EMAIL)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askEmail");
            userDataCache.setNewBotState(chatId, BotState.ASK_PHONE);
        }

        if (botState.equals(BotState.ASK_PHONE)) {
            if (validationService.isValidEmailAddress(usersAnswer)) {
                profileData.setEmail(usersAnswer);
                replyToUser = messageService.getReplyMessage(chatId, "reply.askPhone");
                userDataCache.setNewBotState(chatId, BotState.FINISH_APPLICATION);
            } else {
                replyToUser = messageService.getReplyMessage(chatId, "reply.askRepeatEmail");
                userDataCache.setNewBotState(chatId, BotState.ASK_PHONE);
            }

        }

        if (botState.equals(BotState.FINISH_APPLICATION)) {
            profileData.setPhone(usersAnswer);
            replyToUser = messageService.getReplyMessage(chatId, "reply.thanksForApplication");
            userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);
        }

        userDataCache.saveTelegramUser(chatId, profileData);

        return replyToUser;
    }
}
