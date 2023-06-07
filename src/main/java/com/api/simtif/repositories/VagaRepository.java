package com.api.simtif.repositories;

import com.api.simtif.models.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface VagaRepository extends JpaRepository<Vaga, Long> {

}
