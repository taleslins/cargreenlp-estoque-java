package cargreenlp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {
    public static Connection conectar() {
        Connection conn = null;
        try {

            String url = "jdbc:sqlite:estoque.db";
            conn = DriverManager.getConnection(url);


            String sql = "CREATE TABLE IF NOT EXISTS historico ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "usuario_nome TEXT, "
                    + "acao TEXT, "
                    + "data TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        return conn;
    }
}
