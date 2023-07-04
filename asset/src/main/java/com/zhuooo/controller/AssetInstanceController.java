package com.zhuooo.controller;


import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.pojo.vo.AssetInstanceVo;
import com.zhuooo.response.ZhuoooResponse;
import com.zhuooo.service.AssetInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "资产管理实例")
@RestController
@RequestMapping("/asset/instance")
public class AssetInstanceController  extends BaseController{

    @Autowired
    private AssetInstanceService assetInstanceService;

    @ApiOperation(value = "创建一个资产实例")
    @PutMapping(value = "/add")
    public ZhuoooResponse<String> add(@RequestBody AssetInstanceVo<String, String> instanceVo) {
        if (instanceVo == null) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "instanceVo");
        }
        this.setOperator(instanceVo.getInstance());
        String id = assetInstanceService.add(instanceVo);
        return ZhuoooResponse.success(id);
    }

    @ApiOperation(value = "查询资产实例详情")
    @GetMapping(value = "/get")
    public ZhuoooResponse<AssetInstanceVo> get(@ApiParam(name = "id", required = true) String id) {
        if (id == null) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "id");
        }

        AssetInstanceVo ret = assetInstanceService.get(id);
        return ZhuoooResponse.success(ret);
    }

    @ApiOperation(value = "查询资产实例")
    @GetMapping(value = "/list")
    public ZhuoooResponse<List<AssetInstanceVo>> list(@ApiParam(name = "templateId", required = true) String templateId) {
        if (templateId == null) {
            throw new ZhuoooException(ReturnCode.PARAM_NULL, "templateId");
        }

        List<AssetInstanceVo> ret = assetInstanceService.list(templateId);
        return ZhuoooResponse.success(ret);
    }
}
