package view.controller;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dao.BeneficiarioDAO;
import dao.DBConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Beneficiario;

public class BeneficiarioController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtProfissao;

    @FXML
    private ComboBox<String> cbEmpresa;

    @FXML
    private TextField txtValorSalarial;

    @FXML
    private TextField txtLimiteVale;

    @FXML
    private ListView<Beneficiario> listViewBeneficiarios;

    private BeneficiarioDAO dao;
    private ObservableList<Beneficiario> dados = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws Exception {
        configurarCampoSomenteLetras(txtNome);
        configurarCampoMonetario(txtValorSalarial);
        configurarCampoMonetario(txtLimiteVale);
        configurarCpfMask();
        configurarTelefoneMask();

        Connection conn = DBConnection.getConnection();
        dao = new BeneficiarioDAO(conn);

        cbEmpresa.setItems(FXCollections.observableArrayList(
                "Coalumaq", "Service", "Goias loc", "Contrato"
        ));

        carregarBeneficiarios();
    }

    private void configurarCampoMonetario(TextField campo) {
        campo.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            String numeros = newValue.replaceAll("[^\\d]", "");
            if (numeros.isEmpty()) {
                campo.setText("");
                return;
            }

            double valor = Double.parseDouble(numeros) / 100.0;
            campo.setText(formatarMoeda(valor));
            Platform.runLater(() -> campo.positionCaret(campo.getText().length()));
        });
    }

    private void configurarCampoSomenteLetras(TextField campo) {
        campo.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null) return;

            String textoLimpo = newText.replaceAll("[^\\p{L}\\s]", "");

            if (!newText.equals(textoLimpo)) {
                campo.setText(textoLimpo);
                campo.positionCaret(textoLimpo.length());
            }
        });
    }

    private void configurarCpfMask() {
        txtCpf.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null) return;

            String digits = newText.replaceAll("\\D", "");

            if (digits.length() > 11) {
                digits = digits.substring(0, 11);
            }

            StringBuilder formatted = new StringBuilder();

            for (int i = 0; i < digits.length(); i++) {
                formatted.append(digits.charAt(i));
                if (i == 2 || i == 5) {
                    formatted.append('.');
                } else if (i == 8) {
                    formatted.append('-');
                }
            }

            String formattedText = formatted.toString();
            if (!formattedText.equals(newText)) {
                txtCpf.setText(formattedText);
                txtCpf.positionCaret(formattedText.length());
            }
        });
    }

    private void configurarTelefoneMask() {
        txtTelefone.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null) return;

            String digits = newText.replaceAll("\\D", "");

            if (digits.length() > 11) {
                digits = digits.substring(0, 11);
            }

            StringBuilder formatted = new StringBuilder();

            for (int i = 0; i < digits.length(); i++) {
                if (i == 0) formatted.append("(");
                formatted.append(digits.charAt(i));
                if (i == 1) formatted.append(") ");
                else if (i == 6) formatted.append("-");
            }

            String formattedText = formatted.toString();
            if (!formattedText.equals(newText)) {
                txtTelefone.setText(formattedText);
                txtTelefone.positionCaret(formattedText.length());
            }
        });
    }


    private String formatarMoeda(double valor) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(valor);
    }

    private void carregarBeneficiarios() {
        try {
            dados.clear();
            List<Beneficiario> lista = dao.listarTodos();
            dados.addAll(lista);
            listViewBeneficiarios.setItems(dados);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar beneficiários: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void cadastrarBeneficiario() {
        try {
            Beneficiario b = new Beneficiario();
            b.setNome(txtNome.getText());
            b.setCpf(txtCpf.getText());
            b.setTelefone(txtTelefone.getText());
            b.setProfissao(txtProfissao.getText());
            b.setEmpresa(cbEmpresa.getValue());
            b.setValorSalarial(converterParaDouble(txtValorSalarial.getText()));
            b.setLimiteVale(converterParaDouble(txtLimiteVale.getText()));

            dao.inserir(b);
            mostrarAlerta("Sucesso", "Beneficiário cadastrado com sucesso!", AlertType.INFORMATION);
            limparCampos();
            carregarBeneficiarios();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao cadastrar beneficiário: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void atualizarBeneficiario() {
        Beneficiario b = listViewBeneficiarios.getSelectionModel().getSelectedItem();
        if (b != null) {
            try {
                b.setNome(txtNome.getText());
                b.setCpf(txtCpf.getText());
                b.setTelefone(txtTelefone.getText());
                b.setProfissao(txtProfissao.getText());
                b.setEmpresa(cbEmpresa.getValue());
                b.setValorSalarial(converterParaDouble(txtValorSalarial.getText()));
                b.setLimiteVale(converterParaDouble(txtLimiteVale.getText()));



                dao.atualizar(b);
                mostrarAlerta("Sucesso", "Beneficiário atualizado com sucesso!", AlertType.INFORMATION);
                limparCampos();
                carregarBeneficiarios();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao atualizar beneficiário: " + e.getMessage(), AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atenção", "Selecione um beneficiário na lista para atualizar.", AlertType.WARNING);
        }
    }

    @FXML
    private void excluirBeneficiario() {
        Beneficiario b = listViewBeneficiarios.getSelectionModel().getSelectedItem();
        if (b != null) {
            try {
                dao.excluir(b.getId());
                mostrarAlerta("Sucesso", "Beneficiário excluído com sucesso!", AlertType.INFORMATION);
                limparCampos();
                carregarBeneficiarios();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao excluir beneficiário: " + e.getMessage(), AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atenção", "Selecione um beneficiário na lista para excluir.", AlertType.WARNING);
        }
    }

    @FXML
    private void selecionarBeneficiario() {
        Beneficiario b = listViewBeneficiarios.getSelectionModel().getSelectedItem();
        if (b != null) {
            txtNome.setText(b.getNome());
            txtCpf.setText(b.getCpf());
            txtTelefone.setText(b.getTelefone());
            txtProfissao.setText(b.getProfissao());
            cbEmpresa.setValue(b.getEmpresa());
            txtValorSalarial.setText(formatarMoeda(b.getValorSalarial()));
            txtLimiteVale.setText(formatarMoeda(b.getLimiteVale()));
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtProfissao.clear();
        cbEmpresa.setValue(null);
        txtValorSalarial.clear();
        txtLimiteVale.clear();
        listViewBeneficiarios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private double converterParaDouble(String valor) {
        try {
            if (valor == null || valor.isBlank()) {
                return 0.0;
            }

            valor = valor.replace("R$", "")
                    .replace("\u00A0", "")
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".");

            return Double.parseDouble(valor);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Valor inválido: " + valor, Alert.AlertType.ERROR);
            return 0.0;
        }
    }


    @FXML
    private void voltarMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Menu Principal");
            stage.show();

            Stage current = (Stage) txtNome.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar ao menu: " + e.getMessage(), AlertType.ERROR);
        }
    }
}
