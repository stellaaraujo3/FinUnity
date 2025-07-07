package view.controller;

import dao.UsuarioDAO;
import model.Usuario;
import dao.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtSenha;

    private UsuarioDAO usuarioDAO;

    @FXML
    private void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();
        usuarioDAO = new UsuarioDAO(conn);
    }

    @FXML
    private void handleLogin() {
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        try {
            Usuario usuario = usuarioDAO.autenticar(login, senha);

            if (usuario != null) {
                mostrarAlerta("Login bem-sucedido!",
                        "Bem-vindo, " + usuario.getNome(), Alert.AlertType.INFORMATION);

                abrirTelaMenu();
                fecharTelaAtual();
            } else {
                mostrarAlerta("Erro de Login",
                        "Usuário ou senha inválidos.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro",
                    "Erro ao autenticar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void abrirTelaMenu() {
        try {
            URL fxmlUrl = LoginController.class.getResource("/fxml/menu.fxml");
            if (fxmlUrl == null) {
                mostrarAlerta("Erro", "Arquivo /fxml/menu.fxml não encontrado no classpath!", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            URL cssUrl = LoginController.class.getResource("/css/menu.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("AVISO: CSS menu.css não encontrado!");
            }

            Stage stage = new Stage();
            stage.setTitle("FinUnity - Menu Principal");
            stage.setScene(scene);
            stage.setResizable(true);

            // ADICIONAR ÍCONE AQUI 👇
            stage.getIcons().add(
                    new javafx.scene.image.Image(
                            getClass().getResource("/images/finUnity.png").toExternalForm()
                    )
            );

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir o Menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void fecharTelaAtual() {
        Stage stage = (Stage) txtLogin.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
