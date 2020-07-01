package com.telbot.backend.botapi.handlers.callbackquery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ContactCallbackHandler implements CallbackQueryHandler {

    @Value("${telegram.doctor.phone}")
    private String doctorPhone;

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();

        SendContact contact = new SendContact()
                .setChatId(chatId)
                .setPhoneNumber(doctorPhone)
                .setFirstName("Сергей")
                .setLastName("Босый");

        return contact;
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return CallbackQueryType.CALL_BUTTON_CALLBACK;
    }
}
