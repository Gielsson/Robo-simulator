/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */

/**
 *
 * @author nitue
 */
public class MovimentoInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>movimentoInvalidoException</code> without
     * detail message.
     */
    public MovimentoInvalidoException() {
    }

    /**
     * Constructs an instance of <code>movimentoInvalidoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public MovimentoInvalidoException(String msg) {
        super(msg);
    }
}
