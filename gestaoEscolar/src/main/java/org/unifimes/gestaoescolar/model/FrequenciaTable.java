package org.unifimes.gestaoescolar.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FrequenciaTable {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty matricula;
    private final SimpleBooleanProperty falta;

    public FrequenciaTable(int id,String nome, String matricula) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.matricula = new SimpleStringProperty(matricula);
        this.falta = new SimpleBooleanProperty(false);
    }

    public int getID() {
        return id.get();
    }

    public String getNome() {
        return nome.get();
    }

    public String getMatricula() {
        return matricula.get();
    }

    public boolean isFalta() {
        return falta.get();
    }

    public SimpleBooleanProperty faltaProperty() {
        return falta;
    }

    public void setFalta(boolean falta) {
        this.falta.set(falta);
    }
}

