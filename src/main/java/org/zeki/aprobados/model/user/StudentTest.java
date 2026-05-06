package org.zeki.aprobados.model.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudentTest {

    private int idTest;
    private int lastErrors;
    private int lastRight;
    private double lastNote;
    private LocalDate lastDate;

    public StudentTest() {
    }

    public StudentTest(int idTest, int lastErrors, int lastRight, double lastNote, LocalDate lastDate) {
        this.idTest = idTest;
        this.lastErrors = lastErrors;
        this.lastRight = lastRight;
        this.lastNote = lastNote;
        this.lastDate = lastDate;
    }


}
