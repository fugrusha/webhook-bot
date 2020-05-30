package com.telbot.backend.botapi.handlers.menu;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.botapi.handlers.InputMessageHandler;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.telbot.backend.domain.Constants.*;

@Component
public class ContactButtonHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private UserDataCache userDataCache;

    @Override
    public SendVenue handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_CONTACTS;
    }

    private SendVenue processUserInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendVenue venue = new SendVenue()
                .setChatId(chatId)
                .setTitle(VENUE_TITLE)
                .setAddress(ADDRESS)
                .setLatitude(LATITUDE)
                .setLongitude(LONGITUDE);

        userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

        return venue;
    }
}
