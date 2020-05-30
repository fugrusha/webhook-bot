package com.telbot.backend.botapi.handlers.callbackquery;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ContactCallbackHandler implements CallbackQueryHandler {

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();

        SendContact contact = new SendContact()
                .setChatId(chatId)
                .setPhoneNumber("+380978592859")
                .setFirstName("Andrew")
                .setLastName("Golovko");

        return contact;
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return CallbackQueryType.CALL_BUTTON;
    }
}
