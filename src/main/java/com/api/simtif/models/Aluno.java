package com.api.simtif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private String objetivos;
    private String experiencia;
    private String projetos;
    private String educacao;
    private String habilidades;
    private String cursosComplementares;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "alunos_vagas",
            joinColumns = @JoinColumn(name = "aluno_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vaga_id",
                    referencedColumnName = "id"))
    private List<Vaga> vagas;
}
