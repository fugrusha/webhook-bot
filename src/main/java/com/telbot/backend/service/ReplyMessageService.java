package com.telbot.backend.service;

import com.telbot.backend.MyTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;

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

    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        File image = null;

        try {
            image = ResourceUtils.getFile("classpath:" + imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setCaption(localeMessageService.getMessage(imageCaption))
                .setPhoto(image);

        myTelegramBot.send(sendPhoto);
    }

    public BotApiMethod getWarningReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, replyMessage);
    }

    //TODO
//    public SendMessage getWarningReplyMessage(long chatId, String replyMessage) {
//        return new SendMessage(chatId, getEmojiReplyText(replyMessage, Emojis.NOTIFICATION_MARK_FAILED));
//    }

//    public String getEmojiReplyText(String replyText, Emojis emoji) {
//        return localeMessageService.getMessage(replyText, emoji);
//    }
}
