package com.telbot.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class ReplyMessageService {

    @Autowired
    private LocaleMessageService localeMessageService;

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
