package com.jaychou.konginterview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jaychou.konginterview.model.entity.Post;
import java.util.Date;
import java.util.List;

/**
 * 帖子数据库操作
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://hongmofang.top">KongのBlog</a>
 */
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 查询帖子列表（包括已被删除的数据）
     */
    List<Post> listPostWithDelete(Date minUpdateTime);

}




