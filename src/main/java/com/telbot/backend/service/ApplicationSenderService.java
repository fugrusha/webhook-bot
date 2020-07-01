package com.telbot.backend.service;

import com.telbot.backend.domain.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSenderService {

    @Autowired
    private ReplyMessageService messageService;

    @Value("${telegram.channel.name}")
    private String channelName;

    public void sendToChannel(TelegramUser userProfile) {

        StringBuilder sb = new StringBuilder();
        sb.append("*New application!*\n\n");
        sb.append("*Name:* ").append(userProfile.getName()).append("\n");
        sb.append("*Surname:* ").append(userProfile.getLastName()).append("\n");
        sb.append("*Phone:* ").append(userProfile.getPhone()).append("\n");
        sb.append("*Email:* ").append(userProfile.getEmail()).append("\n");
        sb.append("*Date:* ").append(userProfile.getApplicationDate());

        messageService.sendMessageToChannel(channelName, sb.toString());
    }
}
