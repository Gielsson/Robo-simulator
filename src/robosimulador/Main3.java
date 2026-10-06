package robosimulador;
import java.util.Random;
import java.util.Scanner;

/**
 * Main 3: um robô normal e um inteligente se movem aleatoriamente, um de
 * cada vez, até que OS DOIS encontrem o alimento. No final mostra quantos
 * movimentos cada um fez.
 */
public class Main3 {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Random sorteio = new Random();

        Robo normal = new Robo("Azul");
        RoboInteligente inteligente = new RoboInteligente("Vermelho");
        Robo[] robos = { normal, inteligente };

        System.out.println("=== Jogo do Robô 3: normal (Azul) vs inteligente (Vermelho) ===");
        int xAlimento = lerCoordenada(teclado, "x do alimento");
        int yAlimento = lerCoordenada(teclado, "y do alimento");
        teclado.close();

        Tabuleiro.exibir(robos, xAlimento, yAlimento);

        // Guarda se cada robô já achou (posição 0 = normal, 1 = inteligente).
        boolean[] achou = new boolean[2];
        achou[0] = normal.encontrouAlimento(xAlimento, yAlimento);
        achou[1] = inteligente.encontrouAlimento(xAlimento, yAlimento);

        while (!achou[0] || !achou[1]) {
            for (int i = 0; i < robos.length; i++) {
                if (achou[i]) {
                    continue; // quem já achou fica parado
                }
                int direcao = sorteio.nextInt(4) + 1; // 1 a 4
                try {
                    robos[i].mover(direcao);
                } catch (MovimentoInvalidoException e) {
                    System.out.println("ERRO (" + robos[i].getCor() + "): " + e.getMessage());
                }
                Tabuleiro.exibir(robos, xAlimento, yAlimento);
                Tabuleiro.pausar(500);

                if (robos[i].encontrouAlimento(xAlimento, yAlimento)) {
                    achou[i] = true;
                    System.out.println(">>> O robô " + robos[i].getCor() + " achou o alimento!");
                }
            }
        }

        System.out.println("=== FIM ===");
        for (Robo r : robos) {
            int total = r.getMovimentosValidos() + r.getMovimentosInvalidos();
            System.out.println("Robô " + r.getCor() + " -> " + total + " movimentos ("
                    + r.getMovimentosValidos() + " válidos e "
                    + r.getMovimentosInvalidos() + " inválidos)");
        }
    }

    // Lê uma coordenada de 0 a 3, repetindo até o usuário digitar certo.
    private static int lerCoordenada(Scanner teclado, String nome) {
        while (true) {
            System.out.print("Digite o " + nome + " (0 a " + (Robo.TAMANHO_AREA - 1) + "): ");
            String texto = teclado.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= 0 && valor < Robo.TAMANHO_AREA) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Valor inválido. Tente de novo.");
        }
    }
}