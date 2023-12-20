package cn.controller;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Tag;
import cn.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/tag")
@Validated
@Slf4j
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/save")
    public Result<String> save(@RequestBody @Valid Tag tag) {
        log.info("tag-name:{},tag-sort:{}", tag.getTagName(), tag.getTagSort());

        return tagService.saveTag(tag);
    }

    @DeleteMapping("/delete/{ids}")
    public Result<String> delete(@PathVariable @Valid @NotBlank(message = "删除失败!") List<Long> ids) {
        tagService.remove(ids);

        return Result.success("删除成功!", Code.DELETED);
    }

    @PutMapping("/update")
    public Result<String> update(@RequestParam(required = true) @Valid @NotNull(message = "无法更新") Long id, @RequestParam(required = true) @Valid @NotEmpty(message = "无法更新") String name) {
        log.info("tag-id:{},tag-name:{}", id, name);

        return tagService.update(id, name);
    }

    @GetMapping("/list/{pageNum}/{pageSize}")
    public Result<List<Tag>> getTag(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {

        return tagService.findTag(pageNum, pageSize);
    }
}
