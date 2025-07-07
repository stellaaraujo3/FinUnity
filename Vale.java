package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Vale {

    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private IntegerProperty beneficiarioId = new SimpleIntegerProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private StringProperty formaDesconto = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty banco = new SimpleStringProperty();
    private StringProperty formaPagamento = new SimpleStringProperty();
    private IntegerProperty solicitanteId = new SimpleIntegerProperty();
    private StringProperty comprovante = new SimpleStringProperty();
    private StringProperty reciboAssinado = new SimpleStringProperty();
    private StringProperty situacao = new SimpleStringProperty();
    private IntegerProperty qtdParcelas = new SimpleIntegerProperty();

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

    // Forma de Desconto
    public String getFormaDesconto() {
        return formaDesconto.get();
    }
    public void setFormaDesconto(String formaDesconto) {
        this.formaDesconto.set(formaDesconto);
    }
    public StringProperty formaDescontoProperty() {
        return formaDesconto;
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

    // Recibo Assinado
    public String getReciboAssinado() {
        return reciboAssinado.get();
    }
    public void setReciboAssinado(String reciboAssinado) {
        this.reciboAssinado.set(reciboAssinado);
    }
    public StringProperty reciboAssinadoProperty() {
        return reciboAssinado;
    }

    // Situação
    public String getSituacao() {
        return situacao.get();
    }
    public void setSituacao(String situacao) {
        this.situacao.set(situacao);
    }
    public StringProperty situacaoProperty() {
        return situacao;
    }

    // Qtd Parcelas
    public Integer getQtdParcelas() {
        return qtdParcelas.get() == 0 ? null : qtdParcelas.get();
    }
    public void setQtdParcelas(Integer qtdParcelas) {
        if (qtdParcelas == null) {
            this.qtdParcelas.set(0);
        } else {
            this.qtdParcelas.set(qtdParcelas);
        }
    }
    public StringProperty qtdParcelasProperty() {
        return new SimpleStringProperty(
                getQtdParcelas() == null ? "" : (getQtdParcelas() + "x")
        );
    }
}
