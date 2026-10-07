package robosimulador;
import java.util.ArrayList;
import java.util.Random;
// Robô inteligente: se um movimento deu errado, ele NÃO repete esse mesmo
 // movimento no próximo passo. Ele guarda as direções que falharam
 //(desde o último movimento válido) e sorteia outra direção.
public class RoboInteligente extends Robo {

   //uma lista que memoriza quais as direções que falharam desde o ultimo movimento
    private ArrayList<String> direcoesInvalidas = new ArrayList<>();
    
    private Random sorteio = new Random();

    public RoboInteligente(String cor) {
        super(cor);
    }

//começa por limpar o texto recebido, removendo espaços em branco
    /** Move pela direção em texto, mas troca a direção se ela já falhou. */
    public void mover(String direcao) throws MovimentoInvalidoException {
        //throws serve para avisar que esse metodo pode encontrar um movimento invalido
        String dir = (direcao == null) ? "" : direcao.trim().toLowerCase(); 
        // trim()retira espaços e deixa td minusculo
        //? forma de escrever if/else

        // Se essa direção já deu erro, escolhe outra no lugar.
        if (direcoesInvalidas.contains(dir)) {
            dir = sortearOutraDirecao();
            //se deu erro, ele guarda essa informação na lista direcoesInvalidas 
            //e obtem uma alternativa atravez da sortearOutraDirecao()
        }

        try {
            executarMovimento(dir);
            direcoesInvalidas.clear(); // deu certo: reinicia o historico de falhas
        } catch (MovimentoInvalidoException e) {
            //quando o robo tentaa realizar uma ação que n eh permitida, o codigo lança throw para indicar q o mov falhou  
            direcoesInvalidas.add(dir); // guarda o erro e avisa quem chamou
            throw e; //lança novamente a excessao
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
        //escolhe de forma totalmente aleatoria uma das direções seguras que sobram
        //na lista de opções
    }
}
