package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class SalarioFerias {

    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private IntegerProperty solicitanteId = new SimpleIntegerProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty tipo = new SimpleStringProperty();
    private StringProperty banco = new SimpleStringProperty();
    private StringProperty formaPagamento = new SimpleStringProperty();
    private StringProperty comprovante = new SimpleStringProperty();

    public SalarioFerias() {
    }

    public SalarioFerias(int id, LocalDate data, int solicitanteId,
                         String empresa, double valor, String tipo,
                         String banco, String formaPagamento, String comprovante) {
        this.id.set(id);
        this.data.set(data);
        this.solicitanteId.set(solicitanteId);
        this.empresa.set(empresa);
        this.valor.set(valor);
        this.tipo.set(tipo);
        this.banco.set(banco);
        this.formaPagamento.set(formaPagamento);
        this.comprovante.set(comprovante);
    }

    // ID
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }

    // Data
    public LocalDate getData() {
        return data.get();
    }
    public void setData(LocalDate data) {
        this.data.set(data);
    }
    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }

    // Solicitante
    public int getSolicitanteId() {
        return solicitanteId.get();
    }
    public void setSolicitanteId(int solicitanteId) {
        this.solicitanteId.set(solicitanteId);
    }
    public IntegerProperty solicitanteIdProperty() {
        return solicitanteId;
    }

    // Empresa
    public String getEmpresa() {
        return empresa.get();
    }
    public void setEmpresa(String empresa) {
        this.empresa.set(empresa);
    }
    public StringProperty empresaProperty() {
        return empresa;
    }

    // Valor
    public double getValor() {
        return valor.get();
    }
    public void setValor(double valor) {
        this.valor.set(valor);
    }
    public DoubleProperty valorProperty() {
        return valor;
    }

    // Tipo
    public String getTipo() {
        return tipo.get();
    }
    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }
    public StringProperty tipoProperty() {
        return tipo;
    }

    // Banco
    public String getBanco() {
        return banco.get();
    }
    public void setBanco(String banco) {
        this.banco.set(banco);
    }
    public StringProperty bancoProperty() {
        return banco;
    }

    // Forma de Pagamento
    public String getFormaPagamento() {
        return formaPagamento.get();
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento.set(formaPagamento);
    }
    public StringProperty formaPagamentoProperty() {
        return formaPagamento;
    }

    // Comprovante
    public String getComprovante() {
        return comprovante.get();
    }
    public void setComprovante(String comprovante) {
        this.comprovante.set(comprovante);
    }
    public StringProperty comprovanteProperty() {
        return comprovante;
    }
}
