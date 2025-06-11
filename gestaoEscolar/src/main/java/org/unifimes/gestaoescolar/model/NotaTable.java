package org.unifimes.gestaoescolar.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NotaTable {
    private int id;
    private String nome;
    private String matricula;
    private double media;
    private double nota01;
    private double nota02;
    private double nota03;
    private double nota04;


    public NotaTable(int id, String nome, String matricula, double media, double nota01, double nota02, double nota03, double nota04) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.media = media;
        this.nota01 = nota01;
        this.nota02 = nota02;
        this.nota03 = nota03;
        this.nota04 = nota04;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public double getNota01() {
        return nota01;
    }

    public void setNota01(double nota01) {
        this.nota01 = nota01;
    }

    public double getNota02() {
        return nota02;
    }

    public void setNota02(double nota02) {
        this.nota02 = nota02;
    }

    public double getNota03() {
        return nota03;
    }

    public void setNota03(double nota03) {
        this.nota03 = nota03;
    }

    public double getNota04() {
        return nota04;
    }

    public void setNota04(double nota04) {
        this.nota04 = nota04;
    }
}
