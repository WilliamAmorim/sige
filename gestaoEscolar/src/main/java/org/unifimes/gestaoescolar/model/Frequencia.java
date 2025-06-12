package org.unifimes.gestaoescolar.model;

import javafx.scene.control.CheckBox;

public class Frequencia {
    private int id;
    private int alunoId;
    private int disciplinaId;
    private boolean falta;
    private String data;
    private int bimestre;
    
    public Frequencia(int id, int alunoId, int disciplinaId, boolean falta, String data, int bimestre) {
        this.id = id;
        this.alunoId = alunoId;
        this.disciplinaId = disciplinaId;
        this.falta = falta;
        this.data = data;
        this.bimestre = bimestre;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public boolean isFalta() {
        return falta;
    }

    public void setFalta(boolean falta) {
        this.falta = falta;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
