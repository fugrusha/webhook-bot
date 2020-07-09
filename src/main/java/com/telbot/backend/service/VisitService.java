package com.telbot.backend.service;

import com.telbot.backend.domain.Visit;
import com.telbot.backend.domain.VisitStatus;
import com.telbot.backend.repository.VisitRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    @Autowired
    private ReplyMessageService messageService;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ApplicationSenderService applicationSenderService;

    public Visit createVisit(DateTime date, long chatId) {
        Visit visit = new Visit();
        visit.setDate(date);
        visit.setChatId(chatId);
        visit.setStatus(VisitStatus.SCHEDULED);

        visitRepository.save(visit);

        return visit;
    }

    public List<Visit> getUserVisits(long chatId) {
        return visitRepository.getByChatId(chatId);
    }

    public void cancelVisit(String id) {
        Visit visit = visitRepository.findById(id).get();

        visit.setStatus(VisitStatus.CANCELLED);
        visitRepository.save(visit);

        applicationSenderService.informAboutCancelling(visit);
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
