package com.telbot.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "telegram_user")
public class TelegramUser {

    @Id
    private String id;

    @Indexed(unique = true)
    private long chatId;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate lastDate;

    private DateTime lastTime;
}
