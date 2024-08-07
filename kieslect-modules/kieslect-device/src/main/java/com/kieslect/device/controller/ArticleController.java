package com.kieslect.device.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.Article;
import com.kieslect.device.service.IArticleService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-29
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

     private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    IArticleService articleService;

    @GetMapping("/sys/getList")
    private R<?> sysGetArticleList(){
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("father_id");
        return R.ok(articleService.list(queryWrapper));
    }

    // update
    @PostMapping("/sys/update")
    public R<?> updateAppManage(@RequestBody Article article) {
        return R.ok(articleService.updateById(article));
    }
    // delete
    @PostMapping("/sys/delete")
    public R<?> deleteAppManage(@RequestBody Article article) {
        articleService.removeById(article.getId());
        return R.ok();
    }

    @PostMapping("/sys/save")
    public R<?> saveAppManage(@RequestBody Article article) {
        articleService.save(article);
        return R.ok(article);
    }


    @GetMapping("/sys/getArticleById")
    public R<?> getPrivacyPolicy(@RequestParam(value = "id", required = false) Integer id,@RequestParam(value = "language", required = false) Integer language)  {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getFatherId, id);
        queryWrapper.eq(Article::getLanguage,language);
        List<Article> articles = articleService.list(queryWrapper);
        if (articles.size() > 0){
            return R.ok(BeanUtil.beanToMap(articles.get(0)));
        }

        Article article =  articleService.getById(id);
        if (article == null) {
            return R.fail();
        }

        Map<String, Object> result = BeanUtil.beanToMap(article);
        return R.ok(result);
    }

    @GetMapping("/sys/getMultilingualismList")
    private R<?> sysGetMultilingualismList(@RequestParam(value = "id", required = false) Integer id){
        String title;
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        Article article = articleService.getById(id);
        if (article != null){
            title = article.getTitle();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("title", title);
            queryWrapper.eq("father_id",id);

            List<Article> articles = articleService.list(queryWrapper);
            if (articles.size() > 0) {
                return R.ok(articles);
            }
        }

        return R.ok(new ArrayList<Article>());
    }


}
