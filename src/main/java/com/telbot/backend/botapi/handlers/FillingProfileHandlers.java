package com.telbot.backend.botapi.handlers;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.service.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FillingProfileHandlers implements InputMessageHandler {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private KeyboardFactoryService keyboardFactory;

    @Autowired
    private CalendarKeyboardService calendarKeyboardService;

    @Autowired
    private ApplicationSenderService applicationSenderService;

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getCurrentBotState(message.getChatId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setNewBotState(message.getChatId(), BotState.ASK_DATE);
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

        TelegramUser profileData = telegramUserService.getByChatId(chatId);

        if (profileData == null) {
            profileData = telegramUserService.createUser(inputMsg.getChatId());
        }

        BotState botState = userDataCache.getCurrentBotState(chatId);

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.default");

        if (botState.equals(BotState.ASK_DATE)) {
            profileData.setName(inputMsg.getFrom().getFirstName());
            profileData.setLastName(inputMsg.getFrom().getLastName());

            replyToUser = messageService.getReplyMessage(chatId, "reply.askDate");
            replyToUser.setReplyMarkup(calendarKeyboardService.generateCalendarKeyboard(LocalDate.now()));
        }

        if (botState.equals(BotState.ASK_TIME)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askTimeRepeat");
            replyToUser.setReplyMarkup(keyboardFactory.getChooseTimeKeyboard());
        }

        if (botState.equals(BotState.ASK_EMAIL)) {
            if (validationService.isValidEmailAddress(usersAnswer)) {
                profileData.setEmail(usersAnswer);

                replyToUser = messageService.getReplyMessage(chatId, "reply.askPhone");
                replyToUser.setReplyMarkup(keyboardFactory.getRequestContactKeyboard());

                userDataCache.setNewBotState(chatId, BotState.ASK_PHONE);
            } else {
                replyToUser = messageService.getReplyMessage(chatId, "reply.askRepeatEmail");
                userDataCache.setNewBotState(chatId, BotState.ASK_EMAIL);
            }
        }

        if (botState.equals(BotState.ASK_PHONE)) {
            if (validationService.isValidPhoneNumber(usersAnswer)) {
                profileData.setPhone(usersAnswer);

                replyToUser = messageService.getReplyMessage(chatId, "reply.thanksForApplication");
                replyToUser.setReplyMarkup(keyboardFactory.getMainMenuKeyboard());

                userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

                applicationSenderService.informAboutNewApplication(profileData);
                visitService.createVisit(profileData.getLastDate(), profileData.getChatId());
            } else {
                replyToUser = messageService.getReplyMessage(chatId, "reply.askRepeatPhone");
                replyToUser.setReplyMarkup(keyboardFactory.getRequestContactKeyboard());

                userDataCache.setNewBotState(chatId, BotState.ASK_PHONE);
            }
        }

        telegramUserService.saveUser(profileData);

        return replyToUser;
    }
}
