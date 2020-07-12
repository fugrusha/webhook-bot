package com.telbot.backend.service;

import com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryType;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${telegram.doctor.profile}")
    private String doctorProfile;

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton writeButton = new InlineKeyboardButton()
                .setText("Написать")
                .setUrl(doctorProfile);

        InlineKeyboardButton callButton = new InlineKeyboardButton()
                .setText("Позвонить")
                .setCallbackData(CallbackQueryType.CALL_BUTTON_CALLBACK.toString());

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
        row1.add(new KeyboardButton("Мои записи"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Контакты"));
        row2.add(new KeyboardButton("Помощь"));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getRequestContactKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();

        row1.add(new KeyboardButton("Отправить мой номер")
                .setRequestContact(true));

        keyboard.add(row1);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineButtonForVisit(String visitId, String buttonText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton cancelButton = new InlineKeyboardButton()
                .setText(buttonText)
                .setCallbackData(CallbackQueryType.CANCEL_VISIT.toString() + " " + visitId);

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        keyboardRow.add(cancelButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getChooseTimeKeyboard() {
        String time1 = "10:00";
        String time2 = "12:00";
        String time3 = "14:00";
        String time4 = "16:00";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton()
                .setText(time1)
                .setCallbackData(CallbackQueryType.SCHEDULE_TIME.toString() + " " + time1);

        InlineKeyboardButton button2 = new InlineKeyboardButton()
                .setText(time2)
                .setCallbackData(CallbackQueryType.SCHEDULE_TIME.toString() + " " + time2);

        InlineKeyboardButton button3 = new InlineKeyboardButton()
                .setText(time3)
                .setCallbackData(CallbackQueryType.SCHEDULE_TIME.toString() + " " + time3);

        InlineKeyboardButton button4 = new InlineKeyboardButton()
                .setText(time4)
                .setCallbackData(CallbackQueryType.SCHEDULE_TIME.toString() + " " + time4);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(button1);
        row1.add(button2);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(button3);
        row2.add(button4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
