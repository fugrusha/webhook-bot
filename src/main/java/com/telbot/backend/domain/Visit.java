package com.telbot.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "visit")
public class Visit {

    @Id
    private String id;

    private DateTime date;

    private VisitStatus status;

    @Indexed
    private long chatId;
}
