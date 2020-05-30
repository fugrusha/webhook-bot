package com.telbot.backend.botapi.handlers;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.botapi.InputMessageHandler;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class NewApplicationHandler implements InputMessageHandler {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private ReplyMessageService messageService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.NEW_APPLICATION;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.newApplication");
        userDataCache.setNewBotState(chatId, BotState.FILLING_PROFILE);

        return replyToUser;
    }
}
