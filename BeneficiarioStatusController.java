package view.controller;

import dao.BeneficiarioDAO;
import dao.ValeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Beneficiario;
import dao.DBConnection;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BeneficiarioStatusController {

    @FXML
    private TableView<Beneficiario> tabelaBeneficiarios;

    @FXML
    private TableColumn<Beneficiario, String> colNome;

    @FXML
    private TableColumn<Beneficiario, String> colStatus;

    @FXML
    private TableColumn<Beneficiario, String> colLimiteDisponivel;

    @FXML
    private TableColumn<Beneficiario, Void> colAcoes;

    private ObservableList<Beneficiario> dados = FXCollections.observableArrayList();

    private NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            BeneficiarioDAO dao = new BeneficiarioDAO(conn);
            ValeDAO valeDAO = new ValeDAO(conn);
            List<Beneficiario> lista = dao.listarTodos();

            for (Beneficiario b : lista) {
                double totalValesEmAberto = valeDAO.obterTotalValesEmAbertoPorBeneficiario(b.getId());
                double limiteDisponivel = b.getLimiteVale() - totalValesEmAberto;

                if (limiteDisponivel < 0) {
                    limiteDisponivel = 0;
                }

                b.setLimiteDisponivel(limiteDisponivel);
                b.setStatus(definirStatus(limiteDisponivel));
            }

            dados.addAll(lista);

            configurarTabela();

            tabelaBeneficiarios.setItems(dados);
            tabelaBeneficiarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar beneficiários:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }

          // COLORIR LINHAS CONFORME STATUS
        tabelaBeneficiarios.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Beneficiario item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getStatus()) {
                        case "REGULAR" ->
                                setStyle("-fx-background-color: #d4edda;"); // verde claro
                        case "POUCO LIMITE" ->
                                setStyle("-fx-background-color: #fff3cd;"); // laranja claro
                        case "BLOQUEADO" ->
                                setStyle("-fx-background-color: #f8d7da;"); // vermelho claro
                        default ->
                                setStyle("");
                    }
                }
            }
        });


    }

    private void configurarTabela() {
        colNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNome())
        );

        colStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus())
        );

        colLimiteDisponivel.setCellValueFactory(cellData -> {
            double valor = cellData.getValue().getLimiteDisponivel();
            return new SimpleStringProperty(formatador.format(valor));
        });

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnRegular = new Button("REGULAR");
            private final Button btnPoucoLimite = new Button("POUCO LIMITE");
            private final Button btnBloqueado = new Button("BLOQUEADO");
            private final HBox box = new HBox(5, btnRegular, btnPoucoLimite, btnBloqueado);

            {
                btnRegular.setOnAction(e -> atualizarStatus(getIndex(), "REGULAR"));
                btnPoucoLimite.setOnAction(e -> atualizarStatus(getIndex(), "POUCO LIMITE"));
                btnBloqueado.setOnAction(e -> atualizarStatus(getIndex(), "BLOQUEADO"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
    }

    private void atualizarStatus(int index, String novoStatus) {
        try {
            Beneficiario b = tabelaBeneficiarios.getItems().get(index);
            b.setStatus(novoStatus);

            // Se bloquear, zera o limite disponível (não o limite original!)
            if (novoStatus.equals("BLOQUEADO")) {
                b.setLimiteDisponivel(0.0);
            } else {
                // recalcula o limite disponível com base no limite original - vales em aberto
                Connection conn = DBConnection.getConnection();
                ValeDAO valeDAO = new ValeDAO(conn);
                double totalValesEmAberto = valeDAO.obterTotalValesEmAbertoPorBeneficiario(b.getId());
                double novoLimiteDisponivel = b.getLimiteVale() - totalValesEmAberto;
                if (novoLimiteDisponivel < 0) {
                    novoLimiteDisponivel = 0.0;
                }
                b.setLimiteDisponivel(novoLimiteDisponivel);
            }

            // Atualiza status no banco
            Connection conn = DBConnection.getConnection();
            BeneficiarioDAO dao = new BeneficiarioDAO(conn);
            dao.atualizar(b);

            tabelaBeneficiarios.refresh();

            mostrarAlerta("Sucesso", "Status atualizado com sucesso!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao atualizar status:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    private String definirStatus(double limiteDisponivel) {
        if (limiteDisponivel == 0.0) {
            return "BLOQUEADO";
        } else if (limiteDisponivel <= 300.0) {
            return "POUCO LIMITE";
        } else {
            return "REGULAR";
        }
    }

    @FXML
    private void voltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("FinUnity - Menu Principal");
            stage.show();

            Stage current = (Stage) tabelaBeneficiarios.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar ao menu:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


}
