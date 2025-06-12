package org.unifimes.gestaoescolar.model;

public class FrequenciaChart {
    int aluno_id;
    String nome_aluno;
    int bimestre;
    int total_faltas;

    public FrequenciaChart(int aluno_id, String nome_aluno, int bimestre, int total_faltas) {
        this.aluno_id = aluno_id;
        this.nome_aluno = nome_aluno;
        this.bimestre = bimestre;
        this.total_faltas = total_faltas;
    }

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    public int getTotal_faltas() {
        return total_faltas;
    }

    public void setTotal_faltas(int total_faltas) {
        this.total_faltas = total_faltas;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public String getNome_aluno() {
        return nome_aluno;
    }

    public void setNome_aluno(String nome_aluno) {
        this.nome_aluno = nome_aluno;
    }
}
