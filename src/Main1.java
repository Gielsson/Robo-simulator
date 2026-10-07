
package robosimulador;
import java.util.Scanner;
 
/**
 * Main 1: o usuário define a posição do alimento e move o robô
 * manualmente até encontrá-lo. A exceção é tratada, então o programa
 * não fecha ao bater na "parede".
 */
public class Main1 {
 
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Robo robo = new Robo("Azul");
 
        System.out.println("=== Jogo do Robô 1: mova o robô até a comida ===");
        System.out.println("Área de " + Robo.TAMANHO_AREA + "x" + Robo.TAMANHO_AREA
                + " (coordenadas de 0 a " + (Robo.TAMANHO_AREA - 1) + ").");
 
        int xAlimento = lerCoordenada(teclado, "x do alimento");
        int yAlimento = lerCoordenada(teclado, "y do alimento");
 
        exibirMatriz(robo, xAlimento, yAlimento);
 
        while (!robo.encontrouAlimento(xAlimento, yAlimento)) {
            System.out.print("Movimento (up/down/right/left ou 1=up 2=down 3=right 4=left): ");
            String entrada = teclado.nextLine().trim();
 
            try {
                // Se digitou um número, usa mover(int); senão, mover(String).
                if (entrada.matches("-?\\d+")) {
                    robo.mover(Integer.parseInt(entrada));
                } else {
                    robo.mover(entrada);
                }
            } catch (MovimentoInvalidoException e) {
                // O robô não se mexeu; mostramos o erro e seguimos o jogo.
                System.out.println("ERRO: " + e.getMessage());
            }
 
            exibirMatriz(robo, xAlimento, yAlimento);
        }
 
        System.out.println("O robô " + robo.getCor() + " encontrou o alimento!");
        System.out.println("Movimentos válidos: " + robo.getMovimentosValidos());
        System.out.println("Movimentos inválidos: " + robo.getMovimentosInvalidos());
        teclado.close();
    }
 
    /** Lê uma coordenada de 0 a 3, repetindo até o usuário digitar certo. */
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
 
    /**
     * Exibição simples da matriz 4x4: a inicial da cor é o robô e '*' é
     * o alimento. (Pode ser trocada pelo método de exibição da Pessoa 2.)
     */
    private static void exibirMatriz(Robo robo, int xAlimento, int yAlimento) {
        System.out.println();
        for (int y = Robo.TAMANHO_AREA - 1; y >= 0; y--) { // y maior fica no topo
            StringBuilder linha = new StringBuilder();
            for (int x = 0; x < Robo.TAMANHO_AREA; x++) {
                String celula = ".";
                if (robo.getX() == x && robo.getY() == y) {
                    celula = robo.getCor().substring(0, 1).toUpperCase();
                } else if (xAlimento == x && yAlimento == y) {
                    celula = "*";
                }
                linha.append("[ ").append(celula).append(" ]");
            }
            System.out.println(linha);
        }
        System.out.println();
    }
}
 