package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.Article;
import com.kieslect.device.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    IArticleService articleService;

    @GetMapping("/sys/getList")
    private R<?> sysGetArticleList(){
        return R.ok(articleService.list());
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
        return R.ok();
    }

}
