package com.zhuooo.workflow.controller;

import com.zhuooo.pojo.vo.UserVo;
import com.zhuooo.response.ZhuoooResponse;
import com.zhuooo.workflow.constant.WFConstantEnum;
import com.zhuooo.workflow.pojo.db.WFInstancePojo;
import com.zhuooo.workflow.pojo.vo.WFRequest;
import com.zhuooo.workflow.service.ExampleService;
import com.zhuooo.workflow.service.WFInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(tags = "工作流实例管理")
@RestController
@RequestMapping("/workflow/instance")
public class WFInstanceController {

    @Autowired
    private WFInstanceService wfInstanceService;

    @Autowired
    private ExampleService exampleService;

    @ApiOperation(value = "保存一个流程 -- 草稿")
    @PostMapping(value = "/save")
    public ZhuoooResponse<String> save(@Param("token") String token, @RequestBody WFRequest request) {
        UserVo user = exampleService.findLoginUser(token);
        WFInstancePojo instance = wfInstanceService.save(user, request);
        return ZhuoooResponse.success(instance.getId());
    }

    @ApiOperation(value = "发起一个流程")
    @PostMapping(value = "/start")
    public ZhuoooResponse<String> start(@Param("token") String token, @RequestBody WFRequest request) {
        UserVo user = exampleService.findLoginUser(token);
        WFInstancePojo instance = wfInstanceService.start(user, request);
        return ZhuoooResponse.success(instance.getId());
    }

    @ApiOperation(value = "同意")
    @PostMapping(value = "/approve")
    public ZhuoooResponse<String> approve(@Param("token") String token, @RequestBody WFRequest request) {
        UserVo user = exampleService.findLoginUser(token);
        wfInstanceService.approve(user, request);
        return ZhuoooResponse.success(null);
    }

    @ApiOperation(value = "驳回")
    @PostMapping(value = "/reject")
    public ZhuoooResponse<String> reject(@Param("token") String token,
                                         @Param("instanceNodeId") String instanceNodeId,
                                         @Param("description") String description) {
        UserVo user = exampleService.findLoginUser(token);
        wfInstanceService.reject(user, instanceNodeId, description);
        return ZhuoooResponse.success(null);
    }

    @ApiOperation(value = "作废")
    @PostMapping(value = "/cancel")
    public ZhuoooResponse<String> cancel(@Param("token") String token,
                                         @Param("instanceNodeId") String instanceNodeId,
                                         @Param("description") String description) {
        UserVo user = exampleService.findLoginUser(token);
        wfInstanceService.cancel(user, instanceNodeId, description);
        return ZhuoooResponse.success(null);
    }

    @ApiOperation(value = "加签")
    @PostMapping(value = "/delegate")
    public ZhuoooResponse<String> delegate(@Param("token") String token,
                                           @Param("userId") String userId,
                                           @Param("instanceNodeId") String instanceNodeId,
                                           @Param("description") String description) {
        UserVo user = exampleService.findLoginUser(token);
        UserVo target = exampleService.findUserById(userId);
        wfInstanceService.delegate(user, target, instanceNodeId, description);
        return ZhuoooResponse.success(null);
    }

    @ApiOperation(value = "委托")
    @PostMapping(value = "/assign")
    public ZhuoooResponse<String> assign(@Param("token") String token,
                                         @Param("userId") String userId,
                                         @Param("instanceNodeId") String instanceNodeId,
                                         @Param("description") String description) {
        UserVo user = exampleService.findLoginUser(token);
        UserVo target = exampleService.findUserById(userId);
        wfInstanceService.assign(user, target, instanceNodeId, description);
        return ZhuoooResponse.success(null);
    }

    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/detail")
    public ZhuoooResponse<WFInstancePojo> detail(@Param("id") String id) {
        return ZhuoooResponse.success(wfInstanceService.detail(id));
    }

    @ApiOperation(value = "查询当前人发起的流程")
    @GetMapping(value = "/application/list")
    public ZhuoooResponse<List<WFInstancePojo>> listApplication(@Param("token") String token, @Param("status") Integer status) {
        UserVo user = exampleService.findLoginUser(token);
        List<Integer> list = new ArrayList<>();
        if (status != null) {
            list.add(status);
        }
        return ZhuoooResponse.success(wfInstanceService.list(user, Collections.singletonList(WFConstantEnum.INITIATOR), list));
    }

    @ApiOperation(value = "查询待当前人审批的流程")
    @GetMapping(value = "/candidate/list")
    public ZhuoooResponse<List<WFInstancePojo>> listApplication(@Param("token") String token) {
        UserVo user = exampleService.findLoginUser(token);
        return ZhuoooResponse.success(wfInstanceService.list(user, Collections.singletonList(WFConstantEnum.CANDIDATE), null));
    }

    @ApiOperation(value = "查询当前人经手的流程(审批、加签、驳回)")
    @GetMapping(value = "/handle/list")
    public ZhuoooResponse<List<WFInstancePojo>> listHandle(@Param("token") String token, @Param("status") Integer status) {
        UserVo user = exampleService.findLoginUser(token);
        List<Integer> list = new ArrayList<>();
        if (status != null) {
            list.add(status);
        }
        return ZhuoooResponse.success(wfInstanceService.list(user, Collections.singletonList(WFConstantEnum.HANDLER), list));
    }

}
