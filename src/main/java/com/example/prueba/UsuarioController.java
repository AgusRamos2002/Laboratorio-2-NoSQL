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

    @GetMapping("/codigosError")
    public List<String> codigosError(){
        List<String> errores = new ArrayList<>();
        errores.add("Error 101: Usuario ya existente");
        errores.add("Error 102: Usuario no encontrado");
        errores.add("Error 103: El usuario no contiene el ROL: 'x'");
        errores.add("Error 104: Constraseña incorrecta");
        return errores;
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody Usuario usuario){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getEmail());
        if(optionalUsuario.isPresent()){
            return ResponseEntity.badRequest().body("Error 101: Usuario ya existente");
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
                return ResponseEntity.badRequest().body("Error 103: El usuario no contiene el ROL: " + rol);

        usuario.getRoles().removeAll(eliminarRolDTO.getRoles());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Actualizado");
    }

    private Usuario getUsuario(String email, String password) throws RuntimeException{
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        if(optionalUsuario.isEmpty()) {
            throw new RuntimeException("Error 102: Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if(!optionalUsuario.get().getPassword().equals(password)) {
            throw new RuntimeException("Error 104: Constraseña incorrecta");
        }
        return usuario;
    }

    @DeleteMapping("/eliminarUsuarios")
    public ResponseEntity<String> eliminarUsuarios(){
        if(usuarioRepository.count() == 0) {
            return ResponseEntity.badRequest().body("La Base de Datos no tiene usuarios creados.");
        } else {
            usuarioRepository.deleteAll();
            return ResponseEntity.ok().body("Se han eliminado todos los usuarios.");
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> eliminarUsuarioId(@PathVariable String email){
        Optional<Usuario> usuario = usuarioRepository.findById(email);

        if(usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("El usuario no existe.");
        } else {
            usuarioRepository.deleteById(email);
            return ResponseEntity.ok().body("Se ha eliminado al usuario seleccionado.");
        }
    }

}
