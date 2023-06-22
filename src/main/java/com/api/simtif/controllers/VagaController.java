package com.api.simtif.controllers;

import com.api.simtif.models.Curso;
import com.api.simtif.models.Vaga;
import com.api.simtif.repositories.CursoRepository;
import com.api.simtif.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class VagaController {
    @Autowired
    VagaRepository vagaRepository;

    @Autowired
    CursoRepository cursoRepository;

    @GetMapping("/vagas/")
    public List<Vaga> getAllVagas(){
        return vagaRepository.findAll();
    }

    @GetMapping("/vagas/{id}/")
    public Optional<Vaga> getVagaById(@PathVariable long id){
        return vagaRepository.findById(id);
    }

    @PostMapping("/vagas/")
    public Vaga saveVaga(@RequestBody Vaga vaga) {
        List<Curso> cursos = cursoRepository.findAll();
        List<Curso> vagaCursos = vaga.getCursos();

        if (cursos.isEmpty()) {
            return null;
        }

        List<Curso> cursosRelacionados = new ArrayList<>();

        for (Curso curso : cursos) {
            for (Curso vagaCurso : vagaCursos) {
                if (vagaCurso.getId() == curso.getId()) {
                    curso.getVagas().add(vaga);
                    cursosRelacionados.add(curso);
                }
            }
        }

        vaga.setCursos(cursosRelacionados);
        Vaga savedVaga = vagaRepository.save(vaga);

        return savedVaga;
    }


    @PutMapping("/vagas/{id}/")
    public Vaga updateVaga(@PathVariable long id, @RequestBody Vaga vaga){
        if(vagaRepository.findById(id).isPresent()){
            vaga.setId(id);
            return vagaRepository.save(vaga);
        }

        return null;
    }

    @DeleteMapping("/vagas/{id}/")
    public String deleteVaga(@PathVariable long id){
        vagaRepository.deleteById(id);
        return "Vaga excluída com sucesso";
    }
}
