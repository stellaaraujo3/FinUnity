package dao;

import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Autentica um usuário pelo login e senha.
     * Retorna o objeto Usuario se válido, ou null se inválido.
     */
    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = """
            SELECT * FROM usuarios
            WHERE login = ? AND senha = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setNome(rs.getString("nome"));
                u.setTipo(rs.getString("tipo"));
                return u;
            }
        }

        return null;
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setNome(rs.getString("nome"));
                u.setTipo(rs.getString("tipo"));
                lista.add(u);
            }
        }

        return lista;
    }

    /**
     * Insere um novo usuário.
     */
    public void inserir(Usuario usuario) throws SQLException {
        String sql = """
            INSERT INTO usuarios (login, senha, nome, tipo)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getTipo());
            stmt.executeUpdate();
        }
    }

    /**
     * Atualiza um usuário existente.
     */
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = """
            UPDATE usuarios
            SET login = ?, senha = ?, nome = ?, tipo = ?
            WHERE id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getTipo());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Exclui um usuário pelo ID.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
