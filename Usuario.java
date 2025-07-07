package model;

import javafx.beans.property.*;

public class Usuario {

    private IntegerProperty id;
    private StringProperty login;
    private StringProperty senha;
    private StringProperty nome;
    private StringProperty tipo;

    public Usuario() {
        this.id = new SimpleIntegerProperty();
        this.login = new SimpleStringProperty();
        this.senha = new SimpleStringProperty();
        this.nome = new SimpleStringProperty();
        this.tipo = new SimpleStringProperty();
    }

    // --- Properties (para TableView e bindings) ---
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty loginProperty() {
        return login;
    }

    public StringProperty senhaProperty() {
        return senha;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    // --- Getters e Setters clássicos ---
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getSenha() {
        return senha.get();
    }

    public void setSenha(String senha) {
        this.senha.set(senha);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }
}
