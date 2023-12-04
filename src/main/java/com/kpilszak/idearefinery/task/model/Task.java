package com.kpilszak.idearefinery.task.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
public class Task {

    @Builder.Default
    private final UUID id = UUID.randomUUID();
    private String title;
    private String description;
    @Builder.Default
    private Boolean completed = false;
    @Builder.Default
    private Date createDate = new Date();
    private Date completedDate; // TODO: Warn about property name change from completed (duplicates with Boolean)

}
