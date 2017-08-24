package com.besafx.app.config;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, Optional.ofNullable(ex.getMessage()).isPresent() ? ex.getMessage() : "هناك خطأ ما، فضلاً قم بإعادة تحميل الصفحة او الاتصال بالدعم بالفني", "error occurred");
        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public class ApiError {

        @Getter
        private HttpStatus status;

        @Getter
        private String message;

        @Getter
        private List<String> errors;

        public ApiError(HttpStatus status, String message, List<String> errors) {
            super();
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public ApiError(HttpStatus status, String message, String error) {
            super();
            this.status = status;
            this.message = message;
            errors = Arrays.asList(error);
        }
    }

}
