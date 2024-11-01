package com.jaychou.konginterview.model.vo;

import com.jaychou.konginterview.model.entity.QuestionBank;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题库视图
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;
    /**
     * 图片
     */
    private String picture;

    /**
     * 封装类转对象
     *
     * @param question_bankVO
     * @return
     */
    public static QuestionBank voToObj(QuestionBankVO question_bankVO) {
        if (question_bankVO == null) {
            return null;
        }
        QuestionBank question_bank = new QuestionBank();
        BeanUtils.copyProperties(question_bankVO, question_bank);

        return question_bank;
    }

    /**
     * 对象转封装类
     *
     * @param question_bank
     * @return
     */
    public static QuestionBankVO objToVo(QuestionBank question_bank) {
        if (question_bank == null) {
            return null;
        }
        QuestionBankVO question_bankVO = new QuestionBankVO();
        BeanUtils.copyProperties(question_bank, question_bankVO);

        return question_bankVO;
    }
}
