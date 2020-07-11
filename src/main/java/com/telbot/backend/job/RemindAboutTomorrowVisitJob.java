package com.telbot.backend.job;

import com.telbot.backend.domain.VisitStatus;
import com.telbot.backend.repository.VisitRepository;
import com.telbot.backend.service.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RemindAboutTomorrowVisitJob {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

//    @Transactional(readOnly = true)
    @Scheduled(cron = "${remind.about.tomorrow.visit.job}")
    public void remind() {
        log.info("Job started...");

        visitRepository.getVisitsByStatus(VisitStatus.SCHEDULED)
                .forEach(v -> {
                    try {
                        visitService.informAboutTomorrowVisit(v.getId());
                    } catch (Exception e) {
                        log.error("Failed set remind message about visit {}: {}", v.getId(), e.getMessage());
                    }
                }
        );

        log.info("Job finished!");
    }
}
