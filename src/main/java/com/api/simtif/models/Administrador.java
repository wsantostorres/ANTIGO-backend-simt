package com.api.simtif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
