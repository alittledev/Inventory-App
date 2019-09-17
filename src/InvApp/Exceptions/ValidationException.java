/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.Exceptions;

/**
 *
 * @author allis
 */
public class ValidationException extends Exception {
    /**
     * Constructor. Simply passes the exception message to the base handler.
     * 
     * @param message 
     */
    public ValidationException(String message) {
        super(message);
    }
}
    

