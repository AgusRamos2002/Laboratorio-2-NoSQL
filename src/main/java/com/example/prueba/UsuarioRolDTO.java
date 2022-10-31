package com.example.prueba;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioRolDTO {
    String email;
    String password;
    Set<String> roles;
}
