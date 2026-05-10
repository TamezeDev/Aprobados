package org.zeki.aprobados.dto;

public record StudentStatistDto (
         int testFinished,
         int rightQuestions,
         int wrongQuestions,
         int reviewQuestions){}
