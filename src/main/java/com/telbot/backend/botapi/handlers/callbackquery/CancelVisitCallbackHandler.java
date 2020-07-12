package com.telbot.backend.botapi.handlers.callbackquery;

import com.telbot.backend.botapi.BotState;
import com.telbot.backend.cache.UserDataCache;
import com.telbot.backend.service.KeyboardFactoryService;
import com.telbot.backend.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CancelVisitCallbackHandler implements CallbackQueryHandler {

    @Autowired
    private UserDataCache userDataCache;

    @Autowired
    private VisitService visitService;

    @Autowired
    private KeyboardFactoryService keyboardFactory;

    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        return processUsersCallback(callbackQuery);
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return CallbackQueryType.CANCEL_VISIT;
    }

    private BotApiMethod<?> processUsersCallback(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();

        String visitId = buttonQuery.getData().split(" ")[1];
        visitService.cancelVisit(visitId);

        BotApiMethod<?> callbackAnswer = updateInlineButton(buttonQuery, visitId);

        userDataCache.setNewBotState(chatId, BotState.SHOW_MAIN_MENU);

        return callbackAnswer;
    }

    private EditMessageReplyMarkup updateInlineButton(CallbackQuery buttonQuery, String visitId) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup()
                .setMessageId(buttonQuery.getMessage().getMessageId())
                .setChatId(buttonQuery.getMessage().getChatId())
                .setReplyMarkup(keyboardFactory.getInlineButtonForVisit(visitId, "Отменено"));

        return replyMarkup;
    }
}
