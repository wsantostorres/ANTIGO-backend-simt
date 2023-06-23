package com.api.simtif.repositories;

import com.api.simtif.models.Administrador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(path = "admin", collectionResourceRel = "admin")
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Administrador findByMatricula(String matricula);
}
