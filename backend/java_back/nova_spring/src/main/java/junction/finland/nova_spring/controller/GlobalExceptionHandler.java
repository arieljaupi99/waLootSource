package junction.finland.nova_spring.controller;

import junction.finland.nova_spring.exception.*;
import junction.finland.nova_spring.model.ErrorCode;
import junction.finland.nova_spring.model.ErrorResponse;
import junction.finland.nova_spring.model.RequestData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static junction.finland.nova_spring.utility.RequestDataUtility.getCurrentDateTime;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @Autowired
    RequestData requestData;
    private static final String EXCEPTION = " handleException - Exception ";

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NominativeApiError.class)
    public ResponseEntity<Object> handleNominativeApiError(NominativeApiError ex) {
        return getErrorResponse("500", ErrorCode.NOMINATIVE_API_ERROR, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return getErrorResponse("400" , ErrorCode.BAD_REQUEST , ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationCustomException.class)
    public ResponseEntity<Object> handleAuthException(AuthenticationCustomException ex) {
        return getErrorResponse("401" , ErrorCode.BAD_REQUEST , ex , HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException ex) {
        log.info(EXCEPTION + ex.getMessage());
        return getInternalServerErrorResponse(ex);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleResourceNotFoud(ResourceNotFound ex) {
        return getErrorResponse("404" , ErrorCode.NOT_FOUND_ERROR,ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CoinAlreadyCompletedException.class)
    public ResponseEntity<Object> handleCoinAlreadyCompleted(CoinAlreadyCompletedException ex){
        return getErrorResponse("400" , ErrorCode.BAD_REQUEST , ex , HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getErrorResponse(String status, ErrorCode errorCode, RuntimeException ex, HttpStatus httpStatus) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(String.valueOf(httpStatus.value()));
        error.setStatus(status);
        error.setErrorCode(errorCode);
        error.setDetail(ex.getMessage());
        error.setServerDateTime(getCurrentDateTime());
        error.setClientDateTime(this.requestData.getLastClientDateTime());
        return new ResponseEntity<>(error, httpStatus);
    }


    private ResponseEntity<Object> getInternalServerErrorResponse(Exception ex) {
        return getErrorResponse("500", ErrorCode.INTERNAL_SERVER_ERROR, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> getErrorResponse(String status, ErrorCode errorCode, Exception ex, HttpStatus httpStatus) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status);
        error.setErrorCode(errorCode);
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            error.setDetail(methodArgumentNotValidException.getBindingResult().getAllErrors().
                    stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(" - "))
            );
        } else {
            error.setDetail(ex.getMessage());
        }
        error.setClientDateTime(this.requestData.getLastClientDateTime());
        error.setServerDateTime(getCurrentDateTime());

        return new ResponseEntity<>(error, httpStatus);
    }
}
