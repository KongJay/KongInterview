package com.jaychou.konginterview.esdao;

import com.jaychou.konginterview.model.dto.post.PostEsDTO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://hongmofang.top">KongのBlog</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}