package com.telbot.backend.service;

import com.telbot.backend.domain.Visit;
import com.telbot.backend.domain.VisitStatus;
import com.telbot.backend.repository.VisitRepository;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private VisitRepository visitRepository;

    public Visit createVisit(LocalDate date, LocalTime time, long chatId) {
        Visit visit = new Visit();
        visit.setDate(date);
        visit.setTime(time);
        visit.setChatId(chatId);
        visit.setStatus(VisitStatus.SCHEDULED);

        visitRepository.save(visit);

        return visit;
    }

    public List<Visit> getUserVisits(long chatId) {
        return visitRepository.getByChatId(chatId);
    }

    public void cancelVisit(String id) {
        Optional<Visit> optionalVisit = visitRepository.findById(id);

        optionalVisit.ifPresent(v -> {
            v.setStatus(VisitStatus.CANCELLED);
            visitRepository.save(v);
        });
    }

    public String getLocalizedStatus(VisitStatus status) {
        switch (status) {
            case SCHEDULED:
                return messageService.getReplyText("visit.status.scheduled");
            case FINISHED:
                return messageService.getReplyText("visit.status.finished");
            case CANCELLED:
                return messageService.getReplyText("visit.status.cancelled");
            default:
                return messageService.getReplyText("visit.status.undefined");
        }
    }
}
