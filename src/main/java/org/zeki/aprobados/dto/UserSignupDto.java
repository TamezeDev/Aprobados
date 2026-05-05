package org.zeki.aprobados.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupDto {

    private String email;
    private String password;
    private String name;
    private String lastName;
    private String study;
}
