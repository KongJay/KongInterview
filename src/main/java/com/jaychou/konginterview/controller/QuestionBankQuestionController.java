package com.jaychou.konginterview.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaychou.konginterview.annotation.AuthCheck;
import com.jaychou.konginterview.common.BaseResponse;
import com.jaychou.konginterview.common.DeleteRequest;
import com.jaychou.konginterview.common.ErrorCode;
import com.jaychou.konginterview.common.ResultUtils;
import com.jaychou.konginterview.constant.UserConstant;
import com.jaychou.konginterview.exception.BusinessException;
import com.jaychou.konginterview.exception.ThrowUtils;
import com.jaychou.konginterview.model.dto.QuestionBankQuestion.QuestionBankQuestionAddRequest;

import com.jaychou.konginterview.model.dto.QuestionBankQuestion.QuestionBankQuestionQueryRequest;
import com.jaychou.konginterview.model.dto.QuestionBankQuestion.QuestionBankQuestionUpdateRequest;
import com.jaychou.konginterview.model.entity.QuestionBankQuestion;
import com.jaychou.konginterview.model.entity.User;
import com.jaychou.konginterview.model.vo.QuestionBankQuestionVO;
import com.jaychou.konginterview.service.QuestionBankQuestionService;
import com.jaychou.konginterview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题库题目关联接口
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://www.hongmofang.top">KongのBlog</a>
 */
@RestController
@RequestMapping("/QuestionBankQuestion")
@Slf4j
public class QuestionBankQuestionController {

    @Resource
    private QuestionBankQuestionService QuestionBankQuestionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题库题目关联
     *
     * @param QuestionBankQuestionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionBankQuestion(@RequestBody QuestionBankQuestionAddRequest QuestionBankQuestionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(QuestionBankQuestionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        QuestionBankQuestion QuestionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(QuestionBankQuestionAddRequest, QuestionBankQuestion);
        // 数据校验
        QuestionBankQuestionService.validQuestionBankQuestion(QuestionBankQuestion, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        QuestionBankQuestion.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = QuestionBankQuestionService.save(QuestionBankQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBankQuestionId = QuestionBankQuestion.getId();
        return ResultUtils.success(newQuestionBankQuestionId);
    }

    /**
     * 删除题库题目关联
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionBankQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionBankQuestion oldQuestionBankQuestion = QuestionBankQuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestionBankQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionBankQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = QuestionBankQuestionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题库题目关联（仅管理员可用）
     *
     * @param QuestionBankQuestionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBankQuestion(@RequestBody QuestionBankQuestionUpdateRequest QuestionBankQuestionUpdateRequest) {
        if (QuestionBankQuestionUpdateRequest == null || QuestionBankQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        QuestionBankQuestion QuestionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(QuestionBankQuestionUpdateRequest, QuestionBankQuestion);
        // 数据校验
        QuestionBankQuestionService.validQuestionBankQuestion(QuestionBankQuestion, false);
        // 判断是否存在
        long id = QuestionBankQuestionUpdateRequest.getId();
        QuestionBankQuestion oldQuestionBankQuestion = QuestionBankQuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestionBankQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = QuestionBankQuestionService.updateById(QuestionBankQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题库题目关联（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankQuestionVO> getQuestionBankQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QuestionBankQuestion QuestionBankQuestion = QuestionBankQuestionService.getById(id);
        ThrowUtils.throwIf(QuestionBankQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(QuestionBankQuestionService.getQuestionBankQuestionVO(QuestionBankQuestion, request));
    }

    /**
     * 分页获取题库题目关联列表（仅管理员可用）
     *
     * @param QuestionBankQuestionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionBankQuestion>> listQuestionBankQuestionByPage(@RequestBody QuestionBankQuestionQueryRequest QuestionBankQuestionQueryRequest) {
        long current = QuestionBankQuestionQueryRequest.getCurrent();
        long size = QuestionBankQuestionQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBankQuestion> QuestionBankQuestionPage = QuestionBankQuestionService.page(new Page<>(current, size),
                QuestionBankQuestionService.getQueryWrapper(QuestionBankQuestionQueryRequest));
        return ResultUtils.success(QuestionBankQuestionPage);
    }

    /**
     * 分页获取题库题目关联列表（封装类）
     *
     * @param QuestionBankQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionBankQuestionVO>> listQuestionBankQuestionVOByPage(@RequestBody QuestionBankQuestionQueryRequest QuestionBankQuestionQueryRequest,
                                                               HttpServletRequest request) {
        long current = QuestionBankQuestionQueryRequest.getCurrent();
        long size = QuestionBankQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBankQuestion> QuestionBankQuestionPage = QuestionBankQuestionService.page(new Page<>(current, size),
                QuestionBankQuestionService.getQueryWrapper(QuestionBankQuestionQueryRequest));
        // 获取封装类
        return ResultUtils.success(QuestionBankQuestionService.getQuestionBankQuestionVOPage(QuestionBankQuestionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题库题目关联列表
     *
     * @param QuestionBankQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionBankQuestionVO>> listMyQuestionBankQuestionVOByPage(@RequestBody QuestionBankQuestionQueryRequest QuestionBankQuestionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(QuestionBankQuestionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        QuestionBankQuestionQueryRequest.setUserId(loginUser.getId());
        long current = QuestionBankQuestionQueryRequest.getCurrent();
        long size = QuestionBankQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBankQuestion> QuestionBankQuestionPage = QuestionBankQuestionService.page(new Page<>(current, size),
                QuestionBankQuestionService.getQueryWrapper(QuestionBankQuestionQueryRequest));
        // 获取封装类
        return ResultUtils.success(QuestionBankQuestionService.getQuestionBankQuestionVOPage(QuestionBankQuestionPage, request));
    }

   

    // endregion
}
