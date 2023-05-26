package com.api.simtif.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String titulo;
    private String descricao;
    private int tipo;
    private int dispManha;
    private int dispTarde;
    private int dispNoite;
    private Date dataPublicacao;
    private Date dataEncerramento;
    private String urlImagem;
    private int periodoInt;
    private int periodoSub;
    private int periodoSup;
    private int status;

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

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public int getPeriodoInt() {
        return periodoInt;
    }

    public void setPeriodoInt(int periodoInt) {
        this.periodoInt = periodoInt;
    }

    public int getPeriodoSub() {
        return periodoSub;
    }

    public void setPeriodoSub(int periodoSub) {
        this.periodoSub = periodoSub;
    }

    public int getPeriodoSup() {
        return periodoSup;
    }

    public void setPeriodoSup(int periodoSup) {
        this.periodoSup = periodoSup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
