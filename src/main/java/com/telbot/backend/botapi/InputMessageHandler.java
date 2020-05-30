package com.telbot.backend.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {

    BotApiMethod<?> handle(Message message);

    BotState getHandlerName();
}
