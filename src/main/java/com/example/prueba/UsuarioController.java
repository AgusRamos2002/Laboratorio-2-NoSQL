package com.example.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    public ResponseEntity<String> crear(@RequestBody Usuario usuario){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getEmail());
        if(optionalUsuario.isPresent()){
            return ResponseEntity.badRequest().body("Error Codigo 101: Usuario ya existente");
        }
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado con exito");
    }

    @GetMapping("/{email}/{password}")
    public LoginResponse autenticar(@PathVariable String email, @PathVariable String password){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        return LoginResponse.builder().successful(
                optionalUsuario.map(usuario -> usuario.getPassword().equals(password)).orElse(false)
        ).build();
    }

    @PutMapping("/agregarRoles")
    public ResponseEntity<String> agregarRol(@RequestBody UsuarioRolDTO agregarRolDTO){
        try {
            Usuario usuario = getUsuario(agregarRolDTO.getEmail(), agregarRolDTO.getPassword());
            usuario.getRoles().addAll(agregarRolDTO.getRoles());
            usuarioRepository.save(usuario);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Roles agregados con exito");
    }

    @PutMapping("/eliminarRoles")
    public ResponseEntity<String> eliminarRolesUsuario(@RequestBody UsuarioRolDTO eliminarRolDTO){
        Usuario usuario;
        try {
            usuario = getUsuario(eliminarRolDTO.getEmail(), eliminarRolDTO.getPassword());

        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        //eliminarRolDTO.getRoles().forEach(rol -> {
        //    if (!usuario.getRoles().contains(rol))
        //        ResponseEntity.badRequest().body("103: El usuario no contiene el ROL: " + rol);
        //});

       for (String rol : eliminarRolDTO.getRoles())
            if (!usuario.getRoles().contains(rol))
                return ResponseEntity.badRequest().body("103: El usuario no contiene el ROL: " + rol);

        usuario.getRoles().removeAll(eliminarRolDTO.getRoles());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Actualizado");
    }

    private Usuario getUsuario(String email, String password) throws RuntimeException{
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        if(optionalUsuario.isEmpty()) {
            throw new RuntimeException("Error Codigo 102: Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if(!optionalUsuario.get().getPassword().equals(password)) {
            throw new RuntimeException("Error Codigo 104: Constraseña incorrecta");
        }
        return usuario;
    }

}
