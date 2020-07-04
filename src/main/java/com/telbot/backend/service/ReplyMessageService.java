package com.telbot.backend.service;

import com.telbot.backend.MyTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Service
public class ReplyMessageService {

    @Autowired
    private LocaleMessageService localeMessageService;

    @Autowired
    @Lazy
    private MyTelegramBot myTelegramBot;

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(localeMessageService.getMessage(replyMessage))
                .enableMarkdown(true);
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(localeMessageService.getMessage(replyMessage, args))
                .enableMarkdown(true);
    }

    public void sendPhoto(long chatId, String imageCaption, String photoId) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setCaption(localeMessageService.getMessage(imageCaption))
                .setPhoto(photoId);

        myTelegramBot.send(sendPhoto);
    }

    public BotApiMethod getWarningReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, replyMessage);
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message)
                .enableMarkdown(true);

        myTelegramBot.send(sendMessage);
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

    public String getReplyText(String replyText, Object... args) {
        return localeMessageService.getMessage(replyText, args);
    }

    //TODO
//    public SendMessage getWarningReplyMessage(long chatId, String replyMessage) {
//        return new SendMessage(chatId, getEmojiReplyText(replyMessage, Emojis.NOTIFICATION_MARK_FAILED));
//    }

//    public String getEmojiReplyText(String replyText, Emojis emoji) {
//        return localeMessageService.getMessage(replyText, emoji);
//    }
}
