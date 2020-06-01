package com.telbot.backend.service;

import com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryType;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.telbot.backend.botapi.handlers.callbackquery.CallbackQueryType.CALENDAR_CALLBACK;

@Service
public class CalendarKeyboardService {

    public final String IGNORE = CallbackQueryType.IGNORE_CALLBACK.toString();
    public final String NEXT_MONTH = CallbackQueryType.NEXT_MONTH.toString();
    public final String PREVIOUS_MONTH = CallbackQueryType.PREVIOUS_MONTH.toString();

    public static final String[] WD = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

    public InlineKeyboardMarkup generateCalendarKeyboard(LocalDate date) {

        if (date == null) {
            return null;
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();

        // first row - Month and Year
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(createButton(IGNORE, new SimpleDateFormat("MMM yyyy").format(date.toDate())));
        rowsList.add(headerRow);

        // second row - Days of the week
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();

        for (String day : WD) {
            daysOfWeekRow.add(createButton(IGNORE, day));
        }

        rowsList.add(daysOfWeekRow);

        LocalDate firstDay = date.dayOfMonth().withMinimumValue();

        int shift = firstDay.dayOfWeek().get() - 1;
        int daysInMonth = firstDay.dayOfMonth().getMaximumValue();
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;

        for (int i = 0; i < rows; i++) {
            rowsList.add(buildRow(firstDay, shift));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }

        // last row
        String nextMonth = date.plusMonths(1).toString();
        String previousMonth = date.minusMonths(1).toString();

        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        controlsRow.add(createButton(PREVIOUS_MONTH + " " + previousMonth, "<"));
        controlsRow.add(createButton(NEXT_MONTH + " " + nextMonth, ">"));
        rowsList.add(controlsRow);

        keyboard.setKeyboard(rowsList);

        return keyboard;
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        return new InlineKeyboardButton()
                .setCallbackData(CALENDAR_CALLBACK + " " + callBack)
                .setText(text);
    }

    private List<InlineKeyboardButton> buildRow(LocalDate date, int shift) {
        List<InlineKeyboardButton> row = new ArrayList<>();

        int day = date.getDayOfMonth();
        LocalDate callbackDate = date;

        for (int j = 0; j < shift; j++) {
            row.add(createButton(IGNORE, " "));
        }

        for (int j = shift; j < 7; j++) {
            if (day <= (date.dayOfMonth().getMaximumValue())) {
                row.add(createButton(callbackDate.toString(), Integer.toString(day++)));
                callbackDate = callbackDate.plusDays(1);
            } else {
                row.add(createButton(IGNORE, " "));
            }
        }

        return row;
    }
}
