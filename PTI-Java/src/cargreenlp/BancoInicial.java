package cargreenlp;

import java.sql.Connection;
import java.sql.Statement;

public class BancoInicial {
    public static void criarTabelas() {
        String sqlUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                codigo TEXT PRIMARY KEY,
                nome TEXT NOT NULL,
                tipo TEXT NOT NULL
            );
        """;

        String sqlProdutos = """
            CREATE TABLE IF NOT EXISTS produtos (
                nome TEXT PRIMARY KEY,
                quantidade INTEGER NOT NULL
            );
        """;

        String sqlHistorico = """
            CREATE TABLE IF NOT EXISTS historico (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descricao TEXT NOT NULL,
                data TEXT DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String sqlRelatorios = """
                CREATE TABLE IF NOT EXISTS relatorios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_nome TEXT NOT NULL,
                conteudo TEXT NOT NULL,
                data TEXT DEFAULT CURRENT_TIMESTAMP
            );
        """;

        try (Connection conn = ConexaoDB.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProdutos);
            stmt.execute(sqlHistorico);
            stmt.execute(sqlRelatorios);

            System.out.println("Tabelas criadas com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}
