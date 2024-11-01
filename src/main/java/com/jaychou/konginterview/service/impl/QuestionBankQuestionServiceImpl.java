package com.jaychou.konginterview.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jaychou.konginterview.constant.CommonConstant;
import com.jaychou.konginterview.mapper.QuestionBankQuestionMapper;
import com.jaychou.konginterview.model.dto.QuestionBankQuestion.QuestionBankQuestionQueryRequest;
import com.jaychou.konginterview.model.entity.QuestionBankQuestion;
import com.jaychou.konginterview.model.entity.User;
import com.jaychou.konginterview.model.vo.QuestionBankQuestionVO;
import com.jaychou.konginterview.model.vo.UserVO;
import com.jaychou.konginterview.service.QuestionBankQuestionService;
import com.jaychou.konginterview.service.UserService;
import com.jaychou.konginterview.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题库题目关联服务实现
 *
 * @author <a href="https://github.com/KongJay">红模仿</a>
 * @from <a href="https://www.hongmofang.top">KongのBlog</a>
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements QuestionBankQuestionService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param QuestionBankQuestion
     * @param add                  对创建的数据进行校验
     */
    @Override
    public void validQuestionBankQuestion(QuestionBankQuestion QuestionBankQuestion, boolean add) {
/*        ThrowUtils.throwIf(QuestionBankQuestion == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = QuestionBankQuestion.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }*/
    }

    /**
     * 获取查询条件
     *
     * @param QuestionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest QuestionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (QuestionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = QuestionBankQuestionQueryRequest.getId();
        Long notId = QuestionBankQuestionQueryRequest.getNotId();
        String sortField = QuestionBankQuestionQueryRequest.getSortField();
        String sortOrder = QuestionBankQuestionQueryRequest.getSortOrder();
        Long questionBankId = QuestionBankQuestionQueryRequest.getQuestionBankId();
        Long questionId = QuestionBankQuestionQueryRequest.getQuestionId();
        Long userId = QuestionBankQuestionQueryRequest.getUserId();
        int current = QuestionBankQuestionQueryRequest.getCurrent();
        int pageSize = QuestionBankQuestionQueryRequest.getPageSize();
        // todo 补充需要的查询条件
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 获取题库题目关联封装
     *
     * @param QuestionBankQuestion
     * @param request
     * @return
     */
    @Override
    public QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion QuestionBankQuestion, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankQuestionVO questionBankQuestion_VO = QuestionBankQuestionVO.objToVo(QuestionBankQuestion);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = QuestionBankQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankQuestion_VO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long QuestionBankQuestionId = QuestionBankQuestion.getId();
        User loginUser = userService.getLoginUserPermitNull(request);

        // endregion

        return questionBankQuestion_VO;
    }

    /**
     * 分页获取题库题目关联封装
     *
     * @param QuestionBankQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> QuestionBankQuestionPage, HttpServletRequest request) {
        List<QuestionBankQuestion> QuestionBankQuestionList = QuestionBankQuestionPage.getRecords();
        Page<QuestionBankQuestionVO> QuestionBankQuestionVOPage = new Page<>(QuestionBankQuestionPage.getCurrent(), QuestionBankQuestionPage.getSize(), QuestionBankQuestionPage.getTotal());
        if (CollUtil.isEmpty(QuestionBankQuestionList)) {
            return QuestionBankQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankQuestionVO> questionBankQuestion_VOList = QuestionBankQuestionList.stream().map(QuestionBankQuestion -> {
            return QuestionBankQuestionVO.objToVo(QuestionBankQuestion);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = QuestionBankQuestionList.stream().map(QuestionBankQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> QuestionBankQuestionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> QuestionBankQuestionIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);

        // 填充信息
        questionBankQuestion_VOList.forEach(questionBankQuestion_VO -> {
            Long userId = questionBankQuestion_VO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankQuestion_VO.setUser(userService.getUserVO(user));

        });
        // endregion

        QuestionBankQuestionVOPage.setRecords(questionBankQuestion_VOList);
        return QuestionBankQuestionVOPage;
    }

}
