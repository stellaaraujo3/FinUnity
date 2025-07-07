package model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Relatorio {

    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> periodoInicial = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> periodoFinal = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> dataHoraRequisicao = new SimpleObjectProperty<>();
    private StringProperty solicitante = new SimpleStringProperty();
    private StringProperty referente = new SimpleStringProperty();
    private StringProperty beneficiario = new SimpleStringProperty();
    private StringProperty empresa = new SimpleStringProperty();
    private StringProperty arquivoRelatorio = new SimpleStringProperty();

    public Relatorio() {
    }

    public Relatorio(int id, LocalDate periodoInicial, LocalDate periodoFinal,
                     LocalDateTime dataHoraRequisicao, String solicitante,
                     String referente, String beneficiario, String empresa,
                     String arquivoRelatorio) {
        this.id.set(id);
        this.periodoInicial.set(periodoInicial);
        this.periodoFinal.set(periodoFinal);
        this.dataHoraRequisicao.set(dataHoraRequisicao);
        this.solicitante.set(solicitante);
        this.referente.set(referente);
        this.beneficiario.set(beneficiario);
        this.empresa.set(empresa);
        this.arquivoRelatorio.set(arquivoRelatorio);
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

    // Periodo Inicial
    public LocalDate getPeriodoInicial() {
        return periodoInicial.get();
    }
    public void setPeriodoInicial(LocalDate periodoInicial) {
        this.periodoInicial.set(periodoInicial);
    }
    public ObjectProperty<LocalDate> periodoInicialProperty() {
        return periodoInicial;
    }

    // Periodo Final
    public LocalDate getPeriodoFinal() {
        return periodoFinal.get();
    }
    public void setPeriodoFinal(LocalDate periodoFinal) {
        this.periodoFinal.set(periodoFinal);
    }
    public ObjectProperty<LocalDate> periodoFinalProperty() {
        return periodoFinal;
    }

    // Data Hora Requisicao
    public LocalDateTime getDataHoraRequisicao() {
        return dataHoraRequisicao.get();
    }
    public void setDataHoraRequisicao(LocalDateTime dataHoraRequisicao) {
        this.dataHoraRequisicao.set(dataHoraRequisicao);
    }
    public ObjectProperty<LocalDateTime> dataHoraRequisicaoProperty() {
        return dataHoraRequisicao;
    }

    // Solicitante
    public String getSolicitante() {
        return solicitante.get();
    }
    public void setSolicitante(String solicitante) {
        this.solicitante.set(solicitante);
    }
    public StringProperty solicitanteProperty() {
        return solicitante;
    }

    // Referente
    public String getReferente() {
        return referente.get();
    }
    public void setReferente(String referente) {
        this.referente.set(referente);
    }
    public StringProperty referenteProperty() {
        return referente;
    }

    // Beneficiario
    public String getBeneficiario() {
        return beneficiario.get();
    }
    public void setBeneficiario(String beneficiario) {
        this.beneficiario.set(beneficiario);
    }
    public StringProperty beneficiarioProperty() {
        return beneficiario;
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

    // Arquivo Relatorio
    public String getArquivoRelatorio() {
        return arquivoRelatorio.get();
    }
    public void setArquivoRelatorio(String arquivoRelatorio) {
        this.arquivoRelatorio.set(arquivoRelatorio);
    }
    public StringProperty arquivoRelatorioProperty() {
        return arquivoRelatorio;
    }
}
