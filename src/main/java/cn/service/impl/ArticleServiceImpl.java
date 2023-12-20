package cn.service.impl;

import cn.common.BaseContext;
import cn.common.Code;
import cn.common.Result;
import cn.common.SnowFlake;
import cn.domain.*;
import cn.dto.MessageDto;
import cn.exception.NotHandlerException;
import cn.mapper.ArticleMapper;
import cn.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    @Override
    public void save(MessageDto messageDto) {
        log.info("messageId->{}", messageDto.getCatId());

        //初始化
        //catId需提前获取写入文章表中, 手动设置
        Long catId = SnowFlake.nextId();
        Long userId = BaseContext.get();
        log.info("articleUserId-{}", userId);

        //插入文章信息
        Article article = new Article();
        article.setUserId(userId);
        article.setCatId(catId);

        //更改猫咪信息并插入
        messageDto.setCatId(catId);
        messageService.save(messageDto);

        //插入猫咪标签关系表
        Medium medium = new Medium();
        medium.setCatId(catId);
        for (Tag tag : messageDto.getTags()) {
            medium.setTagId(tag.getTagId());
            mediumService.save(medium);
        }

        //插入图片信息
        List<Image> imageList = messageDto.getPicture();
        imageList = imageList.stream().map((item) -> {
            item.setCatId(catId);
            item.setImageId(SnowFlake.nextId());

            return item;
        }).collect(Collectors.toList());

        imageService.saveBatch(imageList);

        this.save(article);     //如果代码中断, 即使数据被上传最后的文章依然没有新增进数据库, 即查找时无法找寻
    }

    @Override
    public void remove(List<Long> ids) {
        log.warn("ids={}", ids);

        try {
            //根据文章id找到对应猫咪信息
            List<Long> messageIds = new ArrayList<>();

            for (Long id : ids) {
                messageIds.add(this.getById(id).getCatId());
            }

            log.info("messageIds={}", messageIds);

            //通过猫咪id找到图片id
            LambdaQueryWrapper<Image> wrapper;
            for (Long catIds : messageIds) {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Image::getCatId, catIds);
                imageService.remove(wrapper);
            }

            this.removeByIds(ids);
            messageService.removeByIds(messageIds);
            mediumService.removeByIds(messageIds);

        } catch (Exception e) {
            throw new NotHandlerException();
        }
    }

    @Override
    public void updateMessage(MessageDto messageDto) {
        Long catId = messageDto.getCatId();

        messageService.updateById(messageDto);

        //将medium中catId所对应的标签全部删除, 并重新插入, 当更新的标签数量小于原数时, 无法全部覆盖
        //删除关系表
        LambdaQueryWrapper<Medium> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Medium::getCatId, catId);
        mediumService.remove(queryWrapper);

        //重新插入
        Medium medium = new Medium();
        medium.setCatId(catId);
        for (Tag tag : messageDto.getTags()) {
            medium.setTagId(tag.getTagId());
            mediumService.save(medium);
        }

        //删除图片表
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Image::getCatId, catId);
        imageService.remove(wrapper);

        //插入图片信息
        List<Image> imageList = messageDto.getPicture();
        imageList = imageList.stream().map((item) -> {
            item.setCatId(catId);
            item.setImageId(SnowFlake.nextId());

            return item;
        }).collect(Collectors.toList());

    }

    @Override
    public Result<Page> selectPage(Integer pageNum, Integer pageSize, String name, List<Long> tagIds) {
        /**找到对应name的猫咪id
         * 根据tagId在关系表中找到对应的猫咪信息
         * 二者只能存在其一, 以搜索为主
         */
        List<Message> messageList = null;

        if (StringUtils.hasText(name)) {
            LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            messageLambdaQueryWrapper.like(Message::getCatName, name);

            messageList = messageService.list(messageLambdaQueryWrapper);
        } else if (tagIds != null){
            /**这里先将标签id逐一遍历找出符合条件的catId后再进行赋值
             * 若遍历的同时找出答案再遍历后面的标签会不断添加, 标签条件应该在每次查询后累加条件
             */
            LambdaQueryWrapper<Medium>  mediumLambdaQueryWrapper = new LambdaQueryWrapper<>();
            for (Long tagId : tagIds) {
                mediumLambdaQueryWrapper.eq(tagId !=0, Medium::getTagId, tagId);
            }
            List<Long> catIds = mediumService.list(mediumLambdaQueryWrapper).stream().map((item) -> {       //符合条件后的medium
                return item.getCatId();
            }).collect(Collectors.toList());

            messageList = messageService.listByIds(catIds);

        } else {
            messageList = messageService.list();
        }

        /**
         * 通过catId查找对应文章
         * 通过最后的catId找寻每个猫咪对应的图片, 将其存入dto中
         * 将所有数据存入article中
         */
        List<Article> articleList = messageList.stream().map((item) -> {
            try {
                //拷贝到dto中
                MessageDto messageDto = new MessageDto();
                BeanUtils.copyProperties(item, messageDto);

                //找寻图片
                LambdaQueryWrapper<Image> imageLambdaQueryWrapper = new LambdaQueryWrapper<>();
                imageLambdaQueryWrapper.eq(Image::getCatId, item.getCatId());

                messageDto.setPicture(imageService.list(imageLambdaQueryWrapper));

                //找寻文章
                LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleLambdaQueryWrapper.eq(Article::getCatId, item.getCatId());

                Article article = this.getOne(articleLambdaQueryWrapper);
                article.setMessageDto(messageDto);

                return article;
            } catch (NullPointerException e) {
                throw new NotHandlerException(e.getMessage());
            }

        }).collect(Collectors.toList());


        /**
         * 实现分页功能并覆盖records
         */
        Page<Article> page = new Page<>(pageNum, pageSize);
        this.page(page);

        page.setRecords(articleList);

        return Result.success(page, Code.OK);
    }

    @Override
    public Result<Article> selectOne(Long articleId) {
        /**
         * 通过articleId找到catId并找出相对信息存入article的自定义属性中
         */

        Article article = this.getById(articleId);
        Long catId = article.getCatId();

        //处理messageDto
        MessageDto messageDto = new MessageDto();
        Message message = messageService.getById(catId);
        BeanUtils.copyProperties(message, messageDto);

        //获取关系表中与catId有关的tagId
        LambdaQueryWrapper<Medium> mediumLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mediumLambdaQueryWrapper.eq(Medium::getCatId, catId);

        List<Long> tagIdsList = mediumService.list(mediumLambdaQueryWrapper).stream().map((item) -> {
            return item.getTagId();
        }).collect(Collectors.toList());

        messageDto.setTags(tagService.listByIds(tagIdsList));

        //获取图片信息
        LambdaQueryWrapper<Image> imageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        imageLambdaQueryWrapper.eq(Image::getCatId, catId);

        messageDto.setPicture(imageService.list(imageLambdaQueryWrapper));

        article.setMessageDto(messageDto);

        return Result.success(article, Code.OK);
    }
}
