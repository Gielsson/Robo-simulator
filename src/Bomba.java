package robosimulador;

/** Bomba: o robô que encostar explode e a bomba some do tabuleiro. */
public class Bomba extends Obstaculo {

    public Bomba(int id, int x, int y) {
        super(id, x, y, "B");//simbolo de bomba
    }

    @Override
    public void bater(Robo robo) {
        robo.explodir();
        desativar(); // a bomba também desaparece
        System.out.println("BOOM! O robô " + robo.getCor() + " explodiu na bomba "
                + getId() + " em (" + getX() + ", " + getY() + ").");
    }
}
