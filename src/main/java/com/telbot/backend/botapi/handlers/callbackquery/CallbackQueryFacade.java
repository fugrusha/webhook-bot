package com.telbot.backend.botapi.handlers.callbackquery;

import com.telbot.backend.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Optional;

@Component
public class CallbackQueryFacade {

    private ReplyMessageService messageService;
    private List<CallbackQueryHandler> callbackQueryHandlers;

    public CallbackQueryFacade(
            ReplyMessageService messagesService,
            List<CallbackQueryHandler> callbackQueryHandlers
    ) {
        this.messageService = messagesService;
        this.callbackQueryHandlers = callbackQueryHandlers;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery usersQuery) {

        CallbackQueryType usersQueryType = CallbackQueryType
                .valueOf(usersQuery.getData().split(" ")[0]);

        Optional<CallbackQueryHandler> queryHandler = callbackQueryHandlers
                .stream()
                .filter(callbackQuery -> callbackQuery.getHandlerQueryType().equals(usersQueryType))
                .findFirst();

        BotApiMethod<?> replyToUser = queryHandler.map(handler -> handler.handleCallbackQuery(usersQuery))
                .orElse(messageService.getWarningReplyMessage(usersQuery.getMessage().getChatId(),
                        "reply.query.failed"));

        return replyToUser;
    }
}
