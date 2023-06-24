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
    public ResponseEntity<Object> saveVaga(@RequestBody Vaga vaga) {
        List<Curso> cursos = cursoRepository.findAll();
        List<Curso> vagaCursos = vaga.getCursos();

        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ainda não há cursos.");
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
        return ResponseEntity.status(HttpStatus.CREATED).body(vagaRepository.save(vaga));

    }

    @PutMapping("/vagas/{id}")
    public ResponseEntity<Object> updateVaga(@PathVariable Long id, @RequestBody Vaga vagaAtualizada) {
    Optional<Vaga> optionalVaga = vagaRepository.findById(id);

    if (optionalVaga.isPresent()) {
        Vaga vaga = optionalVaga.get();
        List<Curso> cursosAntigos = vaga.getCursos();
        List<Curso> cursosParaRemover = new ArrayList<>();

        // Desvinculando cursos
        for (Curso cursoAntigo : cursosAntigos) {
            cursoAntigo.getVagas().remove(vaga);
            cursosParaRemover.add(cursoAntigo);
        }

        // Remover os cursos antigos da vaga
        vaga.getCursos().removeAll(cursosParaRemover);

        // Relacionando novos cursos
        List<Curso> cursosRelacionados = new ArrayList<>();

        List<Curso> cursos = cursoRepository.findAll();
        List<Curso> vagaCursos = vagaAtualizada.getCursos();

        for (Curso curso : cursos) {
            for (Curso vagaCurso : vagaCursos) {
                if (vagaCurso.getId() == curso.getId()) {
                    curso.getVagas().add(vaga);
                    cursosRelacionados.add(curso);
                }
            }
        }

        // Atualiza os cursos relacionados da vaga
        vaga.setCursos(cursosRelacionados);

        // Atualiza outras propriedades da vaga, se necessário
        vaga.setTitulo(vagaAtualizada.getTitulo());
        vaga.setTipo(vagaAtualizada.getTipo());
        vaga.setDispManha(vagaAtualizada.getDispManha());
        vaga.setDispTarde(vagaAtualizada.getDispTarde());
        vaga.setDispNoite(vagaAtualizada.getDispNoite());
        vaga.setDataPublicacao(vagaAtualizada.getDataPublicacao());
        vaga.setDataEncerramento(vagaAtualizada.getDataEncerramento());
        vaga.setUrlImagem(vagaAtualizada.getUrlImagem());
        vaga.setStatus(vagaAtualizada.getStatus());

        vagaRepository.save(vaga);
        return ResponseEntity.status(HttpStatus.OK).body("Vaga atualizada com sucesso.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
    }
}

    @DeleteMapping("/vagas/{id}")
    public ResponseEntity<Object> deleteVaga(@PathVariable Long id) {
        Optional<Vaga> optionalVaga = vagaRepository.findById(id);

        if (optionalVaga.isPresent()) {
            Vaga vaga = optionalVaga.get();
            List<Curso> cursosRelacionados = vaga.getCursos();

            for (Curso curso : cursosRelacionados) {
                curso.getVagas().remove(vaga);
            }

            vagaRepository.delete(vaga);
            return ResponseEntity.status(HttpStatus.OK).body("Vaga excluída com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
        }
    }


    @GetMapping(value = "buscarTitulo")
    @ResponseBody
    public ResponseEntity<List<Vaga>> buscarTitulo(@RequestParam(name = "titulo") String titulo){
        List<Vaga> vaga = vagaRepository.buscarTitulo(titulo.trim().toUpperCase());
        return new ResponseEntity<List<Vaga>>(vaga, HttpStatus.OK);
    }

    
    @GetMapping(value = "buscarStatus")
    @ResponseBody
    public ResponseEntity<List<Vaga>> buscarPorStatus(@RequestParam(name = "status") int status) {
        List<Vaga> vagas;
        if (status == 0 || status == 1) {
            vagas = vagaRepository.buscarStatus(status);
            return new ResponseEntity<>(vagas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
