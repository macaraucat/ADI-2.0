package cl.aduana.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BadRequestException.class) // 400 BAD REQUEST
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Verifique los datos enviados", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class) // 400 BAD REQUEST DE PARÁMETRO FALTANTE
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e){
        String mensaje = "El parámetro '" + e.getParameterName() + "' es obligatorio";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Parámetro faltante", mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class) // 404 RESOURCE NOT FOUND
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Recurso no encontrado", e.getMessage(), LocalDateTime.now()));
    }    

    @ExceptionHandler(InvalidStateException.class) // 409 CONFLICT
    public ResponseEntity<ErrorResponse> hanfleInvalidStateException(InvalidStateException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Operación no permitida", e.getMessage(), LocalDateTime.now()));
    }    

    @ExceptionHandler(Exception.class) // 500 INTERNAL SERVER ERROR
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno del servidor", e.getMessage(), LocalDateTime.now()));
    }
}