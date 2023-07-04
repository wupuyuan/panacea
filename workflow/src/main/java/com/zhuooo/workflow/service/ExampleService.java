package com.zhuooo.workflow.service;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.constant.RuleEnum;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.pojo.vo.UserVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 仅做演示时使用
 */
@Service
public class ExampleService {


    /**
     * 根据登录信息查询登录的用户
     * @param token
     * @return
     */
    public UserVo findLoginUser(String token) {
        switch (token) {
            case "11111":
                return new UserVo("11111", "老大");
            case "22222":
                return new UserVo("22222", "老二");
            case "33333":
                return new UserVo("33333", "张三");
            case "44444":
                return new UserVo("44444", "李四");
            case "55555":
                return new UserVo("55555", "王五");
            case "66666":
                return new UserVo("66666", "老六");
            default:
                throw new ZhuoooException(ReturnCode.LOGIN_USER);
        }
    }

    /**
     * 根据用户id查询
     * @param id
     * @return
     */
    public UserVo findUserById(String id) {
        return findLoginUser(id);
    }

    public List<UserVo> queryUsers(String ruleCode, UserVo user) {
        RuleEnum rule = RuleEnum.getRule(ruleCode);

        switch (rule) {
            case LEADER:
                return Collections.singletonList(new UserVo("44444", "李四"));
            case MANAGER:
                return Arrays.asList(new UserVo("55555", "王五"), new UserVo("66666", "老六"));
            case SUBMITTER:
                return Collections.singletonList(new UserVo("33333", "张三"));
            case ACCOUNTING:
                return Arrays.asList(new UserVo("11111", "老大"), new UserVo("22222", "老二"));
            default:
                throw new ZhuoooException(ReturnCode.RULE_EMPTY);
        }
    }
}
