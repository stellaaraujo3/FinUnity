package view.controller;

import dao.ComissaoDAO;
import dao.DBConnection;
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
import model.Comissao;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ComissaoController {

    @FXML
    private TableView<Comissao> tabelaComissoes;

    @FXML
    private TableColumn<Comissao, LocalDate> colData;

    @FXML
    private TableColumn<Comissao, String> colTipo;

    @FXML
    private TableColumn<Comissao, String> colEmpresa;

    @FXML
    private TableColumn<Comissao, String> colDescricao;

    @FXML
    private TableColumn<Comissao, String> colValor;

    @FXML
    private TableColumn<Comissao, String> colBanco;

    @FXML
    private TableColumn<Comissao, String> colFormaPagamento;

    @FXML
    private TableColumn<Comissao, String> colSolicitante;

    @FXML
    private TableColumn<Comissao, String> colComprovante;

    @FXML
    private TableColumn<Comissao, String> colRelatorio;

    @FXML
    private ComboBox<String> cbTipoFiltro;

    @FXML
    private ComboBox<String> cbEmpresaFiltro;

    @FXML
    private ComboBox<String> cbBancoFiltro;

    @FXML
    private ComboBox<String> cbFormaPagamentoFiltro;

    @FXML
    private ComboBox<String> cbSolicitanteFiltro;

    @FXML
    private DatePicker dpDataInicial;

    @FXML
    private DatePicker dpDataFinal;

    @FXML
    private TextField txtTotalPago;

    @FXML
    private ComboBox<String> cbBeneficiarioFiltro;

    @FXML
    private TableColumn<Comissao, String> colBeneficiario;

    // Mapa para converter ID <-> Nome
    private Map<Integer, String> beneficiarioMap = new HashMap<>();
    private ObservableList<String> nomesBeneficiarios = FXCollections.observableArrayList();


    private ObservableList<Comissao> dados = FXCollections.observableArrayList();

    private ComissaoDAO dao;

    private Map<Integer, String> solicitanteMap = new HashMap<>();
    private ObservableList<String> nomesSolicitantes = FXCollections.observableArrayList();

    private final ObservableList<String> tipos = FXCollections.observableArrayList("Maquinas", "Peças", "Serviços");
    private final ObservableList<String> empresas = FXCollections.observableArrayList("COALUMAQ", "GOIAS LOC", "SERVICE");
    private final ObservableList<String> bancos = FXCollections.observableArrayList("BRADESCO", "BRASIL", "ITAÚ", "COFRE");
    private final ObservableList<String> formasPagamento = FXCollections.observableArrayList("PIX", "DINHEIRO", "TRANSFERÊNCIA", "DEPÓSITO");

    @FXML
    public void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();
        dao = new ComissaoDAO(conn);

        // Configurar combos com opção Todas
        cbTipoFiltro.setItems(incluirTodas(tipos));
        cbEmpresaFiltro.setItems(incluirTodas(empresas));
        cbBancoFiltro.setItems(incluirTodas(bancos));
        cbFormaPagamentoFiltro.setItems(incluirTodas(formasPagamento));

        carregarSolicitantes(conn);
        carregarBeneficiarios(conn);
        cbBeneficiarioFiltro.setItems(nomesBeneficiarios);
        cbBeneficiarioFiltro.getSelectionModel().select("Todas");
        cbTipoFiltro.getSelectionModel().select("Todas");
        cbEmpresaFiltro.getSelectionModel().select("Todas");
        cbBancoFiltro.getSelectionModel().select("Todas");
        cbFormaPagamentoFiltro.getSelectionModel().select("Todas");
        cbSolicitanteFiltro.getSelectionModel().select("Todas");

        configurarTabela();

        List<Comissao> lista = dao.listarTodos();
        dados.addAll(lista);
        tabelaComissoes.setItems(dados);
        tabelaComissoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        atualizarTotais();
    }

    private void carregarBeneficiarios(Connection conn) throws Exception {
        String sql = "SELECT id, nome FROM beneficiarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            beneficiarioMap.clear();
            nomesBeneficiarios.clear();
            nomesBeneficiarios.add("Todas");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                beneficiarioMap.put(id, nome);
                nomesBeneficiarios.add(nome);
            }
        }
    }


    private ObservableList<String> incluirTodas(List<String> lista) {
        List<String> novaLista = new ArrayList<>();
        novaLista.add("Todas");
        novaLista.addAll(lista);
        return FXCollections.observableArrayList(novaLista);
    }

    private void carregarSolicitantes(Connection conn) throws Exception {
        String sql = "SELECT id, nome FROM solicitantes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            solicitanteMap.clear();
            nomesSolicitantes.clear();
            nomesSolicitantes.add("Todas");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                solicitanteMap.put(id, nome);
                nomesSolicitantes.add(nome);
            }
            cbSolicitanteFiltro.setItems(nomesSolicitantes);
        }
    }

    private void configurarTabela() {
        // Data
        colData.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(e -> {
                    Comissao item = getTableView().getItems().get(getIndex());
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
                    Comissao mov = getTableView().getItems().get(getIndex());
                    if (mov.getData() == null) {
                        mov.setData(LocalDate.now());
                    }
                    datePicker.setValue(mov.getData());
                    setGraphic(datePicker);
                }
            }
        });

        colTipo.setCellValueFactory(cell -> cell.getValue().tipoProperty());
        colTipo.setCellFactory(ComboBoxTableCell.forTableColumn(tipos));
        colTipo.setOnEditCommit(e -> e.getRowValue().setTipo(e.getNewValue()));

        colEmpresa.setCellValueFactory(cell -> cell.getValue().empresaProperty());
        colEmpresa.setCellFactory(ComboBoxTableCell.forTableColumn(empresas));
        colEmpresa.setOnEditCommit(e -> e.getRowValue().setEmpresa(e.getNewValue()));

        colDescricao.setCellValueFactory(cell -> cell.getValue().descricaoProperty());
        colDescricao.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescricao.setOnEditCommit(e -> e.getRowValue().setDescricao(e.getNewValue()));

        colValor.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("R$ %.2f", cell.getValue().getValor())));
        colValor.setCellFactory(TextFieldTableCell.forTableColumn());
        colValor.setOnEditCommit(e -> {
            try {
                String v = e.getNewValue().replace("R$", "").replace(".", "").replace(",", ".");
                double valor = Double.parseDouble(v);
                e.getRowValue().setValor(valor);
                atualizarTotais();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro", "Valor inválido.", Alert.AlertType.ERROR);
                tabelaComissoes.refresh();
            }
        });

        colBanco.setCellValueFactory(cell -> cell.getValue().bancoProperty());
        colBanco.setCellFactory(ComboBoxTableCell.forTableColumn(bancos));
        colBanco.setOnEditCommit(e -> e.getRowValue().setBanco(e.getNewValue()));

        colFormaPagamento.setCellValueFactory(cell -> cell.getValue().formaPagamentoProperty());
        colFormaPagamento.setCellFactory(ComboBoxTableCell.forTableColumn(formasPagamento));
        colFormaPagamento.setOnEditCommit(e -> e.getRowValue().setFormaPagamento(e.getNewValue()));

        colSolicitante.setCellValueFactory(cell -> {
            int id = cell.getValue().getSolicitanteId();
            String nome = solicitanteMap.getOrDefault(id, "");
            return new SimpleStringProperty(nome);
        });
        colSolicitante.setCellFactory(ComboBoxTableCell.forTableColumn(nomesSolicitantes));
        colSolicitante.setOnEditCommit(e -> {
            String nome = e.getNewValue();
            int id = solicitanteMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(nome))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            e.getRowValue().setSolicitanteId(id);
        });

        colComprovante.setCellValueFactory(cell -> cell.getValue().comprovanteProperty());
        colComprovante.setCellFactory(tc -> criarBotaoUpload("Selecionar comprovante...", (mov, file) ->
                mov.setComprovante(file.getName())
        ));

        colRelatorio.setCellValueFactory(cell -> cell.getValue().relatorioProperty());
        colRelatorio.setCellFactory(tc -> criarBotaoUpload("Selecionar relatório...", (mov, file) ->
                mov.setRelatorioComissao(file.getName())
        ));

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


        tabelaComissoes.setEditable(true);
    }

    private TableCell<Comissao, String> criarBotaoUpload(String tooltip, UploadHandler handler) {
        return new TableCell<>() {
            private final Button btn = new Button("Selecionar...");

            {
                btn.setOnAction(e -> {
                    Comissao mov = getTableView().getItems().get(getIndex());
                    FileChooser fc = new FileChooser();
                    File file = fc.showOpenDialog(getScene().getWindow());
                    if (file != null) {
                        handler.upload(mov, file);
                        tabelaComissoes.refresh();
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
        void upload(Comissao mov, File file);
    }

    @FXML
    private void adicionarLinha() {
        Comissao nova = new Comissao();
        nova.setData(LocalDate.now());
        if (nomesBeneficiarios.size() > 1) {
            String nome = nomesBeneficiarios.get(1);
            int id = beneficiarioMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(nome))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            nova.setBeneficiarioId(id);
        }
        dados.add(nova);

    }

    @FXML
    private void excluirLinha() {
        Comissao selecionada = tabelaComissoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Tem certeza que deseja excluir a linha?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    if (selecionada.getId() != 0) {
                        dao.excluir(selecionada.getId());
                    }
                    dados.remove(selecionada);
                    atualizarTotais();
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Erro ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Seleção", "Selecione uma linha para excluir.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void salvarDados() {
        try {
            for (Comissao c : dados) {
                if (!validar(c)) {
                    mostrarAlerta("Validação", "Preencha todos os campos obrigatórios.", Alert.AlertType.WARNING);
                    return;
                }
                if (c.getId() == 0) {
                    dao.inserir(c);
                } else {
                    dao.atualizar(c);
                }
            }
            mostrarAlerta("Sucesso", "Comissões salvas com sucesso!", Alert.AlertType.INFORMATION);
            atualizarTotais();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao salvar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validar(Comissao c) {
        return c.getData() != null &&
                c.getTipo() != null &&
                c.getEmpresa() != null &&
                c.getDescricao() != null &&
                c.getValor() > 0 &&
                c.getBanco() != null &&
                c.getFormaPagamento() != null &&
                c.getSolicitanteId() != 0 &&
                c.getComprovante() != null;
    }

    @FXML
    private void filtrarComissoes() {
        LocalDate dataInicial = dpDataInicial.getValue();
        LocalDate dataFinal = dpDataFinal.getValue();

        String tipo = cbTipoFiltro.getValue();
        String banco = cbBancoFiltro.getValue();
        String empresa = cbEmpresaFiltro.getValue();
        String formaPagamento = cbFormaPagamentoFiltro.getValue();
        String solicitante = cbSolicitanteFiltro.getValue();
        String beneficiario = cbBeneficiarioFiltro.getValue();

        List<Comissao> filtradas = dados.stream()
                .filter(c -> {
                    boolean ok = true;

                    if (dataInicial != null) {
                        ok &= !c.getData().isBefore(dataInicial);
                    }
                    if (dataFinal != null) {
                        ok &= !c.getData().isAfter(dataFinal);
                    }
                    if (tipo != null && !tipo.equals("Todas")) {
                        ok &= tipo.equalsIgnoreCase(c.getTipo());
                    }
                    if (banco != null && !banco.equals("Todas")) {
                        ok &= banco.equalsIgnoreCase(c.getBanco());
                    }
                    if (empresa != null && !empresa.equals("Todas")) {
                        ok &= empresa.equalsIgnoreCase(c.getEmpresa());
                    }
                    if (formaPagamento != null && !formaPagamento.equals("Todas")) {
                        ok &= formaPagamento.equalsIgnoreCase(c.getFormaPagamento());
                    }
                    if (solicitante != null && !solicitante.equals("Todas")) {
                        Integer idSolic = solicitanteMap.entrySet().stream()
                                .filter(e -> e.getValue().equals(solicitante))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(null);
                        ok &= idSolic != null && c.getSolicitanteId() == idSolic;
                    }
                    if (beneficiario != null && !beneficiario.equals("Todas")) {
                        Integer idBenef = beneficiarioMap.entrySet().stream()
                                .filter(e -> e.getValue().equals(beneficiario))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(null);
                        ok &= idBenef != null && c.getBeneficiarioId() == idBenef;
                    }

                    return ok;
                })
                .toList();

        tabelaComissoes.setItems(FXCollections.observableArrayList(filtradas));

        // atualizar total pago
        double totalPago = filtradas.stream()
                .mapToDouble(Comissao::getValor)
                .sum();

        txtTotalPago.setText(String.format("R$ %.2f", totalPago));
    }


    private void atualizarTotais() {
        double total = tabelaComissoes.getItems().stream()
                .mapToDouble(Comissao::getValor)
                .sum();

        txtTotalPago.setText(String.format("R$ %.2f", total));
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

            Stage current = (Stage) tabelaComissoes.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar ao menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private Scene getScene() {
        return tabelaComissoes.getScene();
    }
}
