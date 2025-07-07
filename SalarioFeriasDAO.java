package dao;

import model.SalarioFerias;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalarioFeriasDAO {

    private final Connection conn;

    public SalarioFeriasDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(SalarioFerias sf) throws SQLException {
        String sql = """
            INSERT INTO salarios_ferias
                (data, solicitante_id, empresa, valor, tipo, banco, forma_pagamento, comprovante)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(sf.getData()));
            stmt.setInt(2, sf.getSolicitanteId());
            stmt.setString(3, sf.getEmpresa());
            stmt.setDouble(4, sf.getValor());
            stmt.setString(5, sf.getTipo());
            stmt.setString(6, sf.getBanco());
            stmt.setString(7, sf.getFormaPagamento());
            stmt.setString(8, sf.getComprovante());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sf.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(SalarioFerias sf) throws SQLException {
        String sql = """
            UPDATE salarios_ferias
            SET data = ?, solicitante_id = ?, empresa = ?, valor = ?,
                tipo = ?, banco = ?, forma_pagamento = ?, comprovante = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(sf.getData()));
            stmt.setInt(2, sf.getSolicitanteId());
            stmt.setString(3, sf.getEmpresa());
            stmt.setDouble(4, sf.getValor());
            stmt.setString(5, sf.getTipo());
            stmt.setString(6, sf.getBanco());
            stmt.setString(7, sf.getFormaPagamento());
            stmt.setString(8, sf.getComprovante());
            stmt.setInt(9, sf.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM salarios_ferias WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<SalarioFerias> listarTodos() throws SQLException {
        List<SalarioFerias> lista = new ArrayList<>();
        String sql = "SELECT * FROM salarios_ferias";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SalarioFerias sf = new SalarioFerias();
                sf.setId(rs.getInt("id"));
                sf.setData(rs.getDate("data").toLocalDate());
                sf.setSolicitanteId(rs.getInt("solicitante_id"));
                sf.setEmpresa(rs.getString("empresa"));
                sf.setValor(rs.getDouble("valor"));
                sf.setTipo(rs.getString("tipo"));
                sf.setBanco(rs.getString("banco"));
                sf.setFormaPagamento(rs.getString("forma_pagamento"));
                sf.setComprovante(rs.getString("comprovante"));

                lista.add(sf);
            }
        }
        return lista;
    }

    public SalarioFerias buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM salarios_ferias WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SalarioFerias sf = new SalarioFerias();
                    sf.setId(rs.getInt("id"));
                    sf.setData(rs.getDate("data").toLocalDate());
                    sf.setSolicitanteId(rs.getInt("solicitante_id"));
                    sf.setEmpresa(rs.getString("empresa"));
                    sf.setValor(rs.getDouble("valor"));
                    sf.setTipo(rs.getString("tipo"));
                    sf.setBanco(rs.getString("banco"));
                    sf.setFormaPagamento(rs.getString("forma_pagamento"));
                    sf.setComprovante(rs.getString("comprovante"));

                    return sf;
                }
            }
        }
        return null;
    }

    public List<SalarioFerias> buscarPorFiltros(
            LocalDate inicio,
            LocalDate fim,
            String solicitante,
            String empresa
    ) throws SQLException {

        List<SalarioFerias> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM salarios_ferias
            WHERE data BETWEEN ? AND ?
    """);

        List<Object> params = new ArrayList<>();
        params.add(Date.valueOf(inicio));
        params.add(Date.valueOf(fim));

        if (solicitante != null && !"Todas".equalsIgnoreCase(solicitante)) {
            sql.append(" AND solicitante_id = ? ");
            params.add(Integer.parseInt(solicitante));
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
                    SalarioFerias sf = new SalarioFerias();
                    sf.setId(rs.getInt("id"));
                    sf.setData(rs.getDate("data").toLocalDate());
                    sf.setSolicitanteId(rs.getInt("solicitante_id"));
                    sf.setEmpresa(rs.getString("empresa"));
                    sf.setValor(rs.getDouble("valor"));
                    sf.setBanco(rs.getString("banco"));
                    sf.setTipo(rs.getString("tipo"));
                    sf.setComprovante(rs.getString("comprovante"));
                    sf.setFormaPagamento(rs.getString("forma_pagamento"));
                    lista.add(sf);
                }
            }
        }

        return lista;
    }


}
