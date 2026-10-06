/**
 * Exceção lançada quando o robô tenta fazer um movimento inválido
 * (por exemplo, entrar em uma zona de coordenadas negativas).
 *
 * Estende Exception (checked), portanto quem chama o método mover()
 * é OBRIGADO a tratar com try-catch ou declarar com throws.
 */
public class MovimentoInvalidoException extends Exception {
 
    /**
     * parametro mensagem texto que informa qual movimento foi inválido
     */
    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
 