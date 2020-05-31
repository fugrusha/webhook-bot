package com.telbot.backend.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TelegramUser {

    private long chatId;

    private String name;

    private String lastName;

    private String email;

    private String phone;
}
