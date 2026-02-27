package com.teste.primeiro_exemplo.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class Temp2 extends RuntimeException {

    public Temp2(String message) {
        super(message);
    }

}
