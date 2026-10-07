package robosimulador;

import java.util.ArrayList;

 // Mostra a matriz 4x4 no console e guarda utilidades usadas pelas Mains.
 //Legenda: letra = robô (inicial da cor), * = alimento, B = bomba, R = rocha.

public class Tabuleiro {

 //Mostra a matriz sem obstáculos (serve para Main1, Main2 e Main3).
    public static void exibir(Robo[] robos, int xAlimento, int yAlimento) {
        exibir(robos, xAlimento, yAlimento, new ArrayList<Obstaculo>());
    }

    /** Mostra a matriz com robôs, alimento e obstáculos. */
    public static void exibir(Robo[] robos, int xAlimento, int yAlimento,
                              ArrayList<Obstaculo> obstaculos) {
        System.out.println();//inverte a matriz para garantir que as o y n fique na posição mais alta
        for (int y = Robo.TAMANHO_AREA - 1; y >= 0; y--) { // y maior fica em cima
            String linha = "";
            for (int x = 0; x < Robo.TAMANHO_AREA; x++) {
                String celula = "";

                // robôs que não explodiram e estão nesta casa
                for (Robo r : robos) { //percorre a lista robôs
                    if (!r.isExplodido() && r.getX() == x && r.getY() == y) {
                        celula = celula + r.getCor().substring(0, 1).toUpperCase();//guarda na celula pra seer impressa no mapa
                        //substring faz ele extrair apenas a primeira aletra
                        //topUpperCase converte para maiuscuyla
                    }
                }

                // se não tem robô, mostra obstáculo, alimento ou ponto vazio
                if (celula.equals("")) {
                    //verifica se o tecto na celula esta vazio
                    Obstaculo o = obstaculoNaPosicao(obstaculos, x, y);
                    if (o != null) { //verifica se ele encontrou uma bomba ou rocha
                        celula = o.getSimbolo();
                    } else if (x == xAlimento && y == yAlimento) { //caso nao haja robo, ele verifica
                        //se as coordenadas atuais batem comas do alimento
                        celula = "*";
                    } else {
                        celula = "."; //se n tem nada, eh espaço livre
                    }
                }
                linha = linha + String.format("[ %-2s]", celula);
            }
            System.out.println(linha);
        }
        System.out.println();
    }

    /** Devolve o obstáculo ativo que está em (x, y), ou null se não houver. */
    public static Obstaculo obstaculoNaPosicao(ArrayList<Obstaculo> obstaculos, int x, int y) {
        for (Obstaculo o : obstaculos) {
            if (o.isAtivo() && o.getX() == x && o.getY() == y) {
                return o;
            }
        }
        return null;
    }

    /** Espera um tempo para dar para enxergar os robôs andando. */
    public static void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
