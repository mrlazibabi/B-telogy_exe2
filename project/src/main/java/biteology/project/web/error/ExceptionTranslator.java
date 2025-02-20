package biteology.project.web.error;

import biteology.project.web.error.ExceptionDefine.AuthenticationException;
import biteology.project.web.error.ExceptionDefine.BusinessException;
import biteology.project.web.error.ExceptionDefine.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionTranslator implements ResponseErrorHandler {

    private ResponseEntity<ErrorResponse> badRequest(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> internalServerError(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> notFound(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<ErrorResponse> usernameExist(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorResponse> conflict(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }




    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return internalServerError(
                new ErrorResponse(ErrorConstant.SERVICE_ERROR.getCode(),
                        ErrorConstant.SERVICE_ERROR.getMessage(), map)
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final Map<String, Object> errorResult  = new HashMap<>();
        for(FieldError e: ex.getBindingResult().getFieldErrors()){
            errorResult.put(e.getField(), e.getDefaultMessage());
        }
        //errorResult :
        {
            // {
            //    "name": "Name can not be null !"
            //}
        }
        final ErrorResponse response = new ErrorResponse(ErrorConstant.BAD_REQUEST.getCode(),
                "Validation exception", errorResult);  //details == errorResult
        return badRequest(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<ErrorResponse> handleRegisterException(AuthenticationException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return usernameExist(
                new ErrorResponse(ErrorConstant.UNAUTHORIZED.getCode(),
                        "Authentication exception", map)
        );
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("service", ex.getMessage());
        return usernameExist(
                new ErrorResponse(ErrorConstant.UNAUTHORIZED.getCode(),
                        "Conflict and constraint exception", map)
        );
    }



    private ResponseEntity<ErrorResponse> forbidden(ErrorResponse result){
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }
}
