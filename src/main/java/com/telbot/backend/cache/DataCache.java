package com.telbot.backend.cache;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.domain.TelegramUser;

public interface DataCache {

    BotState getCurrentBotState(long chatId);

    void setNewBotState(long chatId, BotState botState);

    TelegramUser getTelegramUser(long chatId);

    void saveTelegramUser(long chatId, TelegramUser user);
}
