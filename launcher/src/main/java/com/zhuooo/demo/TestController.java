package com.zhuooo.demo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "资产管理模板")
@RestController
@RequestMapping("/asset/template")
public class TestController {

    private Map<String, List<DemoPojo>> map = new HashMap<>();

    @ApiOperation(value = "查询模板节点")
    @GetMapping(value = "/child/get")
    public ZhuoooResponse<List<DemoPojo>> getChildren(@ApiParam(name = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id");
        }
        List<DemoPojo> list = map.computeIfAbsent(id, key -> new ArrayList<>());
        return ZhuoooResponse.success(list);
    }


    @ApiOperation(value = "创建一个资产的模板")
    @PutMapping(value = "/add")
    public ZhuoooResponse<String> add(@RequestBody DemoPojo template) {
        if (template == null) {
            throw new RuntimeException("template");
        }
        try {
            TimeUnit.SECONDS.sleep(5);
            template.setId(UuidUtils.generateUuid());
            List<DemoPojo> list = map.computeIfAbsent(template.getParentId(), key -> new ArrayList<>());
            list.add(template);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String id = template.getId();
        return ZhuoooResponse.success(id);
    }
}
