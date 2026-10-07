package robosimulador;
import java.util.Random;
import java.util.Scanner;
 
/**
 * Main 2: dois robôs se movem aleatoriamente, um de cada vez, até que
 * um deles encontre o alimento. No final mostra quem achou e quantos
 * movimentos válidos e inválidos cada um fez.
 */
public class Main2 {
 
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Random sorteio = new Random();
 
        Robo[] robos = { new Robo("Azul"), new Robo("Vermelho") };
 
        System.out.println("=== Jogo do Robô 2: disputa pelo alimento ===");
        int xAlimento = lerCoordenada(teclado, "x do alimento");
        int yAlimento = lerCoordenada(teclado, "y do alimento");
        teclado.close();
 
        exibirMatriz(robos, xAlimento, yAlimento);
 
        // Caso raro: o alimento está na posição inicial (0,0).
        if (xAlimento == 0 && yAlimento == 0) {
            System.out.println("O alimento está na posição inicial: os dois já o encontraram!");
            return;
        }
 
        Robo vencedor = null;
        int vez = 0; // alterna entre 0 e 1
 
        while (vencedor == null) {
            Robo atual = robos[vez];
            int direcao = sorteio.nextInt(4) + 1; // sorteia de 1 a 4
 
            try {
                atual.mover(direcao);
            } catch (MovimentoInvalidoException e) {
                System.out.println("ERRO: " + e.getMessage());
            }
 
            exibirMatriz(robos, xAlimento, yAlimento);
            pausar(600); // para dar tempo de ver os robôs se movendo
 
            if (atual.encontrouAlimento(xAlimento, yAlimento)) {
                vencedor = atual;
            }
            vez = 1 - vez; // passa a vez para o outro robô
        }
 
        System.out.println("=== FIM ===");
        System.out.println("O robô " + vencedor.getCor() + " achou o alimento!");
        for (Robo r : robos) {
            System.out.println("Robô " + r.getCor()
                    + " -> movimentos válidos: " + r.getMovimentosValidos()
                    + " | inválidos: " + r.getMovimentosInvalidos());
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
 
    private static void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
 
    /**
     * Exibição simples da matriz 4x4: inicial da cor = robô, '*' = alimento.
     * Se dois robôs estiverem na mesma célula, aparecem as duas iniciais.
     * (Pode ser trocada pelo método de exibição da Pessoa 2.)
     */
    private static void exibirMatriz(Robo[] robos, int xAlimento, int yAlimento) {
        System.out.println();
        for (int y = Robo.TAMANHO_AREA - 1; y >= 0; y--) { // y maior fica no topo
            StringBuilder linha = new StringBuilder();
            for (int x = 0; x < Robo.TAMANHO_AREA; x++) {
                StringBuilder celula = new StringBuilder();
                for (Robo r : robos) {
                    if (r.getX() == x && r.getY() == y) {
                        celula.append(r.getCor().substring(0, 1).toUpperCase());
                    }
                }
                if (celula.length() == 0) {
                    celula.append(xAlimento == x && yAlimento == y ? "*" : ".");
                }
                linha.append(String.format("[ %-2s]", celula));
            }
            System.out.println(linha);
        }
        System.out.println();
    }
}