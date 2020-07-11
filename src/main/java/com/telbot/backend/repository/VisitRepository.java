package com.telbot.backend.repository;

import com.telbot.backend.domain.Visit;
import com.telbot.backend.domain.VisitStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface VisitRepository extends MongoRepository<Visit, String> {

    List<Visit> getByChatId(long chatId);

    Stream<Visit> getVisitsByStatus(VisitStatus status);
}
