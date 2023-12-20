package cn.exception;

import cn.common.Code;
import lombok.Data;

@Data
public class AuthorizationException extends Exception{

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {       //cause:引起原因, 有try/catch捕捉提供
        super(message);
    }
}
