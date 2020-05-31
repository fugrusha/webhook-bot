package com.telbot.backend.botapi.handlers;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.service.KeyboardFactoryService;
import com.telbot.backend.service.MainMenuService;
import com.telbot.backend.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartMessageHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private KeyboardFactoryService keyboardFactoryService;

    @Autowired
    private UserDataCache userDataCache;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        messageService.sendPhoto(chatId, "reply.startMessage", "static/images/1.png");

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.mainMenu");
        replyToUser.setReplyMarkup(keyboardFactoryService.getMainMenuKeyboard());

        userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

        return replyToUser;
    }
}