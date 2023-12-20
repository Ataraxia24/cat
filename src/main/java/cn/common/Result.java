package cn.common;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private String code;
    private T data;
    private String token;

    public Result(){}

    public Result(Result<T> tokenResult, String token) {
        this.msg = tokenResult.msg;
        this.code = tokenResult.code;
        this.data = tokenResult.data;
        this.token = token;
    }

    public static <T>Result<T> success(T object, String code) {
        Result<T> result = new Result<>();
        if (object instanceof String) {
            result.msg = (String)object;
        } else {
            result.data = object;
        }

        result.code = code;
        return result;
    }

    public static <T>Result<T> fail(String msg, String code) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        return result;
    }
}
