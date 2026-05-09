package org.zeki.aprobados.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModuleStudyDto {

    private int idModule;
    private int yearStudy;
    private boolean official;
}
