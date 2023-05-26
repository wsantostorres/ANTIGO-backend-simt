package com.api.simtif.controllers;

import com.api.simtif.models.Vaga;
import com.api.simtif.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class VagaController {
    @Autowired
    VagaRepository repository;

    @GetMapping("/vagas")
    public List<Vaga> getAllCarros(){
        return repository.findAll();
    }

    @GetMapping("/vagas/{id}")
    public Optional<Vaga> getVagaById(@PathVariable long id){
        return repository.findById(id);
    }

    @PostMapping("/vagas")
    public Vaga saveVaga(@RequestBody Vaga vaga){
        return repository.save(vaga);
    }

    @PutMapping("/vagas/{id}")
    public Vaga updateVaga(@PathVariable long id, @RequestBody Vaga vaga){
        if(repository.findById(id).isPresent()){
            vaga.setId(id);
            return repository.save(vaga);
        }

        return null;
    }

    @DeleteMapping("/vagas/{id}")
    public String deleteVaga(@PathVariable long id){
        repository.deleteById(id);
        return "Vaga excluída com sucesso";
    }
}
