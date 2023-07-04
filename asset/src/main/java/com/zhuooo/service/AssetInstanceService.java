package com.zhuooo.service;

import com.zhuooo.pojo.vo.AssetInstanceVo;

import java.util.List;

public interface AssetInstanceService {

    String add(AssetInstanceVo<String, String> instanceVo);

    AssetInstanceVo get(String id);

    List<AssetInstanceVo> list(String templateId);
}
