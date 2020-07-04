package com.telbot.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "telegram_user")
public class TelegramUser {

    @Id
    private String id;

    private long chatId;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate applicationDate;
}
