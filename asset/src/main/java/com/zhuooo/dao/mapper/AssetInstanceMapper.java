package com.zhuooo.dao.mapper;

import com.zhuooo.pojo.db.AssetInstancePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AssetInstanceMapper {

    @Select("select * from asset_instance where template_id = #{templateId}")
    List<AssetInstancePojo> selectByTemplateId(@Param("templateId") String templateId);
}
