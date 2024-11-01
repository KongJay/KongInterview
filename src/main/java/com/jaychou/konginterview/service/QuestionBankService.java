package com.jaychou.konginterview.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jaychou.konginterview.model.dto.QuestionBank.QuestionBankQueryRequest;
import com.jaychou.konginterview.model.entity.QuestionBank;
import com.jaychou.konginterview.model.vo.QuestionBankVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题库服务
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://www.hongmofang.top">KongのBlog</a>
 */
public interface QuestionBankService extends IService<QuestionBank> {

    /**
     * 校验数据
     *
     * @param question_bank
     * @param add 对创建的数据进行校验
     */
    void validQuestionBank(QuestionBank question_bank, boolean add);

    /**
     * 获取查询条件
     *
     * @param question_bankQueryRequest
     * @return
     */
    QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest question_bankQueryRequest);
    
    /**
     * 获取题库封装
     *
     * @param question_bank
     * @param request
     * @return
     */
    QuestionBankVO getQuestionBankVO(QuestionBank question_bank, HttpServletRequest request);

    /**
     * 分页获取题库封装
     *
     * @param question_bankPage
     * @param request
     * @return
     */
    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> question_bankPage, HttpServletRequest request);
}
