package com.telbot.backend.botapi.handlers.callbackquery;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.service.ReplyMessageService;
import com.telbot.backend.service.TelegramUserService;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ScheduleTimeCallbackHandler implements CallbackQueryHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private TelegramUserService telegramUserService;

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        return processUsersCallback(callbackQuery);
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return CallbackQueryType.SCHEDULE_TIME;
    }

    private BotApiMethod<?> processUsersCallback(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        String usersInput = buttonQuery.getData().split(" ")[1];

        LocalTime time = LocalTime.parse(usersInput);

        TelegramUser profileData = telegramUserService.getByChatId(chatId);

        DateTime visitDate = profileData.getLastDate().plusHours(time.getHourOfDay());
        profileData.setLastDate(visitDate);
        telegramUserService.saveUser(profileData);

        userDataCache.setNewBotState(chatId, BotState.ASK_EMAIL);
        SendMessage callbackAnswer = messageService.getReplyMessage(chatId, "reply.askEmail");

        return callbackAnswer;
    }
}
