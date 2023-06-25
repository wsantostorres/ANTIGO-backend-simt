package com.api.simtif.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "vagas")
@Getter @Setter
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String titulo;
    @Column
    private String descricao;
    @Column
    private int tipo;
    @Column
    private int dispManha;
    @Column
    private int dispTarde;
    @Column
    private int dispNoite;
    @Column
    private LocalDate dataPublicacao;
    @Column
    private LocalDate dataEncerramento;
    @Column
    private int status;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    @JsonIgnore
    private Administrador administrador;

    @ManyToMany(mappedBy = "vagas")
    private List<Curso> cursos;

    @ManyToMany(mappedBy = "vagas")
    private List<Aluno> alunos;
    
}
