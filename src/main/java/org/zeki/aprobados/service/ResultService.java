package org.zeki.aprobados.service;

import lombok.Getter;
import org.zeki.aprobados.model.test.Topic;

import java.util.List;

@Getter
public class ResultService {

    private String message;
    private boolean success;
    private List<Topic> topics;

    public ResultService(String message, boolean success, List<Topic> topics) {
        this.message = message;
        this.success = success;
        this.topics = topics;
    }

    public ResultService(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
