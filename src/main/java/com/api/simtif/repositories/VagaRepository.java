package com.api.simtif.repositories;

import com.api.simtif.models.Vaga;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query(value = "select v from Vaga v where upper(trim(v.titulo)) like %?1%") // %?1% referece ao parametro titulo
    List<Vaga> buscarTitulo(String titulo);

}
