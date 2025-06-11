package org.unifimes.gestaoescolar.model;

import java.util.List;

public class Turma {
    private int id;
    private String nome;
    private String turno;
    private int ano;
    private List<Disciplina> disciplinas;

    public Turma(int id, String nome, String turno, int ano, List<Disciplina> disciplinas) {
        this.id = id;
        this.nome = nome;
        this.turno = turno;
        this.ano = ano;
        this.disciplinas = disciplinas;
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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public String toString() {
        return nome; // Ou: return nome + " (" + cargaHoraria + "h)";
    }
}
