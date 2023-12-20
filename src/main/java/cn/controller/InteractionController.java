package cn.controller;

import cn.common.BaseContext;
import cn.common.Code;
import cn.common.Result;
import cn.common.WordFilter;
import cn.domain.Interaction;
import cn.service.InteractionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/interaction")
@Validated
@Slf4j
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    //逻辑判断防止一个用户留言多条数据
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Valid Interaction interaction) {
        log.info(interaction.getInteractionMessage());

        return interactionService.save(interaction.getInteractionMessage());
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@Valid @NotNull(message = "信息有误") @RequestParam List<Long> ids) {
        boolean isDelete = interactionService.removeByIds(ids);

        return isDelete == true ? Result.success("删除成功!", Code.DELETED) : Result.fail("删除失败,请稍后再试", Code.BAD_REQUEST);
    }

    //前台处理：只展示留言信息
    @GetMapping("/get/list")
    public Result<List<Interaction>> list() {
        LambdaQueryWrapper<Interaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Interaction::getPublishTime);

        List<Interaction> interactionList = interactionService.list(queryWrapper);

        return Result.success(interactionList, Code.OK);
    }

    //后台处理：查看留言信息并获取发表的用户信息
    @GetMapping("/page/{pageNum}/{pageSize}")
    public Result<Page> getInteraction(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody(required = false) String messageValue) {
        log.info("pageNum->{}&pageSize->{}", pageNum, pageSize);

        return interactionService.getList(pageNum, pageSize, messageValue);
    }
}
