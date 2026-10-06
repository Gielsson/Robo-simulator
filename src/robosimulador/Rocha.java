package robosimulador;

/** Rocha: o robô que bate nela volta para a posição anterior. A rocha continua no lugar. */
public class Rocha extends Obstaculo {

    public Rocha(int id, int x, int y) {
        super(id, x, y, "R");
    }

    @Override
    public void bater(Robo robo) {
        robo.voltarParaPosicaoAnterior();
        System.out.println("O robô " + robo.getCor() + " bateu na rocha " + getId()
                + " e voltou para (" + robo.getX() + ", " + robo.getY() + ").");
    }
}