package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Comissao {

    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private StringProperty tipo = new SimpleStringProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private StringProperty descricao = new SimpleStringProperty();
    private IntegerProperty beneficiarioId = new SimpleIntegerProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty banco = new SimpleStringProperty();
    private StringProperty formaPagamento = new SimpleStringProperty();
    private IntegerProperty solicitanteId = new SimpleIntegerProperty();
    private StringProperty comprovante = new SimpleStringProperty();
    private StringProperty relatorioComissao = new SimpleStringProperty();

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

    // Descrição
    public String getDescricao() {
        return descricao.get();
    }
    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }
    public StringProperty descricaoProperty() {
        return descricao;
    }
    // Beneficiario
    public int getBeneficiarioId() {
        return beneficiarioId.get();
    }
    public void setBeneficiarioId(int beneficiarioId) {
        this.beneficiarioId.set(beneficiarioId);
    }
    public IntegerProperty beneficiarioIdProperty() {
        return beneficiarioId;
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

    // Forma de pagamento
    public String getFormaPagamento() {
        return formaPagamento.get();
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento.set(formaPagamento);
    }
    public StringProperty formaPagamentoProperty() {
        return formaPagamento;
    }

    // Solicitante ID
    public int getSolicitanteId() {
        return solicitanteId.get();
    }
    public void setSolicitanteId(int solicitanteId) {
        this.solicitanteId.set(solicitanteId);
    }
    public IntegerProperty solicitanteIdProperty() {
        return solicitanteId;
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

    // Relatório de Comissão
    public String getRelatorioComissao() {
        return relatorioComissao.get();
    }
    public void setRelatorioComissao(String relatorioComissao) {
        this.relatorioComissao.set(relatorioComissao);
    }
    public StringProperty relatorioProperty() {
        return relatorioComissao;
    }


}
