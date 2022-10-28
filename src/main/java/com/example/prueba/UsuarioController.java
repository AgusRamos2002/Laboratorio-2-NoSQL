package com.example.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/codigos")
    public List<String> codigosError(){
        List<String> errores = new ArrayList<>();
        errores.add("Error 101: Usuario ya existente");
        errores.add("Error 102: Usuario no encontrado");
        errores.add("Error 103: No se ha encontrado el rol 'rol1'");
        errores.add("Error 104: La contraseña no coincide");
        return errores;
    }

    @PostMapping
    public void crear(@RequestBody Usuario usuario){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getEmail());
        if(!optionalUsuario.isEmpty()){
            ResponseEntity.badRequest();
            throw new RuntimeException("Error Codigo 101: Usuario ya existente");
        }
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

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

}
