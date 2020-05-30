package com.telbot.backend.cache;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.domain.TelegramUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, TelegramUser> usersData = new HashMap<>();

    @Override
    public BotState getCurrentBotState(long chatId) {
        BotState botState = usersBotStates.get(chatId);

        if (botState == null) {
            botState = BotState.START;
        }

        return botState;
    }

    @Override
    public void setNewBotState(long chatId, BotState botState) {
        usersBotStates.put(chatId, botState);
    }

    @Override
    public TelegramUser getTelegramUser(long chatId) {
        TelegramUser user = usersData.get(chatId);

        if (user == null) {
            user = new TelegramUser();
        }

        return user;
    }

    @Override
    public void saveTelegramUser(long chatId, TelegramUser user) {
        usersData.put(chatId, user);
    }
}
