package cargreenlp;

import java.sql.*;


public class SistemaEstoque {



    public void adicionarProduto(Usuario usuario, String nomeProduto, int quantidade) {
        if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("GerenteDeEstoque")) {
            try (Connection conn = ConexaoDB.conectar()) {
                String sql = "INSERT INTO produtos (nome, quantidade) VALUES (?, ?) " +
                        "ON CONFLICT(nome) DO UPDATE SET quantidade = quantidade + ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nomeProduto);
                    stmt.setInt(2, quantidade);
                    stmt.setInt(3, quantidade);
                    stmt.executeUpdate();
                }

                registrarHistorico(usuario.nome, "Adicionou " + quantidade + " unidades de " + nomeProduto, conn);
                System.out.println("Produto adicionado com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao adicionar produto: " + e.getMessage());
            }
        } else {
            System.out.println("Você não tem permissão para adicionar produtos.");
        }
    }

    public void removerProduto(Usuario usuario, String nomeProduto, int quantidade) {
        if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("Funcionario")) {
            try (Connection conn = ConexaoDB.conectar()) {
                String sql = "UPDATE produtos SET quantidade = quantidade - ? WHERE nome = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, quantidade);
                    stmt.setString(2, nomeProduto);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        registrarHistorico(usuario.nome, "Removeu " + quantidade + " unidades de " + nomeProduto, conn);
                        System.out.println("Produto removido com sucesso.");
                    } else {
                        System.out.println("Produto inexistente ou quantidade insuficiente.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro ao remover produto: " + e.getMessage());
            }
        } else {
            System.out.println("Você não tem permissão para remover produtos.");
        }
    }

    public void escreverRelatorio(Usuario usuario, String conteudo) {
        if (usuario.tipo.equals("Supervisor")) {
            try (Connection conn = ConexaoDB.conectar()) {
                String sql = "INSERT INTO relatorios (usuario_nome, conteudo) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuario.nome);
                    stmt.setString(2, conteudo);
                    stmt.executeUpdate();
                    System.out.println("Relatório enviado com sucesso.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao enviar relatório: " + e.getMessage());
            }
        } else {
            System.out.println("Você não tem permissão para escrever relatórios.");
        }
    }

    public void exibirRelatorios() {
        System.out.println("=== Relatórios ===");
        try (Connection conn = ConexaoDB.conectar()) {
            String sql = "SELECT usuario_nome, conteudo, data FROM relatorios";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Nenhum relatório disponível.");
                    return;
                }
                while (rs.next()) {
                    System.out.println("Relatório por: " + rs.getString("usuario_nome"));
                    System.out.println("Conteúdo: " + rs.getString("conteudo"));
                    System.out.println("Data: " + rs.getTimestamp("data"));
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir relatórios: " + e.getMessage());
        }
    }

    public void exibirHistorico(Usuario usuario) {
        if (!usuario.tipo.equals("Funcionario") && !usuario.tipo.equals("Gerente")) {
            System.out.println("=== Histórico de Modificações ===");
            try (Connection conn = ConexaoDB.conectar()) {
                String sql = "SELECT usuario_nome, acao, data FROM historico";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("Usuário: " + rs.getString("usuario_nome"));
                        System.out.println("Ação: " + rs.getString("acao"));
                        System.out.println("Data: " + rs.getTimestamp("data"));
                        System.out.println("--------------------------");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro ao exibir histórico: " + e.getMessage());
            }
        } else {
            System.out.println("Você não tem permissão para ver o histórico.");
        }
    }


    public void exibirEstoque() {
        System.out.println("=== Estoque Atual ===");
        try (Connection conn = ConexaoDB.conectar()) {
            String sql = "SELECT nome, quantidade FROM produtos";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                boolean temProdutos = false;
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    int quantidade = rs.getInt("quantidade");
                    System.out.println(nome + ": " + quantidade);
                    temProdutos = true;
                }

                if (!temProdutos) {
                    System.out.println("Estoque vazio.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir estoque: " + e.getMessage());
        }
    }


    private void registrarHistorico(String usuarioNome, String acao, Connection conn) {
        try {
            String sql = "INSERT INTO historico (usuario_nome, acao) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuarioNome);
                stmt.setString(2, acao);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao registrar histórico: " + e.getMessage());
        }
    }
}
