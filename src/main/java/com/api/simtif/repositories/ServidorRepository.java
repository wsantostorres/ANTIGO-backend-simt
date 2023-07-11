package com.api.simtif.repositories;

import com.api.simtif.models.Servidor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "servidores", collectionResourceRel = "servidores")
public interface ServidorRepository extends JpaRepository<Servidor, Long> {
    Servidor findByMatricula(String matricula);
    Optional<Servidor> findById(Long id);
}
