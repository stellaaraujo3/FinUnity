package dao;

import model.Relatorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    private final Connection conn;

    public RelatorioDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Relatorio rel) throws SQLException {
        String sql = """
            INSERT INTO relatorios
                (periodo_inicial, periodo_final, data_hora_requisicao,
                 solicitante, referente, beneficiario, empresa, arquivo_relatorio)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, rel.getPeriodoInicial() != null ? Date.valueOf(rel.getPeriodoInicial()) : null);
            stmt.setDate(2, rel.getPeriodoFinal() != null ? Date.valueOf(rel.getPeriodoFinal()) : null);
            stmt.setTimestamp(3, rel.getDataHoraRequisicao() != null ? Timestamp.valueOf(rel.getDataHoraRequisicao()) : null);
            stmt.setString(4, rel.getSolicitante());
            stmt.setString(5, rel.getReferente());
            stmt.setString(6, rel.getBeneficiario());
            stmt.setString(7, rel.getEmpresa());
            stmt.setString(8, rel.getArquivoRelatorio());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    rel.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Relatorio rel) throws SQLException {
        String sql = """
            UPDATE relatorios
            SET periodo_inicial = ?, periodo_final = ?, data_hora_requisicao = ?,
                solicitante = ?, referente = ?, beneficiario = ?, empresa = ?, arquivo_relatorio = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, rel.getPeriodoInicial() != null ? Date.valueOf(rel.getPeriodoInicial()) : null);
            stmt.setDate(2, rel.getPeriodoFinal() != null ? Date.valueOf(rel.getPeriodoFinal()) : null);
            stmt.setTimestamp(3, rel.getDataHoraRequisicao() != null ? Timestamp.valueOf(rel.getDataHoraRequisicao()) : null);
            stmt.setString(4, rel.getSolicitante());
            stmt.setString(5, rel.getReferente());
            stmt.setString(6, rel.getBeneficiario());
            stmt.setString(7, rel.getEmpresa());
            stmt.setString(8, rel.getArquivoRelatorio());
            stmt.setInt(9, rel.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM relatorios WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Relatorio> listarTodos() throws SQLException {
        List<Relatorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM relatorios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Relatorio rel = new Relatorio();
                rel.setId(rs.getInt("id"));
                rel.setPeriodoInicial(rs.getDate("periodo_inicial") != null ?
                        rs.getDate("periodo_inicial").toLocalDate() : null);
                rel.setPeriodoFinal(rs.getDate("periodo_final") != null ?
                        rs.getDate("periodo_final").toLocalDate() : null);
                rel.setDataHoraRequisicao(rs.getTimestamp("data_hora_requisicao") != null ?
                        rs.getTimestamp("data_hora_requisicao").toLocalDateTime() : null);
                rel.setSolicitante(rs.getString("solicitante"));
                rel.setReferente(rs.getString("referente"));
                rel.setBeneficiario(rs.getString("beneficiario"));
                rel.setEmpresa(rs.getString("empresa"));
                rel.setArquivoRelatorio(rs.getString("arquivo_relatorio"));

                lista.add(rel);
            }
        }
        return lista;
    }

    public Relatorio buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM relatorios WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Relatorio rel = new Relatorio();
                    rel.setId(rs.getInt("id"));
                    rel.setPeriodoInicial(rs.getDate("periodo_inicial") != null
                            ? rs.getDate("periodo_inicial").toLocalDate() : null);
                    rel.setPeriodoFinal(rs.getDate("periodo_final") != null
                            ? rs.getDate("periodo_final").toLocalDate() : null);
                    rel.setDataHoraRequisicao(rs.getTimestamp("data_hora_requisicao") != null
                            ? rs.getTimestamp("data_hora_requisicao").toLocalDateTime() : null);
                    rel.setSolicitante(rs.getString("solicitante"));
                    rel.setReferente(rs.getString("referente"));
                    rel.setBeneficiario(rs.getString("beneficiario"));
                    rel.setEmpresa(rs.getString("empresa"));
                    rel.setArquivoRelatorio(rs.getString("arquivo_relatorio"));

                    return rel;
                }
            }
        }
        return null;
    }
}
