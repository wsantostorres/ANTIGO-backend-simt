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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false)
    private String matricula;
    @Column(nullable=false)
    private String nomeCompleto;
    @Column(nullable=false)
    private String tipoVinculo;
    @Column(nullable=false)
    private String curso;
    private String email;
    private String numTelefone;
    private String cidade;
    private String uf;
    @Column(length = 500)
    private String objetivos;
    @Column(length = 500)
    private String experiencia;
    @Column(length = 500)
    private String projetos;
    @Column(length = 500)
    private String educacao;
    @Column(length = 500)
    private String habilidades;
    @Column(length = 500)
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
