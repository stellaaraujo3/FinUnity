package dao;

import model.Comissao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ComissaoDAO {

    private final Connection conn;

    public ComissaoDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Comissao comissao) throws SQLException {
        String sql = """
            INSERT INTO comissoes
                (data, tipo, descricao, id_beneficiario, valor, banco, forma_pagamento, solicitante_id, comprovante, relatorio, empresa)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(comissao.getData()));
            stmt.setString(2, comissao.getTipo());
            stmt.setString(3, comissao.getDescricao());
            stmt.setInt(4, comissao.getBeneficiarioId());
            stmt.setDouble(5, comissao.getValor());
            stmt.setString(6, comissao.getBanco());
            stmt.setString(7, comissao.getFormaPagamento());
            stmt.setInt(8, comissao.getSolicitanteId());
            stmt.setString(9, comissao.getComprovante());
            stmt.setString(10, comissao.getRelatorioComissao());
            stmt.setString(11, comissao.getEmpresa());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Comissao comissao) throws SQLException {
        String sql = """
            UPDATE comissoes
            SET data = ?, tipo = ?, descricao = ?, id_beneficiario = ?, valor = ?, banco = ?,
                forma_pagamento = ?, solicitante_id = ?, comprovante = ?, relatorio = ?, empresa = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(comissao.getData()));
            stmt.setString(2, comissao.getTipo());
            stmt.setString(3, comissao.getDescricao());
            stmt.setInt(4, comissao.getBeneficiarioId());
            stmt.setDouble(5, comissao.getValor());
            stmt.setString(6, comissao.getBanco());
            stmt.setString(7, comissao.getFormaPagamento());
            stmt.setInt(8, comissao.getSolicitanteId());
            stmt.setString(9, comissao.getComprovante());
            stmt.setString(10, comissao.getRelatorioComissao());
            stmt.setString(11, comissao.getEmpresa());
            stmt.setInt(12, comissao.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM comissoes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Comissao> listarTodos() throws SQLException {
        List<Comissao> lista = new ArrayList<>();
        String sql = "SELECT * FROM comissoes";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Comissao c = new Comissao();
                c.setId(rs.getInt("id"));
                c.setData(rs.getDate("data").toLocalDate());
                c.setTipo(rs.getString("tipo"));
                c.setDescricao(rs.getString("descricao"));
                c.setBeneficiarioId(rs.getInt("id_beneficiario"));
                c.setValor(rs.getDouble("valor"));
                c.setBanco(rs.getString("banco"));
                c.setFormaPagamento(rs.getString("forma_pagamento"));
                c.setSolicitanteId(rs.getInt("solicitante_id"));
                c.setComprovante(rs.getString("comprovante"));
                c.setRelatorioComissao(rs.getString("relatorio"));
                c.setEmpresa(rs.getString("empresa"));

                lista.add(c);
            }
        }
        return lista;
    }

    public List<Comissao> buscarPorFiltros(
            LocalDate inicio,
            LocalDate fim,
            String solicitante,
            String beneficiario,
            String empresa
    ) throws SQLException {

        List<Comissao> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM comissoes
            WHERE data BETWEEN ? AND ?
    """);

        List<Object> params = new ArrayList<>();
        params.add(Date.valueOf(inicio));
        params.add(Date.valueOf(fim));

        if (solicitante != null && !"Todas".equalsIgnoreCase(solicitante)) {
            sql.append(" AND solicitante_id = ? ");
            params.add(Integer.parseInt(solicitante));
        }

        if (beneficiario != null && !"Todas".equalsIgnoreCase(beneficiario)) {
            sql.append(" AND id_beneficiario = ? ");
            params.add(Integer.parseInt(beneficiario));
        }

        if (empresa != null && !"Todas".equalsIgnoreCase(empresa)) {
            sql.append(" AND empresa = ? ");
            params.add(empresa);
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) p);
                } else if (p instanceof String) {
                    stmt.setString(i + 1, (String) p);
                } else if (p instanceof Date) {
                    stmt.setDate(i + 1, (Date) p);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comissao c = new Comissao();
                    c.setId(rs.getInt("id"));
                    c.setData(rs.getDate("data").toLocalDate());
                    c.setTipo(rs.getString("tipo"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setBeneficiarioId(rs.getInt("id_beneficiario"));
                    c.setValor(rs.getDouble("valor"));
                    c.setBanco(rs.getString("banco"));
                    c.setFormaPagamento(rs.getString("forma_pagamento"));
                    c.setSolicitanteId(rs.getInt("solicitante_id"));
                    c.setComprovante(rs.getString("comprovante"));
                    c.setRelatorioComissao(rs.getString("relatorio"));
                    c.setEmpresa(rs.getString("empresa"));
                    lista.add(c);
                }
            }
        }

        return lista;
    }

}
