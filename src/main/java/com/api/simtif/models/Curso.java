package com.api.simtif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cursos")
@Getter @Setter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false)
    private String nome;
    @Column(nullable=false)
    private String categoria;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "vagas_cursos",
            joinColumns = @JoinColumn(name = "curso_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vaga_id",
                    referencedColumnName = "id"))
    @JsonIgnore
    private List<Vaga> vagas;
}

