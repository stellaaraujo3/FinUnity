package view.controller;

import dao.DBConnection;
import dao.MovimentacaoFinanceiraDAO;
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
import model.MovimentacaoFinanceira;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class MovimentacaoFinanceiraController {

    @FXML
    private TableView<MovimentacaoFinanceira> tabelaMovimentacoes;

    @FXML
    private TableColumn<MovimentacaoFinanceira, LocalDate> colData;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colTipo;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colDescricao;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colEmpresa;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colValor;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colBanco;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colForma;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colSolicitante;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colComprovante;

    @FXML
    private TableColumn<MovimentacaoFinanceira, String> colNotaFiscal;

    @FXML
    private ComboBox<String> cbTipoFiltro;

    @FXML
    private ComboBox<String> cbEmpresaFiltro;

    @FXML
    private ComboBox<String> cbSolicitantesFiltro;

    @FXML
    private ComboBox<String> cbFormaPagamentoFiltro;

    @FXML
    private ComboBox<String> cbBancoFiltro;

    @FXML
    private DatePicker dpDataInicial;

    @FXML
    private DatePicker dpDataFinal;


    @FXML
    private TextField txtTotalSaidas;

    @FXML
    private TextField txtTotalEntradas;

    @FXML
    private TextField txtTotalSubtraido;

    private ObservableList<MovimentacaoFinanceira> dados = FXCollections.observableArrayList();
    private List<MovimentacaoFinanceira> listaOriginal = new ArrayList<>();

    private MovimentacaoFinanceiraDAO dao;

    private Map<Integer, String> solicitanteMap = new HashMap<>();
    private ObservableList<String> nomesSolicitantes = FXCollections.observableArrayList();

    private final ObservableList<String> tipos = FXCollections.observableArrayList("Entrada", "Saída");
    private final ObservableList<String> empresas = FXCollections.observableArrayList("COALUMAQ", "GOIAS LOC", "SERVICE");
    private final ObservableList<String> formasPagamento = FXCollections.observableArrayList("PIX", "DEPÓSITO", "DINHEIRO", "TRANSFERÊNCIA", "DEPOSITO");
    private final ObservableList<String> bancos = FXCollections.observableArrayList("BRADESCO", "BRASIL", "ITAÚ", "1930", "COFRE");

    @FXML
    public void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();
        dao = new MovimentacaoFinanceiraDAO(conn);

        carregarSolicitantes(conn);

        // Inicializar filtros
        inicializarFiltros();

        configurarTabela();

        // Carregar dados
        listaOriginal = dao.listarTodos();

        dados.clear();
        dados.addAll(listaOriginal);
        tabelaMovimentacoes.setItems(dados);
        tabelaMovimentacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        atualizarTotais();

    }

    private void inicializarFiltros() {
        cbBancoFiltro.setItems(FXCollections.observableArrayList("Todas", "BRADESCO", "BRASIL", "ITAÚ", "1930", "COFRE"));
        cbBancoFiltro.getSelectionModel().select("Todas");

        cbEmpresaFiltro.setItems(FXCollections.observableArrayList("Todas", "COALUMAQ", "GOIAS LOC", "SERVICE"));
        cbEmpresaFiltro.getSelectionModel().select("Todas");

        cbFormaPagamentoFiltro.setItems(FXCollections.observableArrayList("Todas", "PIX", "DINHEIRO", "TRANSFERÊNCIA", "DEPOSITO"));
        cbFormaPagamentoFiltro.getSelectionModel().select("Todas");

        cbTipoFiltro.setItems(FXCollections.observableArrayList("Todas", "Entrada", "Saída"));
        cbTipoFiltro.getSelectionModel().select("Todas");

        ObservableList<String> solicitantesComTodas = FXCollections.observableArrayList("Todas");
        solicitantesComTodas.addAll(nomesSolicitantes);
        cbSolicitantesFiltro.setItems(solicitantesComTodas);
        cbSolicitantesFiltro.getSelectionModel().select("Todas");
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

    private void configurarTabela() {
        colData.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(e -> {
                    MovimentacaoFinanceira item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        item.setData(datePicker.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    MovimentacaoFinanceira mov = getTableView().getItems().get(getIndex());
                    if (mov.getData() == null) {
                        mov.setData(LocalDate.now());
                    }
                    datePicker.setValue(mov.getData());
                    setGraphic(datePicker);
                }
            }
        });

        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colTipo.setCellFactory(ComboBoxTableCell.forTableColumn(tipos));
        colTipo.setOnEditCommit(event -> event.getRowValue().setTipo(event.getNewValue()));

        colDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());
        colDescricao.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescricao.setOnEditCommit(event -> event.getRowValue().setDescricao(event.getNewValue()));

        colEmpresa.setCellValueFactory(cellData -> cellData.getValue().empresaProperty());
        colEmpresa.setCellFactory(ComboBoxTableCell.forTableColumn(empresas));
        colEmpresa.setOnEditCommit(event -> event.getRowValue().setEmpresa(event.getNewValue()));

        colValor.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getValor())));
        colValor.setCellFactory(TextFieldTableCell.forTableColumn());
        colValor.setOnEditCommit(event -> {
            try {
                String v = event.getNewValue().replace("R$", "").replace(".", "").replace(",", ".");
                double valor = Double.parseDouble(v);
                event.getRowValue().setValor(valor);
                atualizarTotais();
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Valor inválido.", Alert.AlertType.ERROR);
                tabelaMovimentacoes.refresh();
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
        colComprovante.setCellFactory(tc -> criarBotaoUpload("Selecionar comprovante...", (mov, file) ->
                mov.setComprovante(file.getName())
        ));

        colNotaFiscal.setCellValueFactory(cellData -> cellData.getValue().notaFiscalProperty());
        colNotaFiscal.setCellFactory(tc -> criarBotaoUpload("Selecionar nota fiscal...", (mov, file) ->
                mov.setNotaFiscal(file.getName())
        ));

        tabelaMovimentacoes.setEditable(true);
    }

    private TableCell<MovimentacaoFinanceira, String> criarBotaoUpload(String tooltip, UploadHandler handler) {
        return new TableCell<>() {
            private final Button btn = new Button("Selecionar...");

            {
                btn.setOnAction(e -> {
                    MovimentacaoFinanceira mov = getTableView().getItems().get(getIndex());
                    FileChooser fc = new FileChooser();
                    File file = fc.showOpenDialog(getScene().getWindow());
                    if (file != null) {
                        handler.upload(mov, file);
                        tabelaMovimentacoes.refresh();
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
        void upload(MovimentacaoFinanceira mov, File file);
    }

    @FXML
    private void adicionarLinha() {
        MovimentacaoFinanceira nova = new MovimentacaoFinanceira();
        nova.setData(LocalDate.now());
        dados.add(nova);
    }

    @FXML
    private void excluirLinha() {
        MovimentacaoFinanceira selecionada = tabelaMovimentacoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir a linha?");
            alert.setContentText("Esta ação não poderá ser desfeita.");

            ButtonType okButton = new ButtonType("Sim", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("Não", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    try {
                        if (selecionada.getId() != 0) {
                            dao.excluir(selecionada.getId());
                        }
                        dados.remove(selecionada);
                        tabelaMovimentacoes.getItems().remove(selecionada);
                        atualizarTotais();
                        mostrarAlerta("Sucesso", "Linha excluída com sucesso!", Alert.AlertType.INFORMATION);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mostrarAlerta("Erro", "Erro ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            mostrarAlerta("Seleção necessária", "Selecione uma linha para excluir.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void salvarDados() {
        try {
            for (MovimentacaoFinanceira m : dados) {
                if (!validar(m)) {
                    mostrarAlerta("Validação", "Preencha todos os campos obrigatórios.", Alert.AlertType.WARNING);
                    return;
                }
                if (m.getId() == 0) {
                    dao.inserir(m);
                } else {
                    dao.atualizar(m);
                }
            }
            mostrarAlerta("Sucesso", "Movimentações salvas com sucesso!", Alert.AlertType.INFORMATION);
            listaOriginal = dao.listarTodos();
            dados.clear();
            dados.addAll(listaOriginal);
            tabelaMovimentacoes.setItems(dados);
            atualizarTotais();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao salvar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validar(MovimentacaoFinanceira m) {
        return m.getData() != null &&
                m.getTipo() != null &&
                m.getDescricao() != null &&
                m.getEmpresa() != null &&
                m.getValor() != 0 &&
                m.getBanco() != null &&
                m.getFormaPagamento() != null &&
                m.getSolicitanteId() != 0 &&
                m.getComprovante() != null &&
                !m.getComprovante().isEmpty();
    }

    @FXML
    private void filtrarMovimentacoes() {
        LocalDate dataInicial = dpDataInicial.getValue();
        LocalDate dataFinal = dpDataFinal.getValue();

        String tipo = cbTipoFiltro.getValue();
        String banco = cbBancoFiltro.getValue();
        String empresa = cbEmpresaFiltro.getValue();
        String formaPagamento = cbFormaPagamentoFiltro.getValue();
        String solicitante = cbSolicitantesFiltro.getValue();

        List<MovimentacaoFinanceira> filtradas = listaOriginal.stream()
                .filter(m -> {
                    boolean ok = true;

                    if (dataInicial != null) {
                        ok &= !m.getData().isBefore(dataInicial);
                    }
                    if (dataFinal != null) {
                        ok &= !m.getData().isAfter(dataFinal);
                    }
                    if (tipo != null && !"Todas".equals(tipo)) {
                        ok &= tipo.equalsIgnoreCase(m.getTipo());
                    }
                    if (banco != null && !"Todas".equals(banco)) {
                        ok &= banco.equalsIgnoreCase(m.getBanco());
                    }
                    if (empresa != null && !"Todas".equals(empresa)) {
                        ok &= empresa.equalsIgnoreCase(m.getEmpresa());
                    }
                    if (formaPagamento != null && !"Todas".equals(formaPagamento)) {
                        ok &= formaPagamento.equalsIgnoreCase(m.getFormaPagamento());
                    }
                    if (solicitante != null && !"Todas".equals(solicitante)) {
                        String nomeSolicitante = solicitanteMap.getOrDefault(m.getSolicitanteId(), "");
                        ok &= solicitante.equalsIgnoreCase(nomeSolicitante);
                    }

                    return ok;
                })
                .toList();

        tabelaMovimentacoes.setItems(FXCollections.observableArrayList(filtradas));
        atualizarTotais();
    }

    private void atualizarTotais() {
        double entradas = 0.0;
        double saidas = 0.0;

        for (MovimentacaoFinanceira m : tabelaMovimentacoes.getItems()) {
            if ("Entrada".equalsIgnoreCase(m.getTipo())) {
                entradas += m.getValor();
            } else if ("Saída".equalsIgnoreCase(m.getTipo())) {
                saidas += m.getValor();
            }
        }

        double totalSubtraido = entradas - saidas;

        txtTotalEntradas.setText(String.format("R$ %.2f", entradas));
        txtTotalSaidas.setText(String.format("R$ %.2f", saidas));
        txtTotalSubtraido.setText(String.format("R$ %.2f", totalSubtraido));
    }

    private Scene getScene() {
        return tabelaMovimentacoes.getScene();
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

            Stage current = (Stage) tabelaMovimentacoes.getScene().getWindow();
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
