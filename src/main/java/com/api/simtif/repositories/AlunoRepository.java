package com.api.simtif.repositories;

import com.api.simtif.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "aluno", collectionResourceRel = "aluno")
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Aluno findByMatricula(String matricula);
    Optional<Aluno> findById(Long id);
}
