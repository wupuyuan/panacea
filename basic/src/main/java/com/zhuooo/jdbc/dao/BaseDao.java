package com.zhuooo.jdbc.dao;

import com.zhuooo.jdbc.JdbcCache;
import com.zhuooo.constant.ReturnCode;
import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.jdbc.pojo.BaseOperationPojo;
import com.zhuooo.jdbc.pojo.BasePojo;
import com.zhuooo.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T extends BasePojo> {

    @Autowired
    private DataSource dataSource;

    private static final String TYPE_STRING = "java.lang.String";
    private static final String TYPE_INT = "int";
    private static final String TYPE_OBJ_INTEGER = "java.lang.Integer";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_OBJ_LONG = "java.lang.Long";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_OBJ_DOUBLE = "java.lang.Double";
    private static final String TYPE_TIMESTAMP = "java.sql.Timestamp";

    public int insert(T obj) {
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        int row;
        try {
            setDefault(obj);
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getInsertSql());
            setPreparedStatement(baseMapperStatement, ps, obj);
            row = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(ps);
            close(conn);
        }

        return row;
    }

    public int insert(List<T> list) {
        check(list);
        int row = 0;

        if (list.size() == 0) {
            return row;
        }

        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getInsertSql());
            for (T obj : list) {
                setDefault(obj);
                setPreparedStatement(baseMapperStatement, ps, obj);
                ps.addBatch();
            }

            int[] ii = ps.executeBatch();
            for (int i : ii) {
                row += i;
            }
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(ps);
            close(conn);
        }

        return row;
    }

    public T selectOne(String id) {
        check(id);
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        T ret = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getSelectOneSql());
            ps.setString(1, id);
            rs = ps.executeQuery();
            List<T> list = getResult(baseMapperStatement, rs);

            if (list.size() > 1) {
                throw new ZhuoooException(ReturnCode.BASE_DAO_DUPLICATE, "duplicate data in table " + baseMapperStatement.getClassName() + " on id = " + id);
            }

            if (list.size() == 1) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return ret;
    }

    public List<T> selectGroup(String groupId) {
        check(groupId);
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getSelectGroupSql());
            ps.setString(1, groupId);
            rs = ps.executeQuery();
            list = getResult(baseMapperStatement, rs);
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return list;
    }

    public List<T> selectChildren(String parenId) {
        check(parenId);
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getSelectChildrenSql());
            ps.setString(1, parenId);
            rs = ps.executeQuery();
            list = getResult(baseMapperStatement, rs);
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return list;
    }

    public List<T> selectAll() {
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<T> list;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            st = conn.createStatement();
            rs = st.executeQuery(baseMapperStatement.getSelectAllSql());
            list = getResult(baseMapperStatement, rs);
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(rs);
            close(st);
            close(conn);
        }
        return list;
    }

    public int delete(String id) {
        check(id);
        int row = 0;
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getDeleteOneSql());
            ps.setString(1, id);
            row = ps.executeUpdate();
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(ps);
            close(conn);
        }

        return row;
    }

    public int deleteGroup(String id) {
        check(id);
        int row = 0;
        BaseDaoStatement baseMapperStatement = getBaseMapperStatement();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            ps = conn.prepareStatement(baseMapperStatement.getDeleteGroupSql());
            ps.setString(1, id);
            row = ps.executeUpdate();
        } catch (Exception e) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_EXECUTE, e);
        } finally {
            close(ps);
            close(conn);
        }

        return row;
    }

    protected void check(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_PRARM);
        }
    }

    protected void check(List<T> obj) {
        if (obj == null) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_PRARM);
        }
    }

    protected void setDefault(T obj) {
        if (obj.getId() == null) {
            obj.setId(UuidUtils.generateUuid());
        }

        if (obj instanceof BaseOperationPojo) {
            BaseOperationPojo temp = (BaseOperationPojo) obj;
            if (temp.getCreateTime() == null) {
                temp.setCreateTime(new Timestamp(System.currentTimeMillis()));
            }
            if (temp.getUpdateTime() == null) {
                temp.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            }
        }
    }

    private void setPreparedStatement(BaseDaoStatement baseMapperStatement, PreparedStatement ps, Object obj) throws IllegalAccessException, SQLException {
        for (int i = 0; i < baseMapperStatement.getMappedItems().size(); i++) {
            BaseDaoMappedItem item = baseMapperStatement.getMappedItems().get(i);
            switch (item.getField().getType().getName()) {
                case TYPE_STRING:
                    ps.setString((i + 1), (String) item.getField().get(obj));
                    break;
                case TYPE_INT:
                    ps.setInt((i + 1), item.getField().getInt(obj));
                    break;
                case TYPE_OBJ_INTEGER:
                    Integer intObj = (Integer) item.getField().get(obj);
                    ps.setInt((i + 1), intObj == null ? null : intObj.intValue());
                    break;
                case TYPE_LONG:
                    ps.setLong((i + 1), item.getField().getLong(obj));
                    break;
                case TYPE_OBJ_LONG:
                    Long longObj = (Long) item.getField().get(obj);
                    ps.setLong((i + 1), longObj == null ? null : longObj.longValue());
                    break;
                case TYPE_DOUBLE:
                    ps.setDouble((i + 1), item.getField().getDouble(obj));
                    break;
                case TYPE_OBJ_DOUBLE:
                    Double doubleObj = (Double) item.getField().get(obj);
                    ps.setDouble((i + 1), doubleObj == null ? null : doubleObj.doubleValue());
                    break;
                case TYPE_TIMESTAMP:
                    ps.setTimestamp((i + 1), (Timestamp) item.getField().get(obj));
                    break;
                default:
                    throw new ZhuoooException(ReturnCode.BASE_DAO_STATEMENT, "Unknown field type: " + item.getField().getType().getName());
            }
        }
    }

    private List<T> getResult(BaseDaoStatement baseMapperStatement, ResultSet rs) throws IllegalAccessException, SQLException, InstantiationException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T obj = (T) baseMapperStatement.getClazz().newInstance();
            for (int i = 0; i < baseMapperStatement.getMappedItems().size(); i++) {
                BaseDaoMappedItem item = baseMapperStatement.getMappedItems().get(i);
                switch (item.getField().getType().getName()) {
                    case TYPE_STRING:
                        item.getField().set(obj, rs.getString(item.getColumnName()));
                        break;
                    case TYPE_INT:
                        item.getField().setInt(obj, rs.getInt(item.getColumnName()));
                        break;
                    case TYPE_OBJ_INTEGER:
                        item.getField().set(obj, Integer.valueOf(rs.getInt(item.getColumnName())));
                        break;
                    case TYPE_LONG:
                        item.getField().setLong(obj, rs.getLong(item.getColumnName()));
                        break;
                    case TYPE_OBJ_LONG:
                        item.getField().setLong(obj, Long.valueOf(rs.getLong(item.getColumnName())));
                        break;
                    case TYPE_DOUBLE:
                        item.getField().setDouble(obj, rs.getDouble(item.getColumnName()));
                        break;
                    case TYPE_OBJ_DOUBLE:
                        item.getField().setDouble(obj, Double.valueOf(rs.getDouble(item.getColumnName())));
                        break;
                    case TYPE_TIMESTAMP:
                        item.getField().set(obj, rs.getTimestamp(item.getColumnName()));
                        break;
                    default:
                        throw new ZhuoooException(ReturnCode.BASE_DAO_STATEMENT, "Unknown field type: " + item.getField().getType().getName());
                }
            }
            list.add(obj);
        }
        return list;
    }

    protected BaseDaoStatement getBaseMapperStatement() {
        Type type = this.getClass().getGenericSuperclass();
        String className = ((ParameterizedTypeImpl) type).getActualTypeArguments()[0].getTypeName();
        BaseDaoStatement baseMapperStatement = JdbcCache.get(className);
        if (baseMapperStatement == null) {
            throw new ZhuoooException(ReturnCode.BASE_DAO_STATEMENT, "Can not find BaseDaoStatement for " + className);
        }
        return baseMapperStatement;
    }

    private void close(Connection conn) {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // todo
            }
        }
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                // todo
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // todo
            }
        }
    }
}
