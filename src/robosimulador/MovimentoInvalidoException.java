package robosimulador;

/**
  Exceção lançada quando o robô faz um movimento inválido
  (zona negativa, fora da área ou direção desconhecida).
 */
public class MovimentoInvalidoException extends Exception {

    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}