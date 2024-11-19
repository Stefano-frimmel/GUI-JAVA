import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

// Classe para gerenciar a conexão com o banco de dados
class DatabaseConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:./database.db");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro de conexão: " + e.getMessage());
        }
        return conn;
    }
}

// Interface para padronizar os métodos CRUD
interface CrudInterface {
    void create(String name);
    void read();
    void update(int id, String name);
    void delete(int id);
}

// Classe que implementa os métodos CRUD
class UserDAO implements CrudInterface {
    @Override
    public void create(String name) {
        String sql = "INSERT INTO users(name) VALUES(?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário inserido com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir: " + e.getMessage());
        }
    }

    @Override
    public void read() {
        String sql = "SELECT id, name FROM users";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            StringBuilder result = new StringBuilder("ID\tNome\n");
            while (rs.next()) {
                result.append(rs.getInt("id")).append("\t").append(rs.getString("name")).append("\n");
            }
            JOptionPane.showMessageDialog(null, result.toString(), "Lista de Usuários", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar: " + e.getMessage());
        }
    }

    @Override
    public void update(int id, String name) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário excluído com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + e.getMessage());
        }
    }
}

// Classe principal que gerencia a interação com o usuário
public class UserView {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        String menu = "1. Inserir Usuário\n2. Listar Usuários\n3. Atualizar Usuário\n4. Excluir Usuário\n5. Sair";

        while (true) {
            String choice = JOptionPane.showInputDialog(menu);
            if (choice == null || choice.equals("5")) break;

            switch (choice) {
                case "1":
                    String name = JOptionPane.showInputDialog("Digite o nome do usuário:");
                    if (name != null) userDAO.create(name);
                    break;
                case "2":
                    userDAO.read();
                    break;
                case "3":
                    try {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do usuário:"));
                        String newName = JOptionPane.showInputDialog("Digite o novo nome:");
                        if (newName != null) userDAO.update(id, newName);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido!");
                    }
                    break;
                case "4":
                    try {
                        int deleteId = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do usuário para exclusão:"));
                        userDAO.delete(deleteId);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido!");
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }
}
