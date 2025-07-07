package model;

import javafx.beans.property.*;

public class Solicitante {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty setor = new SimpleStringProperty();
    private StringProperty telefone = new SimpleStringProperty();

    // --- ID ---
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // --- Nome ---
    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    // --- Setor ---
    public String getSetor() {
        return setor.get();
    }

    public void setSetor(String setor) {
        this.setor.set(setor);
    }

    public StringProperty setorProperty() {
        return setor;
    }

    // --- Telefone ---
    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String telefone) {
        this.telefone.set(telefone);
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }
}
