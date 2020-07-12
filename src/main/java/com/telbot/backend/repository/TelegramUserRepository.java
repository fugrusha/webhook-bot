package com.telbot.backend.repository;

import com.telbot.backend.domain.TelegramUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends MongoRepository<TelegramUser, String> {

    TelegramUser findByChatId(long chatId);
}
