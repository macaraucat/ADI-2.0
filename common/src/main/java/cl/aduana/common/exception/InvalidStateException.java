package cl.aduana.common.exception;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String mensaje){ 
        super(mensaje);
    }
}