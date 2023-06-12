package com.api.simtif.controllers;

import com.api.simtif.models.Curso;
import com.api.simtif.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CursoController {
    @Autowired
    CursoRepository repository;

    @GetMapping("/cursos/")
    public List<Curso> getAllCursos(){
        return repository.findAll();
    }

    @GetMapping("/cursos/{id}/")
    public Optional<Curso> getCursoById(@PathVariable long id){
        return repository.findById(id);
    }

    @PostMapping("/cursos/")
    public Curso saveCurso(@RequestBody Curso curso){
        return repository.save(curso);
    }

    @PutMapping("/cursos/{id}/")
    public Curso updateCurso(@PathVariable long id, @RequestBody Curso curso){
        if(repository.findById(id).isPresent()){
            curso.setId(id);
            return repository.save(curso);
        }

        return null;
    }

    @DeleteMapping("/cursos/{id}/")
    public String deleteCurso(@PathVariable long id){
        repository.deleteById(id);
        return "Curso excluído com sucesso";
    }
}
