package view.controller;

import dao.DBConnection;
import dao.SolicitanteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Solicitante;

import java.sql.Connection;
import java.util.List;

public class SolicitanteController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSetor;

    @FXML
    private TextField txtTelefone;

    @FXML
    private ListView<String> listViewSolicitantes;

    private ObservableList<String> nomesSolicitantes = FXCollections.observableArrayList();
    private List<Solicitante> solicitantes;

    private SolicitanteDAO dao;

    private Solicitante selecionado = null;

    @FXML
    private void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();
        dao = new SolicitanteDAO(conn);
        carregarSolicitantes();

        listViewSolicitantes.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx >= 0 && idx < solicitantes.size()) {
                selecionado = solicitantes.get(idx);
                preencherCampos(selecionado);
            }
        });
    }

    private void carregarSolicitantes() throws Exception {
        solicitantes = dao.listarTodos();
        nomesSolicitantes.clear();
        for (Solicitante s : solicitantes) {
            nomesSolicitantes.add(s.getNome());
        }
        listViewSolicitantes.setItems(nomesSolicitantes);
    }

    private void preencherCampos(Solicitante s) {
        txtNome.setText(s.getNome());
        txtSetor.setText(s.getSetor());
        txtTelefone.setText(s.getTelefone());
    }

    @FXML
    private void cadastrarSolicitante() {
        try {
            Solicitante s = new Solicitante();
            s.setNome(txtNome.getText().trim());
            s.setSetor(txtSetor.getText().trim());
            s.setTelefone(txtTelefone.getText().trim());

            if (validar(s)) {
                dao.inserir(s);
                mostrarAlerta("Sucesso", "Solicitante cadastrado!", Alert.AlertType.INFORMATION);
                limparCampos();
                carregarSolicitantes();
            } else {
                mostrarAlerta("Atenção", "Preencha todos os campos!", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void atualizarSolicitante() {
        if (selecionado != null) {
            try {
                selecionado.setNome(txtNome.getText().trim());
                selecionado.setSetor(txtSetor.getText().trim());
                selecionado.setTelefone(txtTelefone.getText().trim());

                if (validar(selecionado)) {
                    dao.atualizar(selecionado);
                    mostrarAlerta("Sucesso", "Solicitante atualizado!", Alert.AlertType.INFORMATION);
                    limparCampos();
                    carregarSolicitantes();
                } else {
                    mostrarAlerta("Atenção", "Preencha todos os campos!", Alert.AlertType.WARNING);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atenção", "Selecione um solicitante na lista!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void excluirSolicitante() {
        if (selecionado != null) {
            try {
                dao.excluir(selecionado.getId());
                mostrarAlerta("Sucesso", "Solicitante excluído!", Alert.AlertType.INFORMATION);
                limparCampos();
                carregarSolicitantes();
                selecionado = null;
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atenção", "Selecione um solicitante na lista!", Alert.AlertType.WARNING);
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
            mostrarAlerta("Erro", "Erro ao voltar ao menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validar(Solicitante s) {
        return s.getNome() != null && !s.getNome().isEmpty()
                && s.getSetor() != null && !s.getSetor().isEmpty()
                && s.getTelefone() != null && !s.getTelefone().isEmpty();
    }

    private void limparCampos() {
        txtNome.clear();
        txtSetor.clear();
        txtTelefone.clear();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
