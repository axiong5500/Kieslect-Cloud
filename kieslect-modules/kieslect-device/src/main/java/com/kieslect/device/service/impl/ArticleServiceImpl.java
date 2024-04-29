package com.kieslect.device.service.impl;

import com.kieslect.device.domain.Article;
import com.kieslect.device.mapper.ArticleMapper;
import com.kieslect.device.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-29
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
