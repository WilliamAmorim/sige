package org.unifimes.gestaoescolar.model;

public class Disciplina {
    private String nome;
    private int cargaHoraria;

    public Disciplina(int cargaHoraria, String nome) {
        this.cargaHoraria = cargaHoraria;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return nome; // Ou: return nome + " (" + cargaHoraria + "h)";
    }
}
