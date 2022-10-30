package com.example.prueba;

import lombok.Data;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;

import java.util.Set;

@Data
public class UsuarioEliminarRolDTO {
    String email;
    String password;
    Set<String> roles;
}
