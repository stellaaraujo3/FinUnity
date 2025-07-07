package view.controller;

import dao.DBConnection;
import dao.ValeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Vale;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ValesController {

    @FXML
    private TableView<Vale> tabelaVales;

    @FXML
    private TableColumn<Vale, LocalDate> colData;

    @FXML
    private TableColumn<Vale, String> colBeneficiario;

    @FXML
    private TableColumn<Vale, String> colEmpresa;

    @FXML
    private TableColumn<Vale, String> colFormaDesconto;

    @FXML
    private TableColumn<Vale, String> colParcelas;

    @FXML
    private TableColumn<Vale, String> colValor;

    @FXML
    private TableColumn<Vale, String> colBanco;

    @FXML
    private TableColumn<Vale, String> colForma;

    @FXML
    private TableColumn<Vale, String> colSolicitante;

    @FXML
    private TableColumn<Vale, String> colComprovante;

    @FXML
    private TableColumn<Vale, String> colRecibo;

    @FXML
    private TableColumn<Vale, String> colSituacao;

    @FXML
    private ComboBox<String> cbSituacaoFiltro;

    @FXML
    private ComboBox<String> cbBancoFiltro;

    @FXML
    private ComboBox<String> cbSolicitantesFiltro;

    @FXML
    private ComboBox<String> cbFormaDescontoFiltro;

    @FXML
    private ComboBox<String> cbEmpresaFiltro;

    @FXML
    private ComboBox<String> cbBeneficiarioFiltro;

    @FXML
    private DatePicker dpDataInicial;

    @FXML
    private DatePicker dpDataFinal;

    @FXML
    private TextField txtTotal;

    private ObservableList<Vale> dados = FXCollections.observableArrayList();

    private ValeDAO dao;

    private Map<Integer, String> solicitanteMap = new HashMap<>();
    private ObservableList<String> nomesSolicitantes = FXCollections.observableArrayList();

    private Map<Integer, String> beneficiarioMap = new HashMap<>();
    private ObservableList<String> nomesBeneficiarios = FXCollections.observableArrayList();

    private final ObservableList<String> situacoes = FXCollections.observableArrayList("EM ABERTO", "QUITADO");
    private final ObservableList<String> empresas = FXCollections.observableArrayList("COALUMAQ", "GOIAS LOC", "SERVICE");
    private final ObservableList<String> formasDesconto = FXCollections.observableArrayList(
            "A VISTA",
            "PARCELADO",
            "DESCONTO EM COMISSÃO",
            "DESCONTO EM HORAS EXTRAS",
            "DESCONTO VIA CAJU",
            "ANTECIPAÇÃO DE DÉCIMO TERCEIRO",
            "ANTECIPAÇÃO DE SALÁRIO",
            "ANTECIPAÇÃO DE FÉRIAS"
    );

    private final ObservableList<String> formasPagamento = FXCollections.observableArrayList(
            "PIX", "DEPÓSITO", "DINHEIRO", "TRANSFERÊNCIA"
    );
    private final ObservableList<String> bancos = FXCollections.observableArrayList(
            "BRADESCO", "BRASIL", "ITAÚ", "COFRE"
    );
    private final ObservableList<String> parcelasList = FXCollections.observableArrayList(
            "2x", "3x", "4x", "5x", "6x", "7x", "8x", "9x", "10x", "11x", "12x"
    );

    @FXML
    public void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();
        dao = new ValeDAO(conn);

        carregarBeneficiarios(conn);
        carregarSolicitantes(conn);
        configurarFiltros();
        configurarTabela();

        List<Vale> lista = dao.listarTodos();
        dados.addAll(lista);
        tabelaVales.setItems(dados);
        tabelaVales.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        atualizarTotal();
    }

    private void carregarBeneficiarios(Connection conn) throws Exception {
        String sql = "SELECT id, nome FROM beneficiarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            beneficiarioMap.clear();
            nomesBeneficiarios.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                beneficiarioMap.put(id, nome);
                nomesBeneficiarios.add(nome);
            }
        }
    }

    private void carregarSolicitantes(Connection conn) throws Exception {
        String sql = "SELECT id, nome FROM solicitantes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            solicitanteMap.clear();
            nomesSolicitantes.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                solicitanteMap.put(id, nome);
                nomesSolicitantes.add(nome);
            }
        }
    }

    private void configurarFiltros() {
        cbSituacaoFiltro.setItems(FXCollections.observableArrayList(concatTodas(situacoes)));
        cbSituacaoFiltro.getSelectionModel().select("Todas");

        cbBancoFiltro.setItems(FXCollections.observableArrayList(concatTodas(bancos)));
        cbBancoFiltro.getSelectionModel().select("Todas");

        cbSolicitantesFiltro.setItems(FXCollections.observableArrayList(concatTodas(nomesSolicitantes)));
        cbSolicitantesFiltro.getSelectionModel().select("Todas");

        cbFormaDescontoFiltro.setItems(FXCollections.observableArrayList(concatTodas(formasDesconto)));
        cbFormaDescontoFiltro.getSelectionModel().select("Todas");

        cbEmpresaFiltro.setItems(FXCollections.observableArrayList(concatTodas(empresas)));
        cbEmpresaFiltro.getSelectionModel().select("Todas");

        cbBeneficiarioFiltro.setItems(FXCollections.observableArrayList(concatTodas(nomesBeneficiarios)));
        cbBeneficiarioFiltro.getSelectionModel().select("Todas");
    }

    private List<String> concatTodas(List<String> original) {
        List<String> lista = new ArrayList<>();
        lista.add("Todas");
        lista.addAll(original);
        return lista;
    }

    private void configurarTabela() {
        colData.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(e -> {
                    Vale item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        item.setData(datePicker.getValue());
                    }
                });
                datePicker.setPrefWidth(120);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Vale vale = getTableView().getItems().get(getIndex());
                    if (vale.getData() == null) {
                        vale.setData(LocalDate.now());
                    }
                    datePicker.setValue(vale.getData());
                    setGraphic(datePicker);
                }
            }
        });

        colBeneficiario.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getBeneficiarioId();
            String nome = beneficiarioMap.getOrDefault(id, "");
            return new SimpleStringProperty(nome);
        });
        colBeneficiario.setCellFactory(ComboBoxTableCell.forTableColumn(nomesBeneficiarios));
        colBeneficiario.setOnEditCommit(event -> {
            String nome = event.getNewValue();
            int id = beneficiarioMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(nome))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            event.getRowValue().setBeneficiarioId(id);
        });

        colEmpresa.setCellValueFactory(cellData -> cellData.getValue().empresaProperty());
        colEmpresa.setCellFactory(ComboBoxTableCell.forTableColumn(empresas));
        colEmpresa.setOnEditCommit(event -> event.getRowValue().setEmpresa(event.getNewValue()));

        colFormaDesconto.setCellValueFactory(cellData -> cellData.getValue().formaDescontoProperty());
        colFormaDesconto.setCellFactory(ComboBoxTableCell.forTableColumn(formasDesconto));
        colFormaDesconto.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            Vale v = event.getRowValue();
            v.setFormaDesconto(newValue);
            if (!"PARCELADO".equals(newValue)) {
                v.setQtdParcelas(null);
            }
            tabelaVales.refresh();
        });

        colParcelas.setCellValueFactory(cellData -> cellData.getValue().qtdParcelasProperty());
        colParcelas.setCellFactory(column -> new ComboBoxTableCell<>(parcelasList) {
            @Override
            public void startEdit() {
                Vale v = getTableView().getItems().get(getIndex());
                if (v != null && "PARCELADO".equals(v.getFormaDesconto())) {
                    super.startEdit();
                }
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Vale v = getTableView().getItems().get(getIndex());
                    if (v != null && !"PARCELADO".equals(v.getFormaDesconto())) {
                        setText("");
                        setGraphic(null);
                    }
                }
            }
        });
        colParcelas.setOnEditCommit(event -> {
            String valor = event.getNewValue();
            if (valor != null && !valor.isEmpty()) {
                int qtd = Integer.parseInt(valor.replace("x", ""));
                event.getRowValue().setQtdParcelas(qtd);
            } else {
                event.getRowValue().setQtdParcelas(null);
            }
        });

        colValor.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getValor())));
        colValor.setCellFactory(TextFieldTableCell.forTableColumn());
        colValor.setOnEditCommit(event -> {
            try {
                String v = event.getNewValue().replace("R$", "").replace(".", "").replace(",", ".");
                double valor = Double.parseDouble(v);
                event.getRowValue().setValor(valor);
                atualizarTotal();
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Valor inválido.", Alert.AlertType.ERROR);
                tabelaVales.refresh();
            }
        });

        colBanco.setCellValueFactory(cellData -> cellData.getValue().bancoProperty());
        colBanco.setCellFactory(ComboBoxTableCell.forTableColumn(bancos));
        colBanco.setOnEditCommit(event -> event.getRowValue().setBanco(event.getNewValue()));

        colForma.setCellValueFactory(cellData -> cellData.getValue().formaPagamentoProperty());
        colForma.setCellFactory(ComboBoxTableCell.forTableColumn(formasPagamento));
        colForma.setOnEditCommit(event -> event.getRowValue().setFormaPagamento(event.getNewValue()));

        colSolicitante.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getSolicitanteId();
            String nome = solicitanteMap.getOrDefault(id, "");
            return new SimpleStringProperty(nome);
        });
        colSolicitante.setCellFactory(ComboBoxTableCell.forTableColumn(nomesSolicitantes));
        colSolicitante.setOnEditCommit(event -> {
            String nome = event.getNewValue();
            int id = solicitanteMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(nome))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            event.getRowValue().setSolicitanteId(id);
        });

        colComprovante.setCellValueFactory(cellData -> cellData.getValue().comprovanteProperty());
        colComprovante.setCellFactory(tc -> criarBotaoUpload("Selecionar comprovante...", (vale, file) ->
                vale.setComprovante(file.getName())
        ));

        colRecibo.setCellValueFactory(cellData -> cellData.getValue().reciboAssinadoProperty());
        colRecibo.setCellFactory(tc -> criarBotaoUpload("Selecionar recibo assinado...", (vale, file) ->
                vale.setReciboAssinado(file.getName())
        ));

        colSituacao.setCellValueFactory(cellData -> cellData.getValue().situacaoProperty());
        colSituacao.setCellFactory(ComboBoxTableCell.forTableColumn(situacoes));
        colSituacao.setOnEditCommit(event -> event.getRowValue().setSituacao(event.getNewValue()));

        tabelaVales.setEditable(true);
    }

    private TableCell<Vale, String> criarBotaoUpload(String tooltip, UploadHandler handler) {
        return new TableCell<>() {
            private final Button btn = new Button("Selecionar...");

            {
                btn.setOnAction(e -> {
                    Vale vale = getTableView().getItems().get(getIndex());
                    FileChooser fc = new FileChooser();
                    File file = fc.showOpenDialog(getScene().getWindow());
                    if (file != null) {
                        handler.upload(vale, file);
                        tabelaVales.refresh();
                    }
                });
                btn.setTooltip(new Tooltip(tooltip));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    setText(item != null && !item.isEmpty() ? item : "Nenhum arquivo");
                }
            }
        };
    }

    interface UploadHandler {
        void upload(Vale vale, File file);
    }

    @FXML
    private void adicionarLinha() {
        Vale nova = new Vale();
        nova.setData(LocalDate.now());
        dados.add(nova);
    }

    @FXML
    private void excluirLinha() {
        Vale selecionado = tabelaVales.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Tem certeza que deseja excluir esta linha?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    dao.excluir(selecionado.getId());
                    dados.remove(selecionado);
                    atualizarTotal();
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Erro ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Seleção necessária", "Selecione uma linha para excluir.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void salvarDados() {
        try {
            for (Vale v : dados) {
                if (!validar(v)) {
                    mostrarAlerta("Validação", "Preencha todos os campos obrigatórios.", Alert.AlertType.WARNING);
                    return;
                }
                if (v.getId() == 0) {
                    dao.inserir(v);
                } else {
                    dao.atualizar(v);
                }
            }
            mostrarAlerta("Sucesso", "Vales salvos com sucesso!", Alert.AlertType.INFORMATION);
            atualizarTotal();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao salvar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validar(Vale v) {
        boolean valido = v.getData() != null &&
                v.getBeneficiarioId() != 0 &&
                v.getEmpresa() != null && !v.getEmpresa().isEmpty() &&
                v.getFormaDesconto() != null && !v.getFormaDesconto().isEmpty() &&
                v.getValor() > 0 &&
                v.getBanco() != null && !v.getBanco().isEmpty() &&
                v.getFormaPagamento() != null && !v.getFormaPagamento().isEmpty() &&
                v.getSolicitanteId() != 0 &&
                v.getComprovante() != null && !v.getComprovante().isEmpty() &&
                v.getSituacao() != null && !v.getSituacao().isEmpty();

        if (valido && "PARCELADO".equals(v.getFormaDesconto())) {
            valido &= v.getQtdParcelas() != null;
        }
        return valido;
    }

    @FXML
    private void filtrarVales() {
        LocalDate dataInicial = dpDataInicial.getValue();
        LocalDate dataFinal = dpDataFinal.getValue();

        String situacao = cbSituacaoFiltro.getValue();
        String banco = cbBancoFiltro.getValue();
        String empresa = cbEmpresaFiltro.getValue();
        String formaDesconto = cbFormaDescontoFiltro.getValue();
        String solicitante = cbSolicitantesFiltro.getValue();
        String beneficiario = cbBeneficiarioFiltro.getValue();

        List<Vale> filtradas = dados.stream()
                .filter(v -> {
                    boolean ok = true;
                    if (dataInicial != null) {
                        ok &= !v.getData().isBefore(dataInicial);
                    }
                    if (dataFinal != null) {
                        ok &= !v.getData().isAfter(dataFinal);
                    }
                    if (situacao != null && !"Todas".equals(situacao)) {
                        ok &= situacao.equalsIgnoreCase(v.getSituacao());
                    }
                    if (banco != null && !"Todas".equals(banco)) {
                        ok &= banco.equalsIgnoreCase(v.getBanco());
                    }
                    if (empresa != null && !"Todas".equals(empresa)) {
                        ok &= empresa.equalsIgnoreCase(v.getEmpresa());
                    }
                    if (formaDesconto != null && !"Todas".equals(formaDesconto)) {
                        ok &= formaDesconto.equalsIgnoreCase(v.getFormaDesconto());
                    }
                    if (solicitante != null && !"Todas".equals(solicitante)) {
                        ok &= solicitante.equalsIgnoreCase(solicitanteMap.getOrDefault(v.getSolicitanteId(), ""));
                    }
                    if (beneficiario != null && !"Todas".equals(beneficiario)) {
                        ok &= beneficiario.equalsIgnoreCase(beneficiarioMap.getOrDefault(v.getBeneficiarioId(), ""));
                    }
                    return ok;
                })
                .collect(Collectors.toList());

        tabelaVales.setItems(FXCollections.observableArrayList(filtradas));
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = tabelaVales.getItems().stream()
                .mapToDouble(Vale::getValor)
                .sum();
        txtTotal.setText(String.format("R$ %.2f", total));
    }

    private Scene getScene() {
        return tabelaVales.getScene();
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

            Stage current = (Stage) tabelaVales.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar ao menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

