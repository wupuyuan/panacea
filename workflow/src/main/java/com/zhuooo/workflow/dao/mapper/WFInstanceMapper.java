package com.zhuooo.workflow.dao.mapper;

import com.zhuooo.workflow.pojo.db.WFInstancePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface WFInstanceMapper {

    @Select("select * from wf_instance where template_id = #{templateId} and instance_key=#{instanceKey}")
    WFInstancePojo selectByInstanceKey(@Param("templateId") String templateId, @Param("instanceKey") String instanceKey);

    @Select("<script>" +
            "select * from wf_instance where id in (" +
            "  select instance_id from wf_argument where argument_value = #{argumentValue} and argument_key in " +
            "  <foreach item='item' index='index' collection='argumentKeys' open='(' separator=',' close=')'>" +
            "    #{item} " +
            "  </foreach>" +
            ") " +
            "<if test='status != null and status.size() > 0'>" +
            "  and status in " +
            "  <foreach item='item' index='index' collection='status' open='(' separator=',' close=')'>" +
            "    #{item}" +
            "  </foreach>" +
            "</if>" +
            "</script>")
    List<WFInstancePojo> selectByArgument(@Param("argumentKeys") List<String> argumentKeys, @Param("argumentValue") String argumentValue, @Param("status") List<Integer> status);

    @Update("update wf_instance set status = #{status}, update_time=now(), update_person = #{updatePerson}, description=#{description} where id=#{id}")
    int update(WFInstancePojo instancePojo);
}

