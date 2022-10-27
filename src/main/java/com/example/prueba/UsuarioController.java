package com.example.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public void crear(@RequestBody Usuario usuario){
        usuarioRepository.save(usuario);
    }

    @PutMapping
    public void agregarRol(@RequestParam String email, @RequestParam String password, @RequestParam String rol){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        if(optionalUsuario.isEmpty()) {
            ResponseEntity.badRequest();
            throw new RuntimeException("Error Codigo 102: Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if(!usuario.getPassword().equals(password)) {
            ResponseEntity.badRequest();
            throw new RuntimeException("Error Codigo 104: Constraseña incorrecta");
        }

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

}
