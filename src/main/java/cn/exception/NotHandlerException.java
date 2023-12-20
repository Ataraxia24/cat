package cn.exception;

import lombok.Data;

@Data
public class NotHandlerException extends RuntimeException{

    public NotHandlerException() {
        super();
    }
    public NotHandlerException(String message) {
        super(message);
    }
}
