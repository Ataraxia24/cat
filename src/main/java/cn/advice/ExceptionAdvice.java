package cn.advice;

import cn.common.Code;
import cn.common.Result;
import cn.exception.AuthorizationException;
import cn.exception.CodeNotFoundException;
import cn.exception.NotHandlerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {

    //对自定义异常进行捕捉, 这里拦截器捕捉后内部还会抛出500, 只能在整个exception中对单独的自定义异常做出判断
    @ExceptionHandler(Exception.class)
    public Result<String> catchAuthorization(Exception exception) {
        if (exception instanceof AuthorizationException) {
            return Result.fail(exception.getMessage(), Code.UNAUTHORIZED);
        } else {
            exception.printStackTrace();
            return Result.fail("error", Code.INTERNAL_SERVER_ERROR);
        }
    }

    //当出现验证码不匹配时
    @ExceptionHandler(CodeNotFoundException.class)
    public Result<String> catchCodeNotFound(CodeNotFoundException exception) {
        return Result.fail(exception.getMessage(), exception.getCode());
    }

    /**
     * 自定义返回协议与boot集成的协议 ResponseEntity, 专门对valid非法数据做处理
     * 返回的错误信息与Result不互通, ResponseEntity可以代替, 但无法灵活返回数据, 自定义有名称更灵活, entity只能返回纯数据
     * @param ex 实体类校验后通过controller层返回的对象
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("图片过大!");
    }

    @ExceptionHandler(NotHandlerException.class)
    public ResponseEntity handleNullPointerException(NotHandlerException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("处理失败,请稍后再试");
    }
}
