package org.unifimes.gestaoescolar.model;

public class Disciplina {
    private Integer id;
    private String nome;
    private int cargaHoraria;

    public Disciplina(Integer id,int cargaHoraria, String nome) {
        this.id = id;
        this.cargaHoraria = cargaHoraria;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
