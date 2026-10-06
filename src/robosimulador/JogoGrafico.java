package robosimulador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Versão gráfica (Swing) do jogo da Main4.
 * 1) Escolha o que colocar (comida, bomba ou rocha) e clique numa casa.
 * 2) Clique em "Iniciar" e veja os robôs andando.
 */
public class JogoGrafico extends JFrame {

    private static final int TAM = 100; // tamanho de cada casa em pixels

    private Robo normal;
    private RoboInteligente inteligente;
    private ArrayList<Obstaculo> obstaculos = new ArrayList<>();
    private int xAlimento = -1; // -1 = ainda não escolhido
    private int yAlimento = -1;
    private String modo = "comida"; // o que o clique coloca: comida, bomba ou rocha
    private boolean jogando = false;
    private int vez = 0;            // 0 = normal, 1 = inteligente
    private int proximoId = 1;
    private Random sorteio = new Random();

    private Timer timer;            // chama passo() a cada 600 ms
    private JLabel status = new JLabel(" ");
    private PainelTabuleiro painel = new PainelTabuleiro();

    public JogoGrafico() {
        super("Jogo do Robô");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Botões do topo
        JPanel botoes = new JPanel();
        JButton btnComida = new JButton("Comida");
        JButton btnBomba = new JButton("Bomba");
        JButton btnRocha = new JButton("Rocha");
        JButton btnIniciar = new JButton("Iniciar");
        JButton btnReiniciar = new JButton("Reiniciar");
        botoes.add(btnComida);
        botoes.add(btnBomba);
        botoes.add(btnRocha);
        botoes.add(btnIniciar);
        botoes.add(btnReiniciar);

        btnComida.addActionListener(e -> escolherModo("comida"));
        btnBomba.addActionListener(e -> escolherModo("bomba"));
        btnRocha.addActionListener(e -> escolherModo("rocha"));
        btnIniciar.addActionListener(e -> iniciar());
        btnReiniciar.addActionListener(e -> novoJogo());

        add(botoes, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        timer = new Timer(600, e -> passo());

        novoJogo();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ------------------------------------------------------------------
    // Controle do jogo
    // ------------------------------------------------------------------

    private void novoJogo() {
        timer.stop();
        normal = new Robo("Azul");
        inteligente = new RoboInteligente("Vermelho");
        obstaculos.clear();
        xAlimento = -1;
        yAlimento = -1;
        jogando = false;
        vez = 0;
        proximoId = 1;
        modo = "comida";
        status.setText("Clique numa casa para colocar a comida.");
        painel.repaint();
    }

    private void escolherModo(String novoModo) {
        modo = novoModo;
        status.setText("Modo: " + novoModo + ". Clique numa casa do tabuleiro.");
    }

    // Chamado quando o usuário clica na casa (x, y).
    private void clicou(int x, int y) {
        if (jogando) {
            return; // não dá para mexer no tabuleiro durante o jogo
        }
        if (x == 0 && y == 0) {
            status.setText("A casa (0,0) é a saída dos robôs. Escolha outra.");
            return;
        }
        if (Tabuleiro.obstaculoNaPosicao(obstaculos, x, y) != null) {
            status.setText("Já existe um obstáculo nessa casa.");
            return;
        }

        if (modo.equals("comida")) {
            xAlimento = x;
            yAlimento = y;
            status.setText("Comida em (" + x + ", " + y + ").");
        } else {
            if (x == xAlimento && y == yAlimento) {
                status.setText("Essa casa tem a comida. Escolha outra.");
                return;
            }
            if (modo.equals("bomba")) {
                obstaculos.add(new Bomba(proximoId, x, y));
            } else {
                obstaculos.add(new Rocha(proximoId, x, y));
            }
            proximoId++;
            status.setText("Colocou " + modo + " em (" + x + ", " + y + ").");
        }
        painel.repaint();
    }

    private void iniciar() {
        if (jogando) {
            return;
        }
        if (xAlimento < 0) {
            status.setText("Coloque a comida antes de iniciar!");
            return;
        }
        jogando = true;
        status.setText("Jogo rodando...");
        timer.start();
    }

    // Um passo do jogo: move o robô da vez (chamado pelo Timer).
    private void passo() {
        Robo atual = (vez == 0) ? normal : inteligente;

        if (!atual.isExplodido()) {
            try {
                atual.mover(sorteio.nextInt(4) + 1);
            } catch (MovimentoInvalidoException e) {
                status.setText("ERRO (" + atual.getCor() + "): " + e.getMessage());
            }

            Obstaculo o = Tabuleiro.obstaculoNaPosicao(obstaculos, atual.getX(), atual.getY());
            if (o != null) {
                o.bater(atual);
            }
            painel.repaint();

            if (atual.encontrouAlimento(xAlimento, yAlimento)) {
                terminar("O robô " + atual.getCor() + " achou o alimento!");
                return;
            }
        }

        if (normal.isExplodido() && inteligente.isExplodido()) {
            terminar("Os dois robôs explodiram!");
            return;
        }
        vez = 1 - vez; // passa a vez
    }

    private void terminar(String mensagem) {
        timer.stop();
        status.setText(mensagem);
        String relatorio = mensagem + "\n\n"
                + resumo(normal) + "\n" + resumo(inteligente);
        JOptionPane.showMessageDialog(this, relatorio, "Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
    }

    private String resumo(Robo r) {
        int total = r.getMovimentosValidos() + r.getMovimentosInvalidos();
        String texto = "Robô " + r.getCor() + ": " + total + " movimentos ("
                + r.getMovimentosValidos() + " válidos, "
                + r.getMovimentosInvalidos() + " inválidos)";
        if (r.isExplodido()) {
            texto = texto + " - explodiu";
        }
        return texto;
    }

    // ------------------------------------------------------------------
    // Desenho do tabuleiro
    // ------------------------------------------------------------------

    private class PainelTabuleiro extends JPanel {

        public PainelTabuleiro() {
            setPreferredSize(new Dimension(TAM * Robo.TAMANHO_AREA, TAM * Robo.TAMANHO_AREA));

            // Converte o clique (pixels) em coluna e linha do tabuleiro.
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int x = e.getX() / TAM;
                    int y = Robo.TAMANHO_AREA - 1 - (e.getY() / TAM); // y cresce para cima
                    if (x >= 0 && x < Robo.TAMANHO_AREA && y >= 0 && y < Robo.TAMANHO_AREA) {
                        clicou(x, y);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Casas (xadrez claro) e linhas
            for (int x = 0; x < Robo.TAMANHO_AREA; x++) {
                for (int y = 0; y < Robo.TAMANHO_AREA; y++) {
                    int px = x * TAM;
                    int py = (Robo.TAMANHO_AREA - 1 - y) * TAM;
                    g.setColor((x + y) % 2 == 0 ? Color.WHITE : new Color(230, 230, 230));
                    g.fillRect(px, py, TAM, TAM);
                    g.setColor(Color.BLACK);
                    g.drawRect(px, py, TAM, TAM);
                }
            }

            // Comida (círculo verde em cima da casa)
            if (xAlimento >= 0) {
                int px = xAlimento * TAM;
                int py = (Robo.TAMANHO_AREA - 1 - yAlimento) * TAM;
                g.setColor(new Color(0, 170, 0));
                g.fillOval(px + 35, py + 5, 30, 30);
                g.setColor(Color.BLACK);
                g.drawString("comida", px + 34, py + 52);
            }

            // Obstáculos que ainda estão ativos
            for (Obstaculo o : obstaculos) {
                if (!o.isAtivo()) {
                    continue;
                }
                int px = o.getX() * TAM;
                int py = (Robo.TAMANHO_AREA - 1 - o.getY()) * TAM;
                if (o instanceof Bomba) {
                    g.setColor(Color.BLACK);
                    g.fillOval(px + 20, py + 20, 60, 60);
                    g.setColor(Color.WHITE);
                    g.drawString("Bomba", px + 33, py + 55);
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(px + 15, py + 15, 70, 70);
                    g.setColor(Color.WHITE);
                    g.drawString("Rocha", px + 33, py + 55);
                }
            }

            // Robôs (cada um fica de um lado da casa para não se esconderem)
            desenharRobo(g, normal, Color.BLUE, 10);
            desenharRobo(g, inteligente, Color.RED, 50);
        }

        private void desenharRobo(Graphics g, Robo robo, Color cor, int deslocamento) {
            if (robo.isExplodido()) {
                return; // explodiu: some do tabuleiro
            }
            int px = robo.getX() * TAM + deslocamento;
            int py = (Robo.TAMANHO_AREA - 1 - robo.getY()) * TAM + 50;
            g.setColor(cor);
            g.fillOval(px, py, 40, 40);
            g.setColor(Color.WHITE);
            g.drawString(robo.getCor().substring(0, 1), px + 16, py + 25);
        }
    }

    public static void main(String[] args) {
        new JogoGrafico();
    }
}