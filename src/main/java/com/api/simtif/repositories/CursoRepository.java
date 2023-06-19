package com.api.simtif.repositories;

import com.api.simtif.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "alunos", collectionResourceRel = "alunos")
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
