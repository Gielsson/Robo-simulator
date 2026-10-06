package robosimulador;
import java.util.ArrayList;
import java.util.Random;

/**
 * Robô inteligente: se um movimento deu errado, ele NÃO repete esse mesmo
 * movimento no próximo passo. Ele guarda as direções que falharam
 * (desde o último movimento válido) e sorteia outra direção.
 */
public class RoboInteligente extends Robo {

    // Direções que deram erro desde o último movimento válido.
    private ArrayList<String> direcoesInvalidas = new ArrayList<>();
    private Random sorteio = new Random();

    public RoboInteligente(String cor) {
        super(cor);
    }

    /** Move pela direção em texto, mas troca a direção se ela já falhou. */
    @Override
    public void mover(String direcao) throws MovimentoInvalidoException {
        String dir = (direcao == null) ? "" : direcao.trim().toLowerCase();

        // Se essa direção já deu erro, escolhe outra no lugar.
        if (direcoesInvalidas.contains(dir)) {
            dir = sortearOutraDirecao();
        }

        try {
            executarMovimento(dir);
            direcoesInvalidas.clear(); // deu certo: esquece os erros antigos
        } catch (MovimentoInvalidoException e) {
            direcoesInvalidas.add(dir); // guarda o erro e avisa quem chamou
            throw e;
        }
    }

    /** Versão com número (1 a 4): converte e usa a versão em texto acima. */
    @Override
    public void mover(int direcao) throws MovimentoInvalidoException {
        mover(codigoParaDirecao(direcao));
    }

    // Sorteia uma direção que ainda não falhou.
    private String sortearOutraDirecao() {
        String[] todas = { "up", "down", "right", "left" };
        ArrayList<String> opcoes = new ArrayList<>();
        for (String d : todas) {
            if (!direcoesInvalidas.contains(d)) {
                opcoes.add(d);
            }
        }
        return opcoes.get(sorteio.nextInt(opcoes.size()));
    }
}