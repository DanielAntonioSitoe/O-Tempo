package com.example.otempo.Modelo;

//
// Nesta classe temos a lista de atributos de uma cidade e seus methodos
//
public class Cidade {
    private String nome;
    private String pais;
    private int temp;
    private int max;
    private int min;
    private int humidade;
    private String velocidade;
    private float precisao;
    private String ceu;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getHumidade() {
        return humidade;
    }

    public void setHumidade(int humidade) {
        this.humidade = humidade;
    }

    public String getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(String velocidade) {
        this.velocidade = velocidade;
    }

    public float getPrecisao() {
        return precisao;
    }

    public void setPrecisao(float precisao) {
        this.precisao = precisao;
    }

    public String getCeu() {
        return ceu;
    }

    public void setCeu(String ceu) {
        this.ceu = ceu;
    }
}
