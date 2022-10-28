package com.example.prueba;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    String email;

    String password;
    String nombre;
    String apellido;
    Set<String> roles = new HashSet<>();
}
