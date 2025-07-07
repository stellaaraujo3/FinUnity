package model;

import javafx.beans.property.*;

public class Beneficiario {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty cpf = new SimpleStringProperty();
    private StringProperty telefone = new SimpleStringProperty();
    private StringProperty profissao = new SimpleStringProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private DoubleProperty valorSalarial = new SimpleDoubleProperty();
    private DoubleProperty limiteVale = new SimpleDoubleProperty();
    private DoubleProperty limiteDisponivel= new SimpleDoubleProperty();
    private StringProperty status = new SimpleStringProperty();

    // --- ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // --- Nome
    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    // --- CPF
    public String getCpf() {
        return cpf.get();
    }

    public void setCpf(String cpf) {
        this.cpf.set(cpf);
    }

    public StringProperty cpfProperty() {
        return cpf;
    }

    // --- Telefone
    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String telefone) {
        this.telefone.set(telefone);
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    // --- Profissão
    public String getProfissao() {
        return profissao.get();
    }

    public void setProfissao(String profissao) {
        this.profissao.set(profissao);
    }

    public StringProperty profissaoProperty() {
        return profissao;
    }

    // --- Empresa
    public String getEmpresa() {
        return empresa.get();
    }

    public void setEmpresa(String empresa) {
        this.empresa.set(empresa);
    }

    public StringProperty empresaProperty() {
        return empresa;
    }

    // --- Valor Salarial
    public double getValorSalarial() {
        return valorSalarial.get();
    }

    public void setValorSalarial(double valorSalarial) {
        this.valorSalarial.set(valorSalarial);
    }

    public DoubleProperty valorSalarialProperty() {
        return valorSalarial;
    }

    // --- Limite Vale
    public double getLimiteVale() {
        return limiteVale.get();
    }

    public void setLimiteVale(double limiteVale) {
        this.limiteVale.set(limiteVale);
    }

    public DoubleProperty limiteValeProperty() {
        return limiteVale;
    }
    //status

   public String getStatus() {
        return status.get();
   }
   public void setStatus(String status) {
        this.status.set(status);
   }
   public StringProperty statusProperty() {
        return status;
   }

    // --- Limite Disponível
    public double getLimiteDisponivel() {
        return limiteDisponivel.get();
    }

    public void setLimiteDisponivel(double limiteDisponivel) {
        this.limiteDisponivel.set(limiteDisponivel);
    }



    @Override
    public String toString() {
        return getNome();
    }

}
