package robosimulador;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main 4: jogo completo com obstáculos. O usuário escolhe o alimento e
 * coloca bombas e rochas. Um robô normal e um inteligente andam
 * aleatoriamente até que UM ache o alimento ou os DOIS explodam.
 */
public class Main4 {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Random sorteio = new Random();

        Robo normal = new Robo("Azul");
        RoboInteligente inteligente = new RoboInteligente("Vermelho");
        Robo[] robos = { normal, inteligente };
        ArrayList<Obstaculo> obstaculos = new ArrayList<>();
        int proximoId = 1;

        System.out.println("=== Jogo do Robô 4: com bombas e rochas ===");
        int xAlimento = lerInteiro(teclado, "Digite o x do alimento (0 a 3): ", 0, 3);
        int yAlimento = lerInteiro(teclado, "Digite o y do alimento (0 a 3): ", 0, 3);

        // Coloca as bombas
        int qtdBombas = lerInteiro(teclado, "Quantas bombas? (0 a 5): ", 0, 5);
        for (int i = 1; i <= qtdBombas; i++) {
            int[] pos = lerPosicaoLivre(teclado, "da bomba " + i, xAlimento, yAlimento, obstaculos);
            obstaculos.add(new Bomba(proximoId, pos[0], pos[1]));
            proximoId++;
        }

        // Coloca as rochas
        int qtdRochas = lerInteiro(teclado, "Quantas rochas? (0 a 5): ", 0, 5);
        for (int i = 1; i <= qtdRochas; i++) {
            int[] pos = lerPosicaoLivre(teclado, "da rocha " + i, xAlimento, yAlimento, obstaculos);
            obstaculos.add(new Rocha(proximoId, pos[0], pos[1]));
            proximoId++;
        }
        teclado.close();

        Tabuleiro.exibir(robos, xAlimento, yAlimento, obstaculos);

        if (xAlimento == 0 && yAlimento == 0) {
            System.out.println("O alimento está na posição inicial: os robôs já o encontraram!");
            return;
        }

        Robo vencedor = null;
        boolean todosExplodiram = false;

        while (vencedor == null && !todosExplodiram) {
            for (Robo atual : robos) {
                if (atual.isExplodido()) {
                    continue; // robô explodido não anda mais
                }

                int direcao = sorteio.nextInt(4) + 1;
                try {
                    atual.mover(direcao);
                } catch (MovimentoInvalidoException e) {
                    System.out.println("ERRO (" + atual.getCor() + "): " + e.getMessage());
                }

                // Depois de andar, vê se tem obstáculo na casa.
                Obstaculo o = Tabuleiro.obstaculoNaPosicao(obstaculos, atual.getX(), atual.getY());
                if (o != null) {
                    o.bater(atual);
                }

                Tabuleiro.exibir(robos, xAlimento, yAlimento, obstaculos);
                Tabuleiro.pausar(500);

                if (atual.encontrouAlimento(xAlimento, yAlimento)) {
                    vencedor = atual;
                    break; // acabou o jogo
                }
            }
            todosExplodiram = normal.isExplodido() && inteligente.isExplodido();
        }

        System.out.println("=== FIM ===");
        if (vencedor != null) {
            System.out.println("O robô " + vencedor.getCor() + " achou o alimento!");
        } else {
            System.out.println("Os dois robôs explodiram!");
        }
        for (Robo r : robos) {
            int total = r.getMovimentosValidos() + r.getMovimentosInvalidos();
            String situacao = r.isExplodido() ? " (explodiu)" : "";
            System.out.println("Robô " + r.getCor() + " -> " + total + " movimentos ("
                    + r.getMovimentosValidos() + " válidos e "
                    + r.getMovimentosInvalidos() + " inválidos)" + situacao);
        }
    }

    // Lê um número inteiro entre min e max, repetindo até digitar certo.
    private static int lerInteiro(Scanner teclado, String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String texto = teclado.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Valor inválido. Tente de novo.");
        }
    }

    // Lê x e y de um obstáculo; não deixa ser em (0,0), no alimento ou em outro obstáculo.
    private static int[] lerPosicaoLivre(Scanner teclado, String nome, int xAlimento,
                                         int yAlimento, ArrayList<Obstaculo> obstaculos) {
        while (true) {
            int x = lerInteiro(teclado, "x " + nome + " (0 a 3): ", 0, 3);
            int y = lerInteiro(teclado, "y " + nome + " (0 a 3): ", 0, 3);

            if (x == 0 && y == 0) {
                System.out.println("(0,0) é a posição inicial dos robôs. Escolha outra.");
            } else if (x == xAlimento && y == yAlimento) {
                System.out.println("Essa é a posição do alimento. Escolha outra.");
            } else if (Tabuleiro.obstaculoNaPosicao(obstaculos, x, y) != null) {
                System.out.println("Já existe um obstáculo aí. Escolha outra.");
            } else {
                return new int[] { x, y };
            }
        }
    }
}