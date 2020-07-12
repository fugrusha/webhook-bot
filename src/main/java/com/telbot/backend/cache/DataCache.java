package com.telbot.backend.cache;

import com.telbot.backend.botapi.BotState;

public interface DataCache {

    BotState getCurrentBotState(long chatId);

    void setNewBotState(long chatId, BotState botState);
}
