package com.api.simtif.repositories;

import com.api.simtif.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
