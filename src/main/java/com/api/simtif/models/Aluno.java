package com.api.simtif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "alunos")
@Getter @Setter
public class Aluno {
    @Id
    private Long id;
    @Column(nullable=false)
    private String matricula;
    @Column(nullable=false)
    private String nomeCompleto;
    @Column(nullable=false)
    private String tipoVinculo;
    @Column(nullable=false)
    private String curso;
    @Column
    private String email;
    @Column
    private String numTelefone;
    @Column
    private String cidade;
    @Column
    private String uf;
    @Column(length = 1000)
    private String objetivos;
    @Column(length = 1000)
    private String experiencia;
    @Column(length = 1000)
    private String projetos;
    @Column(length = 1000)
    private String educacao;
    @Column(length = 1000)
    private String habilidades;
    @Column(length = 1000)
    private String cursosComplementares;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "alunos_vagas",
            joinColumns = @JoinColumn(name = "aluno_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vaga_id",
                    referencedColumnName = "id"))
    @JsonIgnore
    private List<Vaga> vagas;
}
