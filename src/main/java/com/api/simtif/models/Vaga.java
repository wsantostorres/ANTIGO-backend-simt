package com.api.simtif.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vagas")
@Validated
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @Column(length = 500)
    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "O tipo da vaga é obrigatório.")
    private int tipo;

    @Column
    private int dispManha;

    @Column
    private int dispTarde;

    @Column
    private int dispNoite;

    @Column(nullable = false)
    private LocalDateTime dataUltimaModificacao;

    @Column(nullable = false)
    @NotNull(message = "A data de encerramento é obrigatória.")
    private LocalDate dataEncerramento;

    @NotEmpty(message = "Você precisa selecionar ao menos um curso.")
    @ManyToMany(mappedBy = "vagas")
    private List<Curso> cursos;

    @ManyToMany(mappedBy = "vagas")
    private List<Aluno> alunos;
    
}
