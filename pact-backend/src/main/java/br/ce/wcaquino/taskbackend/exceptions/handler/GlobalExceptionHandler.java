package br.ce.wcaquino.taskbackend.exceptions.handler;

import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
/*
 * Essa classe foi criada para customizar o retorno do método save de TaskController.
 * O Spring gera uma mensagem em caso de exceção que não é exatamente a esperada pelo
 * teste de contrato.
 * Assim, foi necessário criar essa classe, customizar o retorno para que a validação
 * feita no teste no projeto pact-frontend parasse de indicar incompatibilidade entre
 * a mensagem retornada e a mensagem esperada.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
