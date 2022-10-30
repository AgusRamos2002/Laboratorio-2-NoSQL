package com.example.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public void crear(@RequestBody Usuario usuario){
        usuarioRepository.save(usuario);
    }

    @GetMapping("/{email}/{password}")
    public LoginResponse autenticar(@PathVariable String email, @PathVariable String password){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        return LoginResponse.builder().successful(
                optionalUsuario.map(usuario -> usuario.getPassword().equals(password)).orElse(false)
        ).build();
    }

    @PutMapping
    public void agregarRol(@RequestParam String email, @RequestParam String password, @RequestParam String rol){
        Usuario usuario = getUsuario(email, password);

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> eliminarRolesUsuario(@RequestBody UsuarioEliminarRolDTO eliminarRolDTO){
        Usuario usuario = getUsuario(eliminarRolDTO.getEmail(), eliminarRolDTO.getPassword());

        for (String rol: eliminarRolDTO.getRoles())
            if(!usuario.getRoles().contains(rol)) {
            return ResponseEntity.badRequest().body("103: El usuario no contiene el ROL: " + rol);
        }

        usuario.getRoles().removeAll(eliminarRolDTO.getRoles());


        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Actualizado");
    }


    private Usuario getUsuario(String email, String password) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        if(optionalUsuario.isEmpty()) {
            ResponseEntity.badRequest();
            throw new RuntimeException("Error Codigo 102: Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if(!optionalUsuario.get().getPassword().equals(password)) {
            ResponseEntity.badRequest();
            throw new RuntimeException("Error Codigo 104: Constraseña incorrecta");
        }
        return usuario;
    }


}
