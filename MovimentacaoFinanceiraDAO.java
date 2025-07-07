package dao;

import model.MovimentacaoFinanceira;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoFinanceiraDAO {

    private final Connection conn;

    public MovimentacaoFinanceiraDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(MovimentacaoFinanceira mov) throws SQLException {
        String sql = """
            INSERT INTO movimentacoes_financeiras
            (data, tipo, descricao, empresa, valor, banco, forma_pagamento, solicitante_id, comprovante, nota_fiscal)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mov.getData()));
            stmt.setString(2, mov.getTipo());
            stmt.setString(3, mov.getDescricao());
            stmt.setString(4, mov.getEmpresa());
            stmt.setDouble(5, mov.getValor());
            stmt.setString(6, mov.getBanco());
            stmt.setString(7, mov.getFormaPagamento());
            stmt.setInt(8, mov.getSolicitanteId());
            stmt.setString(9, mov.getComprovante());
            stmt.setString(10, mov.getNotaFiscal());
            stmt.executeUpdate();
        }
    }

    public List<MovimentacaoFinanceira> listarTodos() throws SQLException {
        List<MovimentacaoFinanceira> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes_financeiras";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MovimentacaoFinanceira m = new MovimentacaoFinanceira();
                m.setId(rs.getInt("id"));
                m.setData(rs.getDate("data").toLocalDate());
                m.setTipo(rs.getString("tipo"));
                m.setDescricao(rs.getString("descricao"));
                m.setEmpresa(rs.getString("empresa"));
                m.setValor(rs.getDouble("valor"));
                m.setBanco(rs.getString("banco"));
                m.setFormaPagamento(rs.getString("forma_pagamento"));
                m.setSolicitanteId(rs.getInt("solicitante_id"));
                m.setComprovante(rs.getString("comprovante"));
                m.setNotaFiscal(rs.getString("nota_fiscal"));
                lista.add(m);
            }
        }
        return lista;
    }

    public void atualizar(MovimentacaoFinanceira mov) throws SQLException {
        String sql = """
            UPDATE movimentacoes_financeiras
            SET data = ?, tipo = ?, descricao = ?, empresa = ?, valor = ?,
                banco = ?, forma_pagamento = ?, solicitante_id = ?, comprovante = ?, nota_fiscal = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mov.getData()));
            stmt.setString(2, mov.getTipo());
            stmt.setString(3, mov.getDescricao());
            stmt.setString(4, mov.getEmpresa());
            stmt.setDouble(5, mov.getValor());
            stmt.setString(6, mov.getBanco());
            stmt.setString(7, mov.getFormaPagamento());
            stmt.setInt(8, mov.getSolicitanteId());
            stmt.setString(9, mov.getComprovante());
            stmt.setString(10, mov.getNotaFiscal());
            stmt.setInt(11, mov.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM movimentacoes_financeiras WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<MovimentacaoFinanceira> buscarPorFiltros(
            LocalDate inicio,
            LocalDate fim,
            String solicitante,
            String empresa,
            String tipo
    ) throws SQLException {

        List<MovimentacaoFinanceira> lista = new ArrayList<>();

        String sql = """
        SELECT *
        FROM movimentacoes_financeiras v
        JOIN solicitantes s ON s.id = v.solicitante_id
        WHERE v.data BETWEEN ? AND ?
          AND (? IS NULL OR s.nome = ?)
          AND (? IS NULL OR v.empresa = ?)
          AND (? IS NULL OR v.tipo = ?)
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));

            // Solicitante
            if (solicitante == null || "Todas".equalsIgnoreCase(solicitante)) {
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.VARCHAR);
            } else {
                stmt.setString(3, solicitante);
                stmt.setString(4, solicitante);
            }

            // Empresa
            if (empresa == null || "Todas".equalsIgnoreCase(empresa)) {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
            } else {
                stmt.setString(5, empresa);
                stmt.setString(6, empresa);
            }

            // Tipo
            if (tipo == null || "Todas".equalsIgnoreCase(tipo)) {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            } else {
                stmt.setString(7, tipo);
                stmt.setString(8, tipo);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimentacaoFinanceira v = new MovimentacaoFinanceira();
                    v.setId(rs.getInt("id"));
                    v.setData(rs.getDate("data").toLocalDate());
                    v.setTipo(rs.getString("tipo"));
                    v.setDescricao(rs.getString("descricao"));
                    v.setEmpresa(rs.getString("empresa"));
                    v.setValor(rs.getDouble("valor"));
                    v.setBanco(rs.getString("banco"));
                    v.setFormaPagamento(rs.getString("forma_pagamento"));
                    v.setSolicitanteId(rs.getInt("solicitante_id"));
                    v.setComprovante(rs.getString("comprovante"));
                    v.setNotaFiscal(rs.getString("nota_fiscal"));
                    lista.add(v);
                }
            }
        }
        return lista;
    }






}

