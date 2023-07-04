package com.zhuooo.workflow.dao.mapper;

import com.zhuooo.workflow.pojo.db.WFArgumentPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WFArgumentMapper {

    @Select("<script>" +
            "select * from wf_argument where instance_id = #{instanceId} " +
            "<if test='instanceNodeId != null'>" +
            "  and instance_node_id = #{instanceNodeId} " +
            "</if>\n" +
            "<if test='key != null'>" +
            "  and argument_key = #{key} " +
            "</if>\n" +
            "</script>")
    List<WFArgumentPojo> select(@Param("instanceId") String instanceId, @Param("instanceNodeId") String instanceNodeId, @Param("key") String key);
}
