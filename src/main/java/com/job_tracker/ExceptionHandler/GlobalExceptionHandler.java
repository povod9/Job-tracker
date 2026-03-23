package com.job_tracker.ExceptionHandler;

import com.job_tracker.CreateException.AccessDeniedException;
import com.job_tracker.CreateException.InvalidCredentialsException;
import com.job_tracker.Dto.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleNotFoundException(
            EntityNotFoundException e
    )
    {
        ExceptionDto exceptionDto = new ExceptionDto(
                "Not Found",
                e.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionDto> handleInvalidCredentialsException(
            InvalidCredentialsException e
    )
    {
        ExceptionDto exceptionDto = new ExceptionDto(
                "Invalid Credentials",
                e.getMessage(),
                OffsetDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exceptionDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<ExceptionDto> handleIllegalArgumentException(
            IllegalArgumentException e
    )
    {
        ExceptionDto exceptionDto = new ExceptionDto(
                "Illegal Argument",
                e.getMessage(),
                OffsetDateTime.now()
        );
        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAccessDeniedException(
            AccessDeniedException e
    )
    {
        ExceptionDto exceptionDto = new ExceptionDto(
                "Access Denied",
                e.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exceptionDto);
    }
}
