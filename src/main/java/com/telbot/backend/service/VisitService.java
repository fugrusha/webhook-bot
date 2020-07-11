package com.telbot.backend.service;

import com.telbot.backend.domain.Visit;
import com.telbot.backend.domain.VisitStatus;
import com.telbot.backend.repository.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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

        visit = visitRepository.save(visit);

        log.info("New visit {} from user {} was created", visit.getId(), chatId);
        return visit;
    }

    public List<Visit> getUserVisits(long chatId) {
        return visitRepository.getByChatId(chatId);
    }

    public void cancelVisit(String id) {
        Visit visit = visitRepository.findById(id).get();

        visit.setStatus(VisitStatus.CANCELLED);
        visitRepository.save(visit);

        log.info("Visit {} from user {} was cancelled", visit.getId(), visit.getChatId());

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void informAboutTomorrowVisit(String id) {
        Visit visit = visitRepository.findById(id).get();

        DateTime visitDate = visit.getDate();
        DateTime today = DateTime.now().withTimeAtStartOfDay();
        DateTime dayAfterTomorrow = today.plusDays(2);

        if (visitDate.isAfter(today) && visitDate.isBefore(dayAfterTomorrow)) {
            String message = messageService.getReplyText("visit.inform.message", visitDate.toDate());
            messageService.sendMessage(String.valueOf(visit.getChatId()), message);

            log.info("User {} was informed about visit {}", visit.getChatId(), visit.getId());
        }
    }
}
