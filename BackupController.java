package view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupController {

    @FXML
    private Label lblUltimoBackup;

    @FXML
    private Button btnBackup;

    private final String backupFolder = "src/main/resources/backup_dados"; // pasta de dados
    private final String destinoFolder = "src/main/resources/backups";      // pasta onde salvar o zip
    private String ultimoStatus = "nenhum backup ainda";

    @FXML
    public void initialize() {
        // Carrega informação fictícia ao abrir a tela
        atualizarLabelUltimoBackup(null, ultimoStatus);
    }

    @FXML
    private void forcarBackup() {
        try {
            // Cria pasta se não existir
            Files.createDirectories(Paths.get(destinoFolder));

            // Define nome do arquivo
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nomeZip = "backup_" + timestamp + ".zip";

            Path zipPath = Paths.get(destinoFolder, nomeZip);

            // Faz o zip
            zipDirectory(Paths.get(backupFolder), zipPath);

            ultimoStatus = "sucesso";
            atualizarLabelUltimoBackup(LocalDateTime.now(), ultimoStatus);
            mostrarAlerta("Sucesso", "Backup realizado com sucesso!\nArquivo salvo em:\n" + zipPath.toString(), Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            ultimoStatus = "falhou";
            atualizarLabelUltimoBackup(LocalDateTime.now(), ultimoStatus);
            mostrarAlerta("Erro", "Erro ao realizar backup: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void atualizarLabelUltimoBackup(LocalDateTime data, String status) {
        if (data == null) {
            lblUltimoBackup.setText("Último backup realizado em: --/--/---- às --:-- - " + status);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            lblUltimoBackup.setText("Último backup realizado em: " + data.format(formatter) + " - " + status);
        }
    }

    private void zipDirectory(Path sourceDirPath, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walk(sourceDirPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
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
