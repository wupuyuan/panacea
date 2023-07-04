package com.zhuooo.jdbc.dao;

import com.zhuooo.jdbc.JdbcCache;
import com.zhuooo.jdbc.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseCacheDao<T extends BasePojo> extends BaseDao<T> {

    public boolean enableCache;

    private final String PRE_GROUP = "group_";
    private final String PRE_PARENT = "parent_";

    public T selectOne(String id) {
        check(id);
        T ret = getOne(id);
        if (ret == null) {
            ret = super.selectOne(id);
            if (ret != null) {
                setOne(ret);
            }
        }
        return ret;
    }

    public List<T> selectGroup(String groupId) {
        check(groupId);
        List<T> ret = getGroup(groupId);
        if (ret == null) {
            ret = loadGroup(groupId);
        }
        return ret;
    }

    public List<T> loadGroup(String groupId) {
        check(groupId);
        List<T> ret = super.selectGroup(groupId);
        setGroup(groupId, ret);
        for (T item : ret) {
            setOne(item);
        }
        return ret;
    }


    public List<T> selectChildren(String parenId) {
        check(parenId);
        List<T> ret = getChildren(parenId);
        if (ret == null) {
            ret = loadChildren(parenId);
        }
        return ret;
    }

    public List<T> loadChildren(String parenId) {
        check(parenId);
        List<T> ret = super.selectChildren(parenId);
        setChildren(parenId, ret);
        for (T item : ret) {
            setOne(item);
        }
        return ret;
    }

    private void setOne(T obj) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        map.put(obj.getId(), obj);
    }

    private T getOne(String id) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        return (T) map.get(id);
    }

    private void setGroup(String id, List<T> list) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        map.put(PRE_GROUP + id, list);
    }

    private List<T> getGroup(String id) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        return (List<T>) map.get(PRE_GROUP + id);
    }

    private void setChildren(String id, List<T> list) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        map.put(PRE_PARENT + id, list);
    }

    private List<T> getChildren(String id) {
        ConcurrentHashMap<String, Object> map = getCacheMap();
        return (List<T>) map.get(PRE_PARENT + id);
    }

    private ConcurrentHashMap<String, Object> getCacheMap() {
        String clazz = this.getClass().getName();
        ConcurrentHashMap<String, Object> map = JdbcCache.get(clazz);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            JdbcCache.put(clazz, map);
        }
        return map;
    }

    public void loadCache() {
        System.out.println("load cache for " + this.getClass().getName());
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        List<T> list = selectAll();
        for (T obj : list) {
            // select one
            map.put(obj.getId(), obj);

            // select group
            {
                String groupId = getBaseMapperStatement().getGroupId(obj);
                if (groupId != null) {
                    List<T> values = (List<T>) map.computeIfAbsent(PRE_GROUP + groupId, key -> new ArrayList<>());
                    values.add(obj);
                }
            }

            // select children
            {
                String parentId = getBaseMapperStatement().getParentId(obj);
                if (parentId != null) {
                    List<T> values = (List<T>) map.computeIfAbsent(PRE_PARENT + parentId, key -> new ArrayList<>());
                    values.add(obj);
                }
            }

        }
        JdbcCache.put(this.getClass().getName(), map);
    }
}
