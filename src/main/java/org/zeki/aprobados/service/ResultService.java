package org.zeki.aprobados.service;

import lombok.Getter;

@Getter
public class ResultService {

    private String message;
    private boolean success;

    public ResultService(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
