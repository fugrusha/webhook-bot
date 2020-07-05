package com.telbot.backend.service;

import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.domain.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSenderService {

    @Autowired
    private ReplyMessageService messageService;

    @Value("${telegram.channel.name}")
    private String channelName;

    public void informAboutNewApplication(TelegramUser userProfile) {

        StringBuilder sb = new StringBuilder();
        sb.append("*New application!*\n\n");
        sb.append("*User ID:* ").append(userProfile.getChatId()).append("\n");
        sb.append("*Name:* ").append(userProfile.getName()).append("\n");
        sb.append("*Surname:* ").append(userProfile.getLastName()).append("\n");
        sb.append("*Phone:* ").append(userProfile.getPhone()).append("\n");
        sb.append("*Email:* ").append(userProfile.getEmail()).append("\n");
        sb.append("*Date:* ").append(userProfile.getLastDate()).append("\n");
        sb.append("*Time:* ").append(userProfile.getLastTime());

        messageService.sendMessage(channelName, sb.toString());
    }

    public void informAboutCancelling(Visit visit) {

        StringBuilder sb = new StringBuilder();
        sb.append("*User cancelled application!*\n\n");
        sb.append("*User ID:* ").append(visit.getChatId()).append("\n");
        sb.append("*Date:* ").append(visit.getDate()).append("\n");
        sb.append("*Time:* ").append(visit.getTime()).append("\n");

        messageService.sendMessage(channelName, sb.toString());
    }
}
