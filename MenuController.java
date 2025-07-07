package view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private void abrirMovimentacoes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/movimentacao_financeira.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("FinUnity - Movimentações Financeiras");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            // Fecha a janela atual (Menu)
            Stage atual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            atual.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir a tela de Movimentações Financeiras: " + e.getMessage());
        }
    }


    @FXML
    private void abrirComissoes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comissoes.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("FinUnity - Comissões Pagas");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();

            // Fecha a tela atual (Menu)
            Stage atual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            atual.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }


    @FXML
    private void abrirCadastroSolicitantes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cadastro_solicitante.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("FinUnity - Cadastro de Solicitantes");
            stage.setScene(new Scene(root));
            stage.show();

            // Fecha a tela atual (Menu)
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }


    @FXML
    private void abrirVales(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vales.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Vales");
            stage.show();

            // Fecha a tela atual (Menu)
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }

    @FXML
    private void abrirPagamentosSalarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/salarios_ferias.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Pagamento de Salários");
            stage.show();

            // Fecha a tela atual (Menu)
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }


    @FXML
    private void abrirCadastroBeneficiarios(ActionEvent event) {try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/beneficiario.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("FinUnity - Cadastro de Beneficiarios");
        stage.setScene(new Scene(root));
        stage.show();

        // Fecha a tela atual (Menu)
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Erro");
    }
    }

    @FXML
    private void abrirRelatorios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorios.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Relatorios");
            stage.show();

            // Fecha a tela atual (Menu)
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }

    @FXML
    private void abrirBeneficiariosStatus(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/beneficiarios_status.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Status dos Funcionarios");
            stage.show();

            // Fecha a tela atual (Menu)
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro");
        }
    }

    @FXML
    private void abrirBackup(ActionEvent event) {
        mostrarAlerta("Executar Backup em nuvem");
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Menu");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
