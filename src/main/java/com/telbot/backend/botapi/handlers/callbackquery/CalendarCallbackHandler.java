package com.telbot.backend.botapi.handlers.callbackquery;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.service.CalendarKeyboardService;
import com.telbot.backend.service.ReplyMessageService;
import com.telbot.backend.service.TelegramUserService;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryType.*;

@Component
public class CalendarCallbackHandler implements CallbackQueryHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private CalendarKeyboardService calendarKeyboardService;

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        return processUsersCallback(callbackQuery);
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return CALENDAR_CALLBACK;
    }

    private BotApiMethod<?> processUsersCallback(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        BotApiMethod<?> callbackAnswer = null;

        String usersInput = buttonQuery.getData().split(" ")[1];

        if (IGNORE_CALLBACK.toString().equals(usersInput)) {
            callbackAnswer = getAnswerCallbackQuery("Выберите дату", true, buttonQuery);
        } else if (NEXT_MONTH.toString().equals(usersInput)) {
            String dateString = buttonQuery.getData().split(" ")[2];
            LocalDate date = parseDate(dateString);

            callbackAnswer = getNewCalendarKeyboard(buttonQuery, date);
        } else if (PREVIOUS_MONTH.toString().equals(usersInput)) {
            String dateString = buttonQuery.getData().split(" ")[2];
            LocalDate date = parseDate(dateString);

            callbackAnswer = getNewCalendarKeyboard(buttonQuery, date);
        } else {
            LocalDate date = parseDate(usersInput);
            TelegramUser profileData = telegramUserService.getByChatId(chatId);
            profileData.setLastDate(date);
            telegramUserService.saveUser(profileData);

            userDataCache.setNewBotState(chatId, BotState.ASK_EMAIL);
            callbackAnswer = messageService.getReplyMessage(chatId, "reply.askEmail");
        }

        return callbackAnswer;
    }

    private LocalDate parseDate(String usersInput) {
        return LocalDate.parse(usersInput, DateTimeFormat.forPattern("YYYY-MM-dd"));
    }

    private EditMessageReplyMarkup getNewCalendarKeyboard(CallbackQuery buttonQuery, LocalDate date) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup()
                .setMessageId(buttonQuery.getMessage().getMessageId())
                .setChatId(buttonQuery.getMessage().getChatId())
                .setReplyMarkup(calendarKeyboardService.generateCalendarKeyboard(date));

        return replyMarkup;
    }

    private AnswerCallbackQuery getAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackquery.getId());
        answer.setShowAlert(alert);
        answer.setText(text);
        return answer;
    }
}
