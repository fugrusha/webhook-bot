package com.telbot.backend.service;

import com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardFactoryService {

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton writeButton = new InlineKeyboardButton()
                .setText("Написать")
                .setUrl("https://t.me/fugrusha");

        InlineKeyboardButton callButton = new InlineKeyboardButton()
                .setText("Позвонить")
                .setCallbackData(CallbackQueryType.CALL_BUTTON.toString());

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        keyboardRow.add(writeButton);
        keyboardRow.add(callButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Запись"));
        row1.add(new KeyboardButton("Задать вопрос"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Контакты"));
        row2.add(new KeyboardButton("Помощь"));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}