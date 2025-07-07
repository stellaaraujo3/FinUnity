package dao;

import model.SalarioFerias;
import model.Solicitante;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitanteDAO {

    private final Connection conn;

    public SolicitanteDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Solicitante solicitante) throws SQLException {
        String sql = """
            INSERT INTO solicitantes (nome, setor, telefone)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, solicitante.getNome());
            stmt.setString(2, solicitante.getSetor());
            stmt.setString(3, solicitante.getTelefone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                solicitante.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Solicitante solicitante) throws SQLException {
        String sql = """
            UPDATE solicitantes
            SET nome = ?, setor = ?, telefone = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, solicitante.getNome());
            stmt.setString(2, solicitante.getSetor());
            stmt.setString(3, solicitante.getTelefone());
            stmt.setInt(4, solicitante.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM solicitantes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Solicitante> listarTodos() throws SQLException {
        List<Solicitante> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitantes";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Solicitante s = new Solicitante();
                s.setId(rs.getInt("id"));
                s.setNome(rs.getString("nome"));
                s.setSetor(rs.getString("setor"));
                s.setTelefone(rs.getString("telefone"));
                lista.add(s);
            }
        }
        return lista;
    }


}
