package com.telbot.backend.botapi;

import com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryFacade;
import com.telbot.backend.cache.UserDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class TelegramFacade {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private BotStateContext botStateContext;

    @Autowired
    private CallbackQueryFacade callbackQueryFacade;

    public BotApiMethod<?> handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());

            return callbackQueryFacade.processCallbackQuery(callbackQuery);
        }

        BotApiMethod<?> replyMessage = null;
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            log.info("New message from user:{}, chatId:{}, with text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());

            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getFrom().getId();

        BotState botState;
        BotApiMethod<?> replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.START;
                break;
            case "Запись":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Задать вопрос":
                botState = BotState.HAVE_A_QUESTION;
                break;
            case "Контакты":
                botState = BotState.SHOW_CONTACTS;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getCurrentBotState(chatId);
                break;
        }

        userDataCache.setNewBotState(chatId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
