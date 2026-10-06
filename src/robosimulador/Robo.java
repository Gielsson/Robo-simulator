package robosimulador;

/**
 * Representa o robô (personagem) que se move no eixo cartesiano (x, y).
 *
 * A área de locomoção é um quadrado com 4 unidades de lado, ou seja,
 * x e y vão de 0 a 3. O robô não pode entrar em coordenadas negativas
 * (nem sair da área do tabuleiro).
 */
public class Robo {
 
    // Tamanho do lado da área de locomoção (4x4).
    public static final int TAMANHO_AREA = 4;
 
    private int x;
    private int y;
 
    // Posição antes do último movimento válido (útil para a Rocha, que
    // faz o robô voltar para a posição anterior).
    private int xAnterior;
    private int yAnterior;
 
    private final String cor;
 
    // Contadores usados nas Mains para o relatório final.
    private int movimentosValidos;
    private int movimentosInvalidos;

    // Usado pela Bomba: robô explodido não anda mais nem aparece no tabuleiro.
    private boolean explodido;
 
    
     // Cria um robô na posição (0,0).   
     //parâmetro cor que identifica o robô.
     
    public Robo(String cor) {
        this.cor = cor;
        this.x = 0;
        this.y = 0;
        this.xAnterior = 0;
        this.yAnterior = 0;
    }
 
    // ------------------------------------------------------------------
    // Gets e sets
    // ------------------------------------------------------------------
 
    public int getX() {
        return x;
    }
 
    public int getY() {
        return y;
    }
 
    public String getCor() {
        return cor;
    }
 
    public int getXAnterior() {
        return xAnterior;
    }
 
    public int getYAnterior() {
        return yAnterior;
    }
 
    public int getMovimentosValidos() {
        return movimentosValidos;
    }
 
    public int getMovimentosInvalidos() {
        return movimentosInvalidos;
    }
 
    // Define a posição x. Não aceita valores fora da área (inclusive negativos). 
    public void setX(int x) {
        if (!dentroDaArea(x)) {
            throw new IllegalArgumentException("x fora da área: " + x);
        }
        this.x = x;
    }
 
    // Define a posição y. Não aceita valores fora da área (inclusive negativos). 
    public void setY(int y) {
        if (!dentroDaArea(y)) {
            throw new IllegalArgumentException("y fora da área: " + y);
        }
        this.y = y;
    }
 
    // ------------------------------------------------------------------
    // Movimentos
    // ------------------------------------------------------------------
 
    /**
     * Move o robô conforme a direção informada.
     * "up" (y+1), "down" (y-1), "right" (x+1), "left" (x-1).
     *
     * @param direcao
     * @throws MovimentoInvalidoException se o movimento levar o robô para
     *         uma zona inválida (coordenada negativa ou fora da área) ou
     *         se a direção não existir. Nesse caso o robô NÃO se move.
     */
    public void mover(String direcao) throws MovimentoInvalidoException {
        executarMovimento(direcao);
    }
 
    /**
     * Versão sobrecarregada: recebe um inteiro de 1 a 4.
     * 1 = "cima", 2 = "baixo", 3 = "direita", 4 = "esquerda".
     *
     * throws MovimentoInvalidoException se o código não for de 1 a 4 ou
     *         se o movimento levar o robô para uma zona inválida.
     */
    public void mover(int direcao) throws MovimentoInvalidoException {
        executarMovimento(codigoParaDirecao(direcao));
    }
 
    /**
     * Lógica única de movimento, usada pelas duas versões de mover().
     * Fica separada para que subclasses (ex.: RoboInteligente) possam
     * sobrescrever mover() sem criar chamadas recursivas.
     */
    protected void executarMovimento(String direcao) throws MovimentoInvalidoException {
        int novoX = x;
        int novoY = y;
        String dir = (direcao == null) ? "" : direcao.trim().toLowerCase();
 
        switch (dir) {
            case "up":
                novoY++;
                break;
            case "down":
                novoY--;
                break;
            case "right":
                novoX++;
                break;
            case "left":
                novoX--;
                break;
            default:
                movimentosInvalidos++;
                throw new MovimentoInvalidoException(
                        "Movimento inválido: direção desconhecida \"" + direcao + "\".");
        }
 
        // Valida a nova posição ANTES de mexer no robô.
        if (!dentroDaArea(novoX) || !dentroDaArea(novoY)) {
            movimentosInvalidos++;
            throw new MovimentoInvalidoException(
                    "Movimento inválido: \"" + dir + "\" levaria o robô " + cor
                            + " de (" + x + ", " + y + ") para (" + novoX + ", " + novoY
                            + "), fora da área permitida.");
        }
 
        xAnterior = x;
        yAnterior = y;
        x = novoX;
        y = novoY;
        movimentosValidos++;
 
        mostrarPosicao();
    }
 
    /** Converte o código 1..4 na direção correspondente. */
    protected String codigoParaDirecao(int codigo) throws MovimentoInvalidoException {
        switch (codigo) {
            case 1:
                return "up";
            case 2:
                return "down";
            case 3:
                return "right";
            case 4:
                return "left";
            default:
                movimentosInvalidos++;
                throw new MovimentoInvalidoException(
                        "Movimento inválido: código " + codigo + " (use de 1 a 4).");
        }
    }
 
    /** Faz o robô voltar para a posição anterior (usado pela Rocha). */
    public void voltarParaPosicaoAnterior() {
        x = xAnterior;
        y = yAnterior;
    }
 
    // ------------------------------------------------------------------
    // Alimento e exibição
    // ------------------------------------------------------------------
 
    /**
     * Verifica se o robô está na mesma posição do alimento.
     *
     * @return true se encontrou o alimento
     */
    public boolean encontrouAlimento(int xAlimento, int yAlimento) {
        return x == xAlimento && y == yAlimento;
    }
 
    /** Marca o robô como explodido (chamado pela Bomba). */
    public void explodir() {
        explodido = true;
    }

    public boolean isExplodido() {
        return explodido;
    }

    /** Mostra a posição atual do robô no console. */
    public void mostrarPosicao() {
        System.out.println("Robô " + cor + " está em (" + x + ", " + y + ")");
    }
 
    private boolean dentroDaArea(int valor) {
        return valor >= 0 && valor < TAMANHO_AREA;
    }
}
