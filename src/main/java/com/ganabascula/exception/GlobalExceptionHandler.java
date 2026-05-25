package com.ganabascula.exception;

import com.ganabascula.dto.response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>>
    handleRuntimeException(
            RuntimeException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)

                .body(

                        new ApiResponse<>(

                                ex.getMessage(),

                                null
                        )
                );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>>
    handleUsernameNotFound(
            UsernameNotFoundException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)

                .body(

                        new ApiResponse<>(

                                ex.getMessage(),

                                null
                        )
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>>
    handleBadCredentials(
            BadCredentialsException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)

                .body(

                        new ApiResponse<>(

                                "Credenciales incorrectas",

                                null
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>>
    handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        String mensaje = ex
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)

                .body(

                        new ApiResponse<>(

                                mensaje,

                                null
                        )
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>>
    handleIllegalArgument(
            IllegalArgumentException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)

                .body(

                        new ApiResponse<>(

                                ex.getMessage(),

                                null
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>>
    handleGeneralException(
            Exception ex
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)

                .body(

                        new ApiResponse<>(

                                "Error interno del servidor",

                                null
                        )
                );
    }
}