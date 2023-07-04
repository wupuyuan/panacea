package com.zhuooo.service.impl;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.dao.AssetArgumentDao;
import com.zhuooo.dao.AssetInstanceDao;
import com.zhuooo.dao.AssetTemplateDao;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.pojo.db.AssetArgumentPojo;
import com.zhuooo.pojo.db.AssetInstancePojo;
import com.zhuooo.pojo.db.AssetParameterPojo;
import com.zhuooo.pojo.vo.AssetInstanceVo;
import com.zhuooo.response.ZhuoooFieldValue;
import com.zhuooo.service.AssetInstanceService;
import com.zhuooo.service.AssetTemplateService;
import com.zhuooo.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssetInstanceServiceImpl implements AssetInstanceService {

    @Autowired
    private AssetTemplateService templateService;

    @Autowired
    private AssetInstanceDao instanceDao;

    @Autowired
    private AssetArgumentDao argumentDao;

    @Autowired
    private AssetTemplateDao templateDao;

    @Autowired
    private AssetTemplateService assetTemplateService;

    @Override
    public String add(AssetInstanceVo<String, String> instanceVo) {

        String id = UuidUtils.generateUuid();
        Set<String> set = new HashSet<>();
        AssetInstancePojo instance = instanceVo.getInstance();

        instance.setId(id);

        List<AssetArgumentPojo> arguments = new ArrayList<>();
        for (Map.Entry<String, String> entry : instanceVo.getValues().entrySet()) {
            if (StringUtils.isEmpty(entry.getKey()) || StringUtils.isEmpty(entry.getValue())) {
                throw new ZhuoooException(ReturnCode.PARAM_ILLEGALITY);
            }

            set.add(entry.getKey());

            AssetArgumentPojo argument = new AssetArgumentPojo();
            argument.setInstanceId(id);
            argument.setTemplateId(instance.getTemplateId());
            argument.setParameterId(entry.getKey());
            argument.setValue(entry.getValue());
            arguments.add(argument);
        }

        List<AssetParameterPojo> parameters = templateService.getParameters(instance.getTemplateId());
        for (AssetParameterPojo parameter : parameters) {
            if (parameter.getRequired() == 1 && !set.contains(parameter.getId())) {
                throw new ZhuoooException(ReturnCode.PARAM_REQUIRED);
            }
        }

        instanceDao.insert(instance);
        argumentDao.insert(arguments);
        return id;
    }

    @Override
    public AssetInstanceVo get(String id) {
        AssetInstancePojo instance = instanceDao.selectOne(id);
        AssetInstanceVo ret = null;
        if (instance != null) {
            ret = new AssetInstanceVo();
            ret.setTemplate(templateDao.selectOne(instance.getTemplateId()));

            List<AssetParameterPojo> parameters = templateService.getParameters(instance.getTemplateId());
            List<AssetArgumentPojo> arguments = argumentDao.selectGroup(instance.getId());
            Map<String, AssetArgumentPojo> map = arguments.stream().collect(Collectors.toMap(AssetArgumentPojo::getParameterId, item -> item));

            List<ZhuoooFieldValue<AssetParameterPojo, AssetArgumentPojo>> list = new ArrayList<>();
            for (AssetParameterPojo parameter : parameters) {
                ZhuoooFieldValue<AssetParameterPojo, AssetArgumentPojo> value = new ZhuoooFieldValue<>();
                value.setParament(parameter);
                value.setArgument(map.get(parameter.getId()));
                list.add(value);
            }

            ret.setValues(arguments.stream().collect(Collectors.toMap(AssetArgumentPojo::getParameterId, AssetArgumentPojo::getValue)));

        }
        return ret;
    }

    @Override
    public List<AssetInstanceVo> list(String templateId) {
        List<AssetInstancePojo> list = instanceDao.selectByTemplateId(templateId);
        List<AssetInstanceVo> ret = new ArrayList<>();
        for (AssetInstancePojo instance : list) {
            AssetInstanceVo vo = new AssetInstanceVo();
            List<AssetArgumentPojo> arguments = argumentDao.selectGroup(instance.getId());
            if (!CollectionUtils.isEmpty(arguments)) {
                List<ZhuoooFieldValue<String, String>> values = new ArrayList<>();
                for (AssetArgumentPojo argument : arguments) {
                    values.add(new ZhuoooFieldValue(argument.getParameterId(), argument.getValue()));
                }
            }
            vo.setInstance(instance);
            vo.setValues(arguments.stream().collect(Collectors.toMap(AssetArgumentPojo::getParameterId, AssetArgumentPojo::getValue)));
            ret.add(vo);
        }
        return ret;
    }
}
