package com.telbot.backend.cache;

import com.telbot.backend.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();

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
}
