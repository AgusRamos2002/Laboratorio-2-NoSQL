package com.example.prueba;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    @Id
    String email;
    String password;
    String nombre;
    String apellido;
    int puntaje;
    String idEquipoApostado;
    Set<String> roles = new HashSet<>();
}
