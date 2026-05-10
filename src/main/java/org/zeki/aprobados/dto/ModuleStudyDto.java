package org.zeki.aprobados.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record ModuleStudyDto(
        int idModule,
        int yearStudy,
        boolean official) {}
