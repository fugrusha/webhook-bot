package com.telbot.backend.repository;

import com.telbot.backend.domain.Visit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends MongoRepository<Visit, String> {

    List<Visit> getByChatId(long chatId);
}
