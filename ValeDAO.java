package dao;

import model.Vale;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ValeDAO {

    private final Connection conn;

    public ValeDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Vale vale) throws SQLException {
        String sql = """
            INSERT INTO vales
                (data, beneficiario_id, empresa, forma_desconto, valor,
                 banco, forma_pagamento, solicitante_id, anexo_comprovante,
                 anexo_recibo, situacao, qtd_parcelas)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(vale.getData()));
            stmt.setInt(2, vale.getBeneficiarioId());
            stmt.setString(3, vale.getEmpresa());
            stmt.setString(4, vale.getFormaDesconto());
            stmt.setDouble(5, vale.getValor());
            stmt.setString(6, vale.getBanco());
            stmt.setString(7, vale.getFormaPagamento());
            stmt.setInt(8, vale.getSolicitanteId());
            stmt.setString(9, vale.getComprovante());
            stmt.setString(10, vale.getReciboAssinado());
            stmt.setString(11, vale.getSituacao());

            if (vale.getQtdParcelas() != null) {
                stmt.setInt(12, vale.getQtdParcelas());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }

            stmt.executeUpdate();
        }
    }

    public void atualizar(Vale vale) throws SQLException {
        String sql = """
            UPDATE vales
            SET data = ?, beneficiario_id = ?, empresa = ?, forma_desconto = ?, valor = ?,
                banco = ?, forma_pagamento = ?, solicitante_id = ?, anexo_comprovante = ?,
                anexo_recibo = ?, situacao = ?, qtd_parcelas = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(vale.getData()));
            stmt.setInt(2, vale.getBeneficiarioId());
            stmt.setString(3, vale.getEmpresa());
            stmt.setString(4, vale.getFormaDesconto());
            stmt.setDouble(5, vale.getValor());
            stmt.setString(6, vale.getBanco());
            stmt.setString(7, vale.getFormaPagamento());
            stmt.setInt(8, vale.getSolicitanteId());
            stmt.setString(9, vale.getComprovante());
            stmt.setString(10, vale.getReciboAssinado());
            stmt.setString(11, vale.getSituacao());

            if (vale.getQtdParcelas() != null) {
                stmt.setInt(12, vale.getQtdParcelas());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }

            stmt.setInt(13, vale.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM vales WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Vale> listarTodos() throws SQLException {
        List<Vale> lista = new ArrayList<>();
        String sql = "SELECT * FROM vales";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vale v = new Vale();
                v.setId(rs.getInt("id"));
                v.setData(rs.getDate("data").toLocalDate());
                v.setBeneficiarioId(rs.getInt("beneficiario_id"));
                v.setEmpresa(rs.getString("empresa"));
                v.setFormaDesconto(rs.getString("forma_desconto"));
                v.setValor(rs.getDouble("valor"));
                v.setBanco(rs.getString("banco"));
                v.setFormaPagamento(rs.getString("forma_pagamento"));
                v.setSolicitanteId(rs.getInt("solicitante_id"));
                v.setComprovante(rs.getString("anexo_comprovante"));
                v.setReciboAssinado(rs.getString("anexo_recibo"));
                v.setSituacao(rs.getString("situacao"));

                Integer qtdParcelas = rs.getObject("qtd_parcelas", Integer.class);
                v.setQtdParcelas(qtdParcelas);

                lista.add(v);
            }
        }
        return lista;
    }

    public List<Vale> buscarPorFiltros(
            LocalDate inicio,
            LocalDate fim,
            String solicitante,
            String beneficiario,
            String empresa
    ) throws SQLException {

        List<Vale> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM vales
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
            sql.append(" AND beneficiario_id = ? ");
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
                    Vale v = new Vale();
                    v.setId(rs.getInt("id"));
                    v.setData(rs.getDate("data").toLocalDate());
                    v.setBeneficiarioId(rs.getInt("beneficiario_id"));
                    v.setEmpresa(rs.getString("empresa"));
                    v.setFormaDesconto(rs.getString("forma_desconto"));
                    v.setValor(rs.getDouble("valor"));
                    v.setBanco(rs.getString("banco"));
                    v.setFormaPagamento(rs.getString("forma_pagamento"));
                    v.setSolicitanteId(rs.getInt("solicitante_id"));
                    v.setComprovante(rs.getString("anexo_comprovante"));
                    v.setReciboAssinado(rs.getString("anexo_recibo"));
                    v.setSituacao(rs.getString("situacao"));
                    v.setQtdParcelas(rs.getObject("qtd_parcelas", Integer.class));
                    lista.add(v);
                }
            }
        }

        return lista;
    }






    public double obterTotalValesEmAbertoPorBeneficiario(int beneficiarioId) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(valor), 0) as total
            FROM vales
            WHERE beneficiario_id = ? AND situacao = 'EM ABERTO'
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, beneficiarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }

}
