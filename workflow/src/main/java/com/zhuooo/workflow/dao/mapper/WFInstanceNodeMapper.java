package com.zhuooo.workflow.dao.mapper;

import com.zhuooo.workflow.pojo.db.WFInstanceNodePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WFInstanceNodeMapper {

    @Select("<script>" +
            "select * from wf_instance_node where deleted = 0 " +
            "<if test='instanceId != null'>" +
            "  and instance_id = #{instanceId} " +
            "</if>" +
            "<if test='templateId != null'>" +
            "  and template_id = #{templateId} " +
            "</if>" +
            "<if test='templateNodeIds != null'>" +
            "  and template_node_id in " +
            "  <foreach item='item' index='index' collection='templateNodeIds' open='(' separator=',' close=')'>" +
            "    #{item}" +
            "  </foreach>" +
            "</if>" +
            "<if test='status != null'>" +
            "  and status in " +
            "  <foreach item='item' index='index' collection='status' open='(' separator=',' close=')'>" +
            "    #{item}" +
            "  </foreach>" +
            "</if>" +
            "order by update_time asc" +
            "</script>")
    List<WFInstanceNodePojo> select(@Param("instanceId") String instanceId, @Param("templateId") String templateId, @Param("templateNodeIds") List<String> templateNodeIds, @Param("status") List<Integer> status);

    @Update("update wf_instance_node set status = #{status}, update_time=now(), update_person = #{updatePerson}, description=#{description} where id=#{id}")
    int update(WFInstanceNodePojo instanceNodePojo);

    @Update("<script>" +
            "update wf_instance_node set status = #{status} " +
            "<if test='deleted != null'>" +
            "  , deleted=#{deleted} " +
            "</if>" +
            "<if test='description != null'>" +
            "  , description=#{description} " +
            "</if>" +
            "  where id in " +
            "  <foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "    #{item}" +
            "  </foreach>" +
            "</script>")
    int updateBatch(@Param("ids") List<String> ids, @Param("status") Integer status, @Param("deleted") Integer deleted, @Param("description") String description);
}
