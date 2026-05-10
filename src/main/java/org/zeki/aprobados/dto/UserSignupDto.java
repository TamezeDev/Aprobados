package org.zeki.aprobados.dto;

public record UserSignupDto(
         String email,
         String password,
         String name,
         String lastName,
         String study) {}
