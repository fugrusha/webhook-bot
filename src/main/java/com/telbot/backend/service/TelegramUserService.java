package com.telbot.backend.service;

import com.telbot.backend.domain.TelegramUser;
import com.telbot.backend.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUserService {

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    public List<TelegramUser> getAllUsers() {
        return telegramUserRepository.findAll();
    }

    public TelegramUser getByChatId(long chatId) {
        return telegramUserRepository.findByChatId(chatId);
    }

    public TelegramUser saveUser(TelegramUser user) {
        return telegramUserRepository.save(user);
    }

    public TelegramUser createUser(long chatId) {
        TelegramUser user = new TelegramUser();
        user.setChatId(chatId);
        telegramUserRepository.save(user);

        return user;
    }
}
