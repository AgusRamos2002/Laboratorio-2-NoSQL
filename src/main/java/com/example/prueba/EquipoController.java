package com.example.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/equipos")
public class EquipoController {
    @Autowired
    EquipoRepository equipoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    MatchRepository matchRepository;

    @PostMapping
    public void crearEquipo(@RequestBody Equipo equipo){
        if(equipoRepository.findById(equipo.getNombre()).isPresent())
            throw new RuntimeException("El equipo ya existe");

        equipoRepository.save(equipo);
    }

    @DeleteMapping("/{email}/{password}/{idEquipo}/eliminar")
    public void eliminarEquipo(@PathVariable String email, @PathVariable String password, @PathVariable String idEquipo){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(email);
        if(optionalUsuario.isEmpty())
            throw new RuntimeException("Usuario inexistente");

        Usuario usuario = optionalUsuario.get();
        if(usuario.getPassword().equals(password)){
            Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipo);
            if(optionalEquipo.isEmpty())
                throw new RuntimeException("Equipo inexistente");

            equipoRepository.deleteById(idEquipo);
        }else{
            throw new RuntimeException("Contraseña incorrecta");
        }
    }

    @GetMapping("/matchActual")
    public List<Equipo> matchActual(){
        List<Equipo> equipos = equipoRepository.findAll();

        Equipo equipo1 = equipos.get((int) Math.floor(Math.random() * equipos.size()));
        equipos.remove(equipo1);

        Equipo equipo2 = equipos.get((int) Math.floor(Math.random() * equipos.size()));
        equipos.remove(equipo2);

        List<Equipo> matchActual = new ArrayList<>();
        matchActual.add(equipo1);
        matchActual.add(equipo2);

        matchRepository.save(Match.builder()
                .fecha(LocalDateTime.now())
                .emparejamientos(matchActual)
                .build());

        return matchActual;
    }

    @GetMapping("/{matchId}/ganador")
    public Equipo ganador(@PathVariable String matchId){
        Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if(optionalMatch.isEmpty())
            throw new RuntimeException("Match inexistente");

        Match match = optionalMatch.get();

        return match.getEmparejamientos().get((int) Math.floor(Math.random() * 1));
    }
}
