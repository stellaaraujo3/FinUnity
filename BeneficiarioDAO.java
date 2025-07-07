package dao;

import model.Beneficiario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAO {

    private final Connection conn;

    public BeneficiarioDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Beneficiario b) throws SQLException {
        String sql = """
                INSERT INTO beneficiarios
                (nome, cpf, telefone, profissao, empresa, valor_salarial, limite_vale, limite_disponivel, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, b.getNome());
            stmt.setString(2, b.getCpf());
            stmt.setString(3, b.getTelefone());
            stmt.setString(4, b.getProfissao());
            stmt.setString(5, b.getEmpresa());
            stmt.setDouble(6, b.getValorSalarial());
            stmt.setDouble(7, b.getLimiteVale());
            stmt.setDouble(8, b.getLimiteDisponivel());
            stmt.setString(9, b.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    b.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Beneficiario b) throws SQLException {
        String sql = """
                UPDATE beneficiarios
                SET nome = ?, cpf = ?, telefone = ?, profissao = ?, empresa = ?,
                    valor_salarial = ?, limite_vale = ?, limite_disponivel = ?, status = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getNome());
            stmt.setString(2, b.getCpf());
            stmt.setString(3, b.getTelefone());
            stmt.setString(4, b.getProfissao());
            stmt.setString(5, b.getEmpresa());
            stmt.setDouble(6, b.getValorSalarial());
            stmt.setDouble(7, b.getLimiteVale());
            stmt.setDouble(8, b.getLimiteDisponivel());
            stmt.setString(9, b.getStatus());
            stmt.setInt(10, b.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM beneficiarios WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Beneficiario> listarTodos() throws SQLException {
        List<Beneficiario> lista = new ArrayList<>();
        String sql = "SELECT * FROM beneficiarios ORDER BY nome";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Beneficiario b = new Beneficiario();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setCpf(rs.getString("cpf"));
                b.setTelefone(rs.getString("telefone"));
                b.setProfissao(rs.getString("profissao"));
                b.setEmpresa(rs.getString("empresa"));
                b.setValorSalarial(rs.getDouble("valor_salarial"));
                b.setLimiteVale(rs.getDouble("limite_vale"));
                b.setLimiteDisponivel(rs.getDouble("limite_disponivel"));
                b.setStatus(rs.getString("status"));
                lista.add(b);
            }
        }
        return lista;
    }

    public Beneficiario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM beneficiarios WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Beneficiario b = new Beneficiario();
                    b.setId(rs.getInt("id"));
                    b.setNome(rs.getString("nome"));
                    b.setCpf(rs.getString("cpf"));
                    b.setTelefone(rs.getString("telefone"));
                    b.setProfissao(rs.getString("profissao"));
                    b.setEmpresa(rs.getString("empresa"));
                    b.setValorSalarial(rs.getDouble("valor_salarial"));
                    b.setLimiteVale(rs.getDouble("limite_vale"));
                    b.setLimiteDisponivel(rs.getDouble("limite_disponivel"));
                    b.setStatus(rs.getString("status"));
                    return b;
                }
            }
        }
        return null;
    }
}
