package com.telbot.backend.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class PingGoogleJob {

    @Value("${google.url}")
    private String pingUrl;

    @Scheduled(fixedRateString = "${ping.job.period}")
    public void pingGoogle() {
        try {
            URL url = new URL(pingUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());

            connection.disconnect();
        } catch (IOException e) {
            log.error("Ping FAILED: {}", e.getMessage());
        }
    }
}
