package cargreenlp;

import java.util.*;

public class SistemaEstoque {
    Map<String, Produto> estoque = new HashMap<>();
    List<String> historico = new ArrayList<>();
    List<String> relatorios = new ArrayList<>();

    public void adicionarProduto(Usuario usuario, String nomeProduto, int quantidade) {
        if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("GerenteDeEstoque")) {
            Produto produto = estoque.getOrDefault(nomeProduto, new Produto(nomeProduto, 0));
            produto.quantidade += quantidade;
            estoque.put(nomeProduto, produto);
            historico.add(usuario.nome + " adicionou " + quantidade + " de " + nomeProduto);
            System.out.println("Produto adicionado com sucesso.");
        } else {
            System.out.println("Você não tem permissão para adicionar produtos.");
        }
    }

    public void removerProduto(Usuario usuario, String nomeProduto, int quantidade) {
        if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("Funcionario")) {
            Produto produto = estoque.get(nomeProduto);
            if (produto != null && produto.quantidade >= quantidade) {
                produto.quantidade -= quantidade;
                historico.add(usuario.nome + " removeu " + quantidade + " de " + nomeProduto);
                System.out.println("Produto removido com sucesso.");
            } else {
                System.out.println("Produto inexistente ou quantidade insuficiente.");
            }
        } else {
            System.out.println("Você não tem permissão para remover produtos.");
        }
    }

    public void escreverRelatorio(Usuario usuario, String conteudo) {
        if (usuario.tipo.equals("Supervisor")) {
            relatorios.add("Relatório por " + usuario.nome + ": " + conteudo);
            System.out.println("Relatório enviado com sucesso.");
        } else {
            System.out.println("Você não tem permissão para escrever relatórios.");
        }
    }

    public void exibirHistorico(Usuario usuario) {
        if (!usuario.tipo.equals("Funcionario") && !usuario.tipo.equals("Gerente")) {
            System.out.println("=== Histórico de Modificações ===");
            for (String registro : historico) {
                System.out.println(registro);
            }
        } else {
            System.out.println("Você não tem permissão para ver o histórico.");
        }
    }

    public void exibirEstoque() {
        System.out.println("=== Estoque Atual ===");
        for (Produto p : estoque.values()) {
            System.out.println(p.nome + ": " + p.quantidade);
        }
    }
}
