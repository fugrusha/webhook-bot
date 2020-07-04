package com.telbot.backend.botapi.handlers;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.service.ApplicationSenderService;
import com.telbot.backend.service.KeyboardFactoryService;
import com.telbot.backend.service.ReplyMessageService;
import com.telbot.backend.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class PhoneNumberHandler {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private KeyboardFactoryService keyboardFactory;

    @Autowired
    private ApplicationSenderService applicationSenderService;

    public BotApiMethod<?> handle(Message inputMsg) {
        Contact contact = inputMsg.getContact();
        long chatId = inputMsg.getChatId();

        TelegramUser profileData = telegramUserService.getByChatId(chatId);

        profileData.setPhone(contact.getPhoneNumber());
        profileData.setName(contact.getFirstName());
        profileData.setLastName(contact.getLastName());

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.thanksForApplication");
        replyToUser.setReplyMarkup(keyboardFactory.getMainMenuKeyboard());

        userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

        telegramUserService.saveUser(profileData);
        applicationSenderService.sendToChannel(profileData);

        return replyToUser;
    }
}
