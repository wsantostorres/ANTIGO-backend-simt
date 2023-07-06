package com.api.simtif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "vagas")
@Getter @Setter
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, length = 100)
    private String titulo;
    @Column(length = 500)
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
    private LocalDateTime dataPublicacao;
    @Column
    private LocalDateTime dataEncerramento;

    @ManyToMany(mappedBy = "vagas")
    private List<Curso> cursos;

    @ManyToMany(mappedBy = "vagas")
    private List<Aluno> alunos;
    
}
