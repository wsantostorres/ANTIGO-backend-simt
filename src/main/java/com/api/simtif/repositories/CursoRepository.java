package com.api.simtif.repositories;

import com.api.simtif.models.Curso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "cursos", collectionResourceRel = "cursos")
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("SELECT c FROM Curso c WHERE c.nome = :nome")
    Curso findByNome(@Param("nome") String nome);
}
