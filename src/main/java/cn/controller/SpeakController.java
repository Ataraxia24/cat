package cn.controller;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Speak;
import cn.service.SpeakService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/speak")
@Validated
public class SpeakController {

    @Autowired
    private SpeakService speakService;

    @PostMapping("/add")
    public Result<String> save(@Valid @RequestBody Speak speak) {
        boolean isSave = speakService.save(speak);
        return isSave == true ? Result.success("新增成功!", Code.CREATED) : Result.fail("添加失败,请稍后再试", Code.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@Valid @PathVariable Long id) {
        boolean isDelete = speakService.removeById(id);
        return isDelete == true ? Result.success("删除成功!", Code.DELETED) : Result.fail("删除失败,请稍后再试", Code.BAD_REQUEST);
    }

    @PutMapping("/put")
    public Result<String> update(@Valid @RequestBody Speak speak) {
        boolean isUpdate = speakService.updateById(speak);
        return isUpdate == true ? Result.success("修改成功!", Code.OK) : Result.fail("修改失败,请稍后再试", Code.BAD_REQUEST);
    }

    @GetMapping("/list")
    public Result<List<Speak>> list() {
        LambdaQueryWrapper<Speak> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Speak::getPublishTime);

        return Result.success(speakService.list(wrapper), Code.OK);

    }
}
