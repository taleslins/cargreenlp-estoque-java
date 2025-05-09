package cargreenlp;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        BancoInicial.criarTabelas();
        Scanner sc = new Scanner(System.in);
        SistemaEstoque sistema = new SistemaEstoque();
        Map<String, Usuario> usuarios = Map.of(
                "22d41", new Usuario("Roberto", "22d41", "Funcionario"),
                "a32a2", new Usuario("Lucia", "a32a2", "Supervisor"),
                "gh312", new Usuario("Carlos", "gh312", "GerenteDeEstoque"),
                "lpo99", new Usuario("Patrícia", "lpo99", "Administrador")
        );

        System.out.print("Digite seu código de acesso: ");
        String codigo = sc.nextLine();

        Usuario usuario = usuarios.get(codigo);
        if (usuario == null) {
            System.out.println("Código inválido.");
            return;
        }

        System.out.println("Bem-vindo, " + usuario.nome + " (" + usuario.tipo + ")");

        boolean rodando = true;
        while (rodando) {
            System.out.println("\nEscolha uma ação:");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Remover Produto");
            System.out.println("3. Escrever Relatório");
            System.out.println("4. Ver Estoque");
            System.out.println("5. Ver Histórico");

            if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("Administrador")) {
                System.out.println("6. Ver Relatórios");
            }
            System.out.println("0. Sair");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome do produto: ");
                    String nomeAdd = sc.nextLine();
                    System.out.print("Quantidade: ");
                    int qtdAdd = sc.nextInt();
                    sc.nextLine();
                    sistema.adicionarProduto(usuario, nomeAdd, qtdAdd);
                    break;
                case 2:
                    System.out.print("Nome do produto: ");
                    String nomeRem = sc.nextLine();
                    System.out.print("Quantidade: ");
                    int qtdRem = sc.nextInt();
                    sc.nextLine();
                    sistema.removerProduto(usuario, nomeRem, qtdRem);
                    break;
                case 3:
                    System.out.print("Escreva seu relatório: ");
                    String conteudo = sc.nextLine();
                    sistema.escreverRelatorio(usuario, conteudo);
                    break;
                case 4:
                    sistema.exibirEstoque();
                    break;
                case 5:
                    sistema.exibirHistorico(usuario);
                    break;
                case 6:
                    if (usuario.tipo.equals("Supervisor") || usuario.tipo.equals("Administrador")) {
                        sistema.exibirRelatorios();
                    } else {
                        System.out.println("Acesso negado. Esta opção é restrita.");
                    }
                    break;
                case 0:
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        sc.close();
    }
}
