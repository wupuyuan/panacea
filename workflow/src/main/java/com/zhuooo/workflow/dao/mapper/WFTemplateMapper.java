package com.zhuooo.workflow.dao.mapper;

import com.zhuooo.workflow.pojo.db.WFTemplatePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WFTemplateMapper {

    @Select("<script>" +
            "select * from wf_template where deleted = 0 \n" +
            "<if test='key != null'>" +
            "   and template_key = #{key} " +
            "</if>\n" +
            "<if test='type != null'>" +
            "   and type = #{type} " +
            "</if>\n" +
            "order by create_time desc" +
            "</script>")
    List<WFTemplatePojo> list(@Param("key") String key, @Param("type") Integer type);


    @Update("update wf_template set update_time=now(), name=#{name}, version=#{version}, type=#{type}, description=#{description}, update_person=#{updatePerson}" +
            "where id = #{id} ")
    int update(WFTemplatePojo template);
}
