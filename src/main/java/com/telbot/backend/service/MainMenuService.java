package com.telbot.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

// TODO
@Service
public class MainMenuService {

    @Autowired
    private KeyboardFactoryService keyboardFactoryService;

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        sendMessage.setReplyMarkup(keyboardFactoryService.getMainMenuKeyboard());

        return sendMessage;
    }

}
