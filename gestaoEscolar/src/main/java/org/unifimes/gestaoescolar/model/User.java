package org.unifimes.gestaoescolar.model;

import java.util.List;

public class User {

    private String nome;
    private String cpf;
    private String tipo; // admin ou professor
    private String senha;
    private List<Disciplina> disciplinas;//obrigatorio para professor


    public User(String nome, String cpf, String tipo, String senha, List<Disciplina> disciplinas) {
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.senha = senha;
        this.disciplinas = disciplinas;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
