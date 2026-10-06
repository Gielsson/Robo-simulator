
package robosimulador;

/**
 * Classe abstrata: representa qualquer obstáculo do tabuleiro.
 * Cada tipo de obstáculo decide o que acontece quando o robô bate nele.
 */
public abstract class Obstaculo {

    private int id;
    private int x;
    private int y;
    private String simbolo;   // letra usada na matriz do console
    private boolean ativo;    // false = sumiu do tabuleiro

    public Obstaculo(int id, int x, int y, String simbolo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.simbolo = simbolo;
        this.ativo = true;
    }

    /** O que acontece quando o robô encosta no obstáculo. */
    public abstract void bater(Robo robo);

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    /** Tira o obstáculo do tabuleiro. */
    public void desativar() {
        ativo = false;
    }
}