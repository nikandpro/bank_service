package github.nikandpro.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(RequestRejectedException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequest(RequestRejectedException ex) {
//        ErrorResponse response = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                ex.getMessage(),
//                System.currentTimeMillis()
//        );
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
//        ErrorResponse response = new ErrorResponse(
//                HttpStatus.FORBIDDEN.value(),
//                ex.getMessage(),
//                System.currentTimeMillis()
//        );
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                System.currentTimeMillis(),
                errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
