package org.zeki.aprobados.model.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    private int idAnswer;
    private String text;
    private boolean right;
    private int order;
}
