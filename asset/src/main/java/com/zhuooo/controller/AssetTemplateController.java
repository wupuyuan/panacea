package com.zhuooo.controller;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;
import com.zhuooo.pojo.db.AssetParameterPojo;
import com.zhuooo.pojo.db.AssetTemplatePojo;
import com.zhuooo.pojo.vo.AssetTemplateVo;
import com.zhuooo.response.ZhuoooResponse;
import com.zhuooo.service.AssetTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "资产管理模板")
@RestController
@RequestMapping("/asset/template")
public class AssetTemplateController extends  BaseController{

    @Autowired
    private AssetTemplateService assetTemplateService;


    @ApiOperation(value = "创建一个资产的模板")
    @PutMapping(value = "/add")
    public ZhuoooResponse<String> add(@RequestBody AssetTemplatePojo template) {

        if (template == null) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "template");
        }
        this.setOperator(template);
        String id = assetTemplateService.add(template);
        return ZhuoooResponse.success(id);
    }

    @ApiOperation(value = "查询模板信息")
    @GetMapping(value = "/get")
    public ZhuoooResponse<AssetTemplateVo> get(@ApiParam(name = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "id");
        }
        AssetTemplateVo vo = assetTemplateService.get(id);
        return ZhuoooResponse.success(vo);
    }

    @ApiOperation(value = "查询模板节点")
    @GetMapping(value = "/child/get")
    public ZhuoooResponse<List<AssetTemplatePojo>> getChildren(@ApiParam(name = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "id");
        }
        List<AssetTemplatePojo> list = assetTemplateService.getChildren(id);
        return ZhuoooResponse.success(list);
    }

    @ApiOperation(value = "为资产模板添加一个属性")
    @PutMapping(value = "/parameter/add")
    public ZhuoooResponse addParameter(@RequestBody AssetParameterPojo parameter) {
        if (parameter == null) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "parameter");
        }
        String id = assetTemplateService.addParameter(parameter);
        return ZhuoooResponse.success(id);
    }

    @ApiOperation(value = "删除一个资产的模板的属性")
    @DeleteMapping(value = "/parameter/delete")
    public ZhuoooResponse<String> delete(@ApiParam(name = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "id");
        }
        assetTemplateService.delete(id);
        return ZhuoooResponse.success(id);
    }

}
