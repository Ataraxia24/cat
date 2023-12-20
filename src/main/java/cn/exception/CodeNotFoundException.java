package cn.exception;

import lombok.Data;

@Data
public class CodeNotFoundException extends RuntimeException{
    private String code;

    public CodeNotFoundException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
