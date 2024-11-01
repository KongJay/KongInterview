package com.jaychou.konginterview.model.dto.QuestionBank;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新题库请求
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String picture;
    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;
}