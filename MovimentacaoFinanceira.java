package model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class MovimentacaoFinanceira {

    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private StringProperty tipo = new SimpleStringProperty();
    private StringProperty descricao = new SimpleStringProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty banco = new SimpleStringProperty();
    private StringProperty formaPagamento = new SimpleStringProperty();
    private IntegerProperty solicitanteId = new SimpleIntegerProperty();
    private StringProperty comprovante = new SimpleStringProperty();
    private StringProperty notaFiscal = new SimpleStringProperty();

    // ID
    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    // Data
    public LocalDate getData() {
        return data.get();
    }
    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data.set(data);
    }

    // Tipo
    public String getTipo() {
        return tipo.get();
    }
    public StringProperty tipoProperty() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    // Descrição
    public String getDescricao() {
        return descricao.get();
    }
    public StringProperty descricaoProperty() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }
    //Empresa
    public String getEmpresa() {
        return empresa.get();
    }
    public StringProperty empresaProperty() {
        return empresa;
    }
    public void setEmpresa(String empresa) {
        this.empresa.set(empresa);
    }

    // Valor
    public double getValor() {
        return valor.get();
    }
    public DoubleProperty valorProperty() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor.set(valor);
    }

    // Banco
    public String getBanco() {
        return banco.get();
    }
    public StringProperty bancoProperty() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco.set(banco);
    }

    // Forma de pagamento
    public String getFormaPagamento() {
        return formaPagamento.get();
    }
    public StringProperty formaPagamentoProperty() {
        return formaPagamento;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento.set(formaPagamento);
    }

    // Solicitante (agora via ID)
    public int getSolicitanteId() {
        return solicitanteId.get();
    }
    public IntegerProperty solicitanteIdProperty() {
        return solicitanteId;
    }
    public void setSolicitanteId(int solicitanteId) {
        this.solicitanteId.set(solicitanteId);
    }

    // Comprovante
    public String getComprovante() {
        return comprovante.get();
    }
    public StringProperty comprovanteProperty() {
        return comprovante;
    }
    public void setComprovante(String comprovante) {
        this.comprovante.set(comprovante);
    }

    // Nota fiscal
    public String getNotaFiscal() {
        return notaFiscal.get();
    }
    public StringProperty notaFiscalProperty() {
        return notaFiscal;
    }
    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal.set(notaFiscal);
    }

}
