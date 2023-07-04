package com.zhuooo.workflow.controller;

import com.zhuooo.response.ZhuoooResponse;
import com.zhuooo.workflow.pojo.db.WFTemplatePojo;
import com.zhuooo.workflow.service.WFTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "工作流模板管理")
@RestController
@RequestMapping("/workflow/template")
public class WFTemplateController {

    @Autowired
    private WFTemplateService wfTemplateService;

    @ApiOperation(value = "保存一个流程模板")
    @PostMapping(value = "/save")
    public ZhuoooResponse<String> save(@RequestBody WFTemplatePojo template) {
        //todo check template
        return ZhuoooResponse.success(wfTemplateService.save(template));
    }

    @ApiOperation(value = "查询流程模板列表")
    @GetMapping(value = "/list")
    public ZhuoooResponse<List<WFTemplatePojo>> list() {
        return ZhuoooResponse.success(wfTemplateService.list());
    }

    @ApiOperation(value = "查询流程模板详情")
    @GetMapping(value = "/query")
    public ZhuoooResponse<WFTemplatePojo> queryById(String id) {
        //todo check id
        return ZhuoooResponse.success(wfTemplateService.queryById(id));
    }

//    @ApiOperation(value = "查询流程模板详情")
//    @GetMapping(value = "/list")
//    public WFTemplatePojo queryByKey(String templateKey) {
//        //todo check templateKey
//        return wfTemplateService.queryByKey(templateKey);
//    }
}
