package cn.controller;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Article;
import cn.domain.Tag;
import cn.dto.MessageDto;
import cn.service.ArticleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/article")
@Validated
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //新增有关猫猫的文章
    @PostMapping("/add")
    public Result<String> save(@RequestBody @Valid MessageDto messageDto) {
        log.info("cat=>{}", messageDto);

        articleService.save(messageDto);

        return Result.success("发布成功!", Code.CREATED);
    }

    //删除有关猫猫的文章
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam @Valid @NotNull(message = "id不能为空") List<Long> ids) {
        articleService.remove(ids);

        return Result.success("删除成功!", Code.DELETED);
    }

    //修改有关猫猫文章
    @PutMapping("/update/{id}")
    public Result<String> update(@PathVariable @Valid @NotNull(message = "id不能为空") Long id, @RequestBody @Valid MessageDto messageDto) {
        log.info("update-id:{}", id);

        //查找出需要更改的猫猫信息的id
        @NotNull(message = "数据有误") Long catId = articleService.getById(id).getCatId();
        messageDto.setCatId(catId);

        articleService.updateMessage(messageDto);

        return Result.success("更新成功", Code.OK);
    }

    //查询文章, 利用文章查找猫咪信息
    @GetMapping("/get/page/{pageNum}/{pageSize}")
    public Result<Page> select(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String name, @RequestParam(required = false) List<Long> tagId) {
        return articleService.selectPage(pageNum, pageSize, name, tagId);
    }

    //通过id查找单个文章
    @GetMapping("/getOne/{id}")
    public Result<Article> selectOne(@PathVariable @Valid @NotNull(message = "错误id") Long id) {
        return articleService.selectOne(id);
    }

}
