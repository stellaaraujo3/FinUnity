package view.controller;
import util.*;
import dao.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import util.EmailUtils;
import java.io.File;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class RelatoriosController {

    @FXML
    private DatePicker dpPeriodoInicial;

    @FXML
    private DatePicker dpPeriodoFinal;

    @FXML
    private ComboBox<String> cbReferente;

    @FXML
    private ComboBox<String> cbSolicitante;

    @FXML
    private ComboBox<String> cbBeneficiario;

    @FXML
    private ComboBox<String> cbEmpresa;

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private TableView<Relatorio> tabelaRelatorios;

    @FXML
    private TableColumn<Relatorio, String> colNumeroRequisicao;

    @FXML
    private TableColumn<Relatorio, String> colPeriodoInicial;

    @FXML
    private TableColumn<Relatorio, String> colPeriodoFinal;

    @FXML
    private TableColumn<Relatorio, String> colDataHora;

    @FXML
    private TableColumn<Relatorio, Void> colAcoes;

    private ObservableList<Relatorio> dados = FXCollections.observableArrayList();

    private final ObservableList<String> referentes = FXCollections.observableArrayList(
            "MOVIMENTAÇÃO FINANCEIRA",
            "COMISSÕES",
            "VALES",
            "SALÁRIOS E FÉRIAS"
    );

    private final ObservableList<String> empresas = FXCollections.observableArrayList(
            "Todas",
            "COALUMAQ",
            "SERVICE",
            "GOIAS LOC"
    );


    private Map<Integer, String> solicitanteMap = new HashMap<>();
    private ObservableList<String> nomesSolicitantes = FXCollections.observableArrayList();

    private Map<Integer, String> beneficiarioMap = new HashMap<>();
    private ObservableList<String> nomesBeneficiarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        Connection conn = DBConnection.getConnection();

        cbReferente.setItems(referentes);
        cbEmpresa.setItems(empresas);
        cbEmpresa.getSelectionModel().selectFirst();

        carregarSolicitantes(conn);
        carregarBeneficiarios(conn);

        cbSolicitante.getSelectionModel().select("Todas");
        cbBeneficiario.getSelectionModel().select("Todas");

        cbTipo.setItems(FXCollections.observableArrayList("Todas", "ENTRADA", "SAÍDA"));
        cbTipo.getSelectionModel().selectFirst();

        configurarTabela();

        tabelaRelatorios.setItems(dados);
        tabelaRelatorios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarSolicitantes(Connection conn) throws Exception {
        SolicitanteDAO dao = new SolicitanteDAO(conn);
        List<Solicitante> lista = dao.listarTodos();
        nomesSolicitantes.clear();
        nomesSolicitantes.add("Todas");

        for (Solicitante s : lista) {
            solicitanteMap.put(s.getId(), s.getNome());
            nomesSolicitantes.add(s.getNome());
        }
        cbSolicitante.setItems(nomesSolicitantes);
    }

    private void carregarBeneficiarios(Connection conn) throws Exception {
        BeneficiarioDAO dao = new BeneficiarioDAO(conn);
        List<Beneficiario> lista = dao.listarTodos();
        nomesBeneficiarios.clear();
        nomesBeneficiarios.add("Todas");

        for (Beneficiario b : lista) {
            beneficiarioMap.put(b.getId(), b.getNome());
            nomesBeneficiarios.add(b.getNome());
        }
        cbBeneficiario.setItems(nomesBeneficiarios);
    }


    private void configurarTabela() {
        colNumeroRequisicao.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colPeriodoInicial.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPeriodoInicial() != null
                        ? cellData.getValue().getPeriodoInicial().format(dateFormatter) : "")
        );

        colPeriodoFinal.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPeriodoFinal() != null
                        ? cellData.getValue().getPeriodoFinal().format(dateFormatter) : "")
        );

        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDataHora.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataHoraRequisicao() != null
                        ? cellData.getValue().getDataHoraRequisicao().format(dtFormatter) : "")
        );

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnDownload;
            private final Button btnView;
            private final Button btnEmail;

            {
                btnDownload = criarBotaoComImagem("/images/dowload.png");
                btnView = criarBotaoComImagem("/images/formato-de-arquivo.png");
                btnEmail = criarBotaoComImagem("/images/email.png");

                btnDownload.setOnAction(e -> downloadRelatorio(getIndex()));
                btnView.setOnAction(e -> visualizarRelatorio(getIndex()));
                btnEmail.setOnAction(e -> enviarEmailRelatorio(getIndex()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnDownload, btnView, btnEmail);
                    setGraphic(box);
                }
            }
        });
    }

    private Button criarBotaoComImagem(String caminho) {
        Image img = new Image(Objects.requireNonNull(getClass().getResource(caminho)).toExternalForm());
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(24);
        imgView.setFitHeight(24);
        Button btn = new Button("", imgView);
        btn.setStyle("-fx-background-color: transparent;");
        return btn;
    }

    @FXML
    private void gerarRelatorio() {
        try {
            Connection conn = DBConnection.getConnection();

            LocalDate inicio = dpPeriodoInicial.getValue();
            LocalDate fim = dpPeriodoFinal.getValue();
            String referente = cbReferente.getValue();
            String solicitante = cbSolicitante.getValue();
            String beneficiario = cbBeneficiario.getValue();
            String empresa = cbEmpresa.getValue();
            if ("Todas".equalsIgnoreCase(empresa)) {
                empresa = null;
            }

            String tipo = cbTipo.getValue();

            MovimentacaoFinanceiraDAO movFinanceiraDAO = new MovimentacaoFinanceiraDAO(conn);
            ComissaoDAO comissaoDAO = new ComissaoDAO(conn);
            ValeDAO valeDAO = new ValeDAO(conn);
            SalarioFeriasDAO salarioFeriasDAO = new SalarioFeriasDAO(conn);

            List<?> lista = switch (referente) {
                case "MOVIMENTAÇÃO FINANCEIRA" ->
                        movFinanceiraDAO.buscarPorFiltros(inicio, fim, solicitante, empresa, tipo);
                case "COMISSÕES" ->
                        comissaoDAO.buscarPorFiltros(inicio, fim, solicitante, beneficiario, empresa);
                case "VALES" ->
                        valeDAO.buscarPorFiltros(inicio, fim, solicitante, beneficiario, empresa);
                case "SALÁRIOS E FÉRIAS" ->
                        salarioFeriasDAO.buscarPorFiltros(inicio, fim, solicitante, empresa);
                default -> List.of();
            };

            String nomeArquivo = "relatorio_" + System.currentTimeMillis() + ".pdf";
            String caminho = "src/main/resources/relatorios/" + nomeArquivo;
            gerarPDF(caminho, referente, lista);

            Relatorio rel = new Relatorio();
            rel.setPeriodoInicial(inicio);
            rel.setPeriodoFinal(fim);
            rel.setDataHoraRequisicao(LocalDateTime.now());
            rel.setSolicitante(solicitante);
            rel.setReferente(referente);
            rel.setBeneficiario(beneficiario);
            rel.setEmpresa(empresa);
            rel.setArquivoRelatorio(nomeArquivo);

            RelatorioDAO dao = new RelatorioDAO(conn);
            dao.inserir(rel);

            dados.add(rel);
            tabelaRelatorios.refresh();

            mostrarAlerta("Sucesso", "Relatório gerado com sucesso!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao gerar relatório:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void downloadRelatorio(int index) {
        try {
            Relatorio rel = tabelaRelatorios.getItems().get(index);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório");
            fileChooser.setInitialFileName(rel.getArquivoRelatorio());
            File destino = fileChooser.showSaveDialog(tabelaRelatorios.getScene().getWindow());
            if (destino != null) {
                File origem = new File("src/main/resources/relatorios/" + rel.getArquivoRelatorio());
                java.nio.file.Files.copy(
                        origem.toPath(),
                        destino.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                mostrarAlerta("Download", "Relatório salvo em:\n" + destino.getAbsolutePath(), Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao baixar relatório: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void visualizarRelatorio(int index) {
        try {
            Relatorio rel = tabelaRelatorios.getItems().get(index);
            File arquivo = new File("src/main/resources/relatorios/" + rel.getArquivoRelatorio());
            if (arquivo.exists()) {
                java.awt.Desktop.getDesktop().open(arquivo);
            } else {
                mostrarAlerta("Erro", "Arquivo não encontrado!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir relatório: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void enviarEmailRelatorio(int index) {
        try {
            Relatorio rel = tabelaRelatorios.getItems().get(index);

            String destinatario = "email@destino.com"; // coloque o e-mail de quem vai receber
            String assunto = "Relatório - " + rel.getReferente();
            String corpo = "Segue o relatório gerado.";

            // caminho físico do arquivo PDF:
            File arquivo = new File("src/main/resources/relatorios/" + rel.getArquivoRelatorio());

            EmailUtils.enviarEmailComAnexo(
                    destinatario,
                    assunto,
                    corpo,
                    arquivo
            );

            mostrarAlerta("Sucesso", "Relatório enviado por e-mail!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao enviar e-mail: " + e.getMessage(), Alert.AlertType.ERROR);
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

            Stage current = (Stage) tabelaRelatorios.getScene().getWindow();
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

    // ------------ PDF GENERATION ------------------

    private void gerarPDF(String caminho, String referente, List<?> lista) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 40, 40, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(caminho));
        document.open();

        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.WHITE);
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell cellTitle = new PdfPCell(new Phrase("Relatório - " + referente, tituloFont));
        cellTitle.setBackgroundColor(new Color(51, 102, 153));
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitle.setPadding(10);
        header.addCell(cellTitle);
        document.add(header);
        document.add(Chunk.NEWLINE);

        switch (referente) {
            case "MOVIMENTAÇÃO FINANCEIRA" -> gerarMovimentacoesFinanceiras(document, (List<MovimentacaoFinanceira>) lista);
            case "COMISSÕES" -> gerarComissoes(document, (List<Comissao>) lista);
            case "VALES" -> gerarVales(document, (List<Vale>) lista);
            case "SALÁRIOS E FÉRIAS" -> gerarSalariosEFerias(document, (List<SalarioFerias>) lista);
            default -> document.add(new Paragraph("Nenhum dado disponível."));
        }

        Paragraph rodape = new Paragraph("FinUnity - Relatório Financeiro", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLUE));
        rodape.setAlignment(Element.ALIGN_CENTER);
        rodape.setSpacingBefore(20);
        document.add(rodape);

        document.close();
    }

    private void gerarMovimentacoesFinanceiras(Document document, List<MovimentacaoFinanceira> lista) throws Exception {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 4, 3, 2, 3, 3});

        String[] headers = {"Data", "Tipo", "Descrição", "Empresa", "Valor", "Banco", "Forma Pag."};
        addTableHeader(table, headers);

        Font font = new Font(Font.HELVETICA, 10);
        double entradas = 0.0;
        double saidas = 0.0;

        for (MovimentacaoFinanceira m : lista) {
            table.addCell(createDataCell(m.getData().toString(), font));
            table.addCell(createDataCell(m.getTipo(), font));
            table.addCell(createDataCell(m.getDescricao(), font));
            table.addCell(createDataCell(m.getEmpresa(), font));
            table.addCell(createDataCell(String.format("R$ %.2f", m.getValor()), font, Element.ALIGN_RIGHT));
            table.addCell(createDataCell(m.getBanco(), font));
            table.addCell(createDataCell(m.getFormaPagamento(), font));

            if ("ENTRADA".equalsIgnoreCase(m.getTipo())) {
                entradas += m.getValor();
            } else if ("SAÍDA".equalsIgnoreCase(m.getTipo())) {
                saidas += m.getValor();
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        double totalLiquido = entradas - saidas;

        Paragraph totalEntradas = new Paragraph("Total Entradas: R$ " + String.format("%.2f", entradas),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE));
        totalEntradas.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalEntradas);

        Paragraph totalSaidas = new Paragraph("Total Saídas: R$ " + String.format("%.2f", saidas),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE));
        totalSaidas.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalSaidas);

        Paragraph totalLiquidoP = new Paragraph("Total Líquido (Entradas - Saídas): R$ " + String.format("%.2f", totalLiquido),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED));
        totalLiquidoP.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalLiquidoP);

        document.add(Chunk.NEWLINE);
    }

    private void gerarComissoes(Document document, List<Comissao> lista) throws Exception {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 3, 4, 2, 2, 2, 3});

        String[] headers = {"Data", "Tipo", "Beneficiário", "Descrição", "Valor", "Banco", "Forma Pag.", "Empresa"};
        addTableHeader(table, headers);

        Font font = new Font(Font.HELVETICA, 10);
        double total = 0.0;

        for (Comissao c : lista) {
            table.addCell(createDataCell(c.getData().toString(), font));
            table.addCell(createDataCell(c.getTipo(), font));
            table.addCell(createDataCell(String.valueOf(c.getBeneficiarioId()), font));
            table.addCell(createDataCell(c.getDescricao(), font));
            table.addCell(createDataCell(String.format("R$ %.2f", c.getValor()), font, Element.ALIGN_RIGHT));
            table.addCell(createDataCell(c.getBanco(), font));
            table.addCell(createDataCell(c.getFormaPagamento(), font));
            table.addCell(createDataCell(c.getEmpresa(), font));
            total += c.getValor();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph totalParagraph = new Paragraph("Total Comissões: R$ " + String.format("%.2f", total),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.add(Chunk.NEWLINE);
    }

    private void gerarVales(Document document, List<Vale> lista) throws Exception {
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 3, 3, 2, 2, 3, 2, 2});

        String[] headers = {
                "Data", "Beneficiário", "Empresa", "Forma Desconto", "Valor",
                "Banco", "Forma Pag.", "Situação", "Qtd Parcelas"
        };
        addTableHeader(table, headers);

        Font font = new Font(Font.HELVETICA, 10);
        double total = 0.0;

        for (Vale v : lista) {
            table.addCell(createDataCell(v.getData().toString(), font));
            table.addCell(createDataCell(String.valueOf(v.getBeneficiarioId()), font));
            table.addCell(createDataCell(v.getEmpresa(), font));
            table.addCell(createDataCell(v.getFormaDesconto(), font));
            table.addCell(createDataCell(String.format("R$ %.2f", v.getValor()), font, Element.ALIGN_RIGHT));
            table.addCell(createDataCell(v.getBanco(), font));
            table.addCell(createDataCell(v.getFormaPagamento(), font));
            table.addCell(createDataCell(v.getSituacao(), font));
            table.addCell(createDataCell(
                    v.getQtdParcelas() != null ? v.getQtdParcelas().toString() : "-", font
            ));
            total += v.getValor();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph totalParagraph = new Paragraph("Total Vales: R$ " + String.format("%.2f", total),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.add(Chunk.NEWLINE);
    }

    private void gerarSalariosEFerias(Document document, List<SalarioFerias> lista) throws Exception {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 3, 3, 2, 3, 3, 3});

        String[] headers = {
                "Data", "Tipo", "Solicitante", "Empresa", "Valor",
                "Banco", "Forma Pagamento", "Comprovante"
        };
        addTableHeader(table, headers);

        Font font = new Font(Font.HELVETICA, 10);
        double total = 0.0;

        for (SalarioFerias sf : lista) {
            table.addCell(createDataCell(sf.getData().toString(), font));
            table.addCell(createDataCell(sf.getTipo(), font));

            // Converte solicitante_id em nome:
            String nomeSolicitante = solicitanteMap.getOrDefault(sf.getSolicitanteId(), "Desconhecido");
            table.addCell(createDataCell(nomeSolicitante, font));

            table.addCell(createDataCell(sf.getEmpresa(), font));
            table.addCell(createDataCell(String.format("R$ %.2f", sf.getValor()), font, Element.ALIGN_RIGHT));
            table.addCell(createDataCell(sf.getBanco(), font));
            table.addCell(createDataCell(sf.getFormaPagamento(), font));
            table.addCell(createDataCell(sf.getComprovante(), font));
            total += sf.getValor();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph totalParagraph = new Paragraph(
                "Total Salários/Férias: R$ " + String.format("%.2f", total),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE)
        );
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.add(Chunk.NEWLINE);
    }


    private void addTableHeader(PdfPTable table, String[] headers) {
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        for (String col : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(new Color(204, 204, 204));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private PdfPCell createDataCell(String texto, Font font) {
        return createDataCell(texto, font, Element.ALIGN_LEFT);
    }

    private PdfPCell createDataCell(String texto, Font font, int alinhamento) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setMinimumHeight(25f);
        cell.setPaddingTop(6f);
        cell.setPaddingBottom(6f);
        cell.setHorizontalAlignment(alinhamento);
        return cell;
    }
}
