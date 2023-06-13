package com.api.simtif.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false)
    private String nome;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "assignments",
            joinColumns = @JoinColumn(name = "curso_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vaga_id",
                    referencedColumnName = "id"))
    private List<Vaga> vagas;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

