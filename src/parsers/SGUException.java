/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

/**
 *
 * @author ARMAX
 */
public class SGUException extends RuntimeException {

    public SGUException(String message)
    {
        super(message);
    }

    public SGUException(String message, Throwable cause)
    {
        super(message);
        super.setStackTrace(cause.getStackTrace());
    }

    public static void Throw(String message) throws SGUException
    {
        throw new SGUException(message);
    }

    public static void Throw(String message, Throwable cause) throws SGUException
    {
        throw new SGUException(message, cause);
    }
}
