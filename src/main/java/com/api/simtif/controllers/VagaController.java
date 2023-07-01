package com.api.simtif.controllers;

import com.api.simtif.models.Aluno;
import com.api.simtif.models.Curso;
import com.api.simtif.models.Vaga;
import com.api.simtif.repositories.AlunoRepository;
import com.api.simtif.repositories.CursoRepository;
import com.api.simtif.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Autowired
    AlunoRepository alunoRepository;

    @GetMapping("/vagas/")
    public List<Vaga> getAllVagas(@RequestParam(required = false) String cursoNome){
        // if curso Contains in getCursos.
        Sort sortByDataPublicacao = Sort.by(Sort.Direction.DESC, "dataPublicacao");
        List <Vaga> vagas = vagaRepository.findAll(sortByDataPublicacao);;
        

        if(cursoNome != null && !cursoNome.isEmpty()){
            Curso curso = cursoRepository.findByNome(cursoNome);
            List<Vaga> vagasDestinadas = new ArrayList<>();

            for(Vaga vaga : vagas){
                if(vaga.getCursos().contains(curso)){
                    vagasDestinadas.add(vaga);
                }
            }

            return vagasDestinadas;
        }


        return vagas;
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
        vaga.setDescricao(vagaAtualizada.getDescricao());
        vaga.setTipo(vagaAtualizada.getTipo());
        vaga.setDispManha(vagaAtualizada.getDispManha());
        vaga.setDispTarde(vagaAtualizada.getDispTarde());
        vaga.setDispNoite(vagaAtualizada.getDispNoite());
        vaga.setDataPublicacao(vagaAtualizada.getDataPublicacao());
        vaga.setDataEncerramento(vagaAtualizada.getDataEncerramento());
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

            List<Aluno> alunosRelacionados = vaga.getAlunos();
            for (Aluno aluno : alunosRelacionados) {
                aluno.getVagas().remove(vaga);
            }

            vagaRepository.delete(vaga);
            return ResponseEntity.status(HttpStatus.OK).body("Vaga excluída com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
        }
    }

    @PutMapping("/participar/{alunoId}/{vagaId}/")
    public ResponseEntity<Object> participarVaga(@PathVariable Long alunoId, @PathVariable Long vagaId) {
        
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        Optional<Vaga> vagaOptional = vagaRepository.findById(vagaId);

        if(alunoOptional.isPresent() && vagaOptional.isPresent()){
            Aluno aluno = alunoOptional.get();
            Vaga vaga = vagaOptional.get();
            String cursoAluno = aluno.getCurso();
            Curso curso = cursoRepository.findByNome(cursoAluno);

            if(!vaga.getCursos().contains(curso)){
                return ResponseEntity.status(HttpStatus.OK).body("Você não pode participar desta vaga.");
            }

            if(aluno.getTipoVinculo() == null || aluno.getTipoVinculo().isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("Você não tem mais vinculo com a instituição.");
            }

            if(vaga.getAlunos().contains(aluno)){
                return ResponseEntity.status(HttpStatus.OK).body("Você já está participando desta vaga!");
            }

            
            aluno.getVagas().add(vaga);
            vaga.getAlunos().add(aluno);

            alunoRepository.save(aluno);
            vagaRepository.save(vaga);

            return ResponseEntity.status(HttpStatus.OK).body("Participação realizada com sucesso!");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ocorreu um erro.");
    }


    @GetMapping(value = "buscarTitulo")
    @ResponseBody
    public ResponseEntity<List<Vaga>> buscarTitulo(@RequestParam(name = "titulo") String titulo, @RequestParam(required = false) String cursoNome){
        List<Vaga> vagas = vagaRepository.buscarTitulo(titulo.trim().toUpperCase());

        if(cursoNome != null && !cursoNome.isEmpty()){
            Curso curso = cursoRepository.findByNome(cursoNome);
            List<Vaga> vagasDestinadas = new ArrayList<>();

            for(Vaga vaga : vagas){
                if(vaga.getCursos().contains(curso)){
                    vagasDestinadas.add(vaga);
                }
            }
            
            return new ResponseEntity<List<Vaga>>(vagasDestinadas, HttpStatus.OK);
        }

        return new ResponseEntity<List<Vaga>>(vagas, HttpStatus.OK);
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
