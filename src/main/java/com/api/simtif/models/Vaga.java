package com.api.simtif.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "vagas")
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
    private String dataPublicacao;
    @Column
    private String dataEncerramento;
    @Column
    private String urlImagem;
    @Column
    private int status;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    @JsonIgnore
    private Administrador administrador;

    @ManyToMany(mappedBy = "vagas")
    private List<Curso> cursos;

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getDispManha() {
        return dispManha;
    }

    public void setDispManha(int dispManha) {
        this.dispManha = dispManha;
    }

    public int getDispTarde() {
        return dispTarde;
    }

    public void setDispTarde(int dispTarde) {
        this.dispTarde = dispTarde;
    }

    public int getDispNoite() {
        return dispNoite;
    }

    public void setDispNoite(int dispNoite) {
        this.dispNoite = dispNoite;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(String dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
