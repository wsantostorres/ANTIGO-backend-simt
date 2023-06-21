package com.api.simtif.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "administradores")
@Getter @Setter
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false)
    private String matricula;
    @Column(nullable=false)
    private String nomeCompleto;
    @Column(nullable=false)
    private String tipoVinculo;
    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vaga> vagas;
}
