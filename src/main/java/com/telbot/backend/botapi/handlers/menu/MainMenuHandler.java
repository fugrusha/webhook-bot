package com.telbot.backend.botapi.handlers.menu;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.botapi.handlers.InputMessageHandler;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }

    private SendMessage processUserInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        return messageService.getReplyMessage(chatId, "reply.mainMenu");
    }
}
