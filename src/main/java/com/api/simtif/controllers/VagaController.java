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
    public ResponseEntity<Vaga> saveVaga(@RequestBody Vaga vaga) {
        List<Curso> cursos = new ArrayList<>();
        for (Curso curso : vaga.getCursos()) {
            Optional<Curso> cursoOptional = cursoRepository.findById(curso.getId());
            if (cursoOptional.isPresent()) {
                Curso existingCurso = cursoOptional.get();
                existingCurso.getVagas().add(vaga);
                cursos.add(existingCurso);
            }
        }
        vaga.setCursos(cursos);

        Vaga savedVaga = vagaRepository.save(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVaga);
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
