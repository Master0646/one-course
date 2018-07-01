package onecourse.dao;

import javafx.util.Pair;
import onecourse.utils.DBHelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseDAO<T> {

    private Class<T> EntityClass; // 获取实体类


    @SuppressWarnings("unchecked")
    public BaseDAO() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        EntityClass = (Class<T>) type.getActualTypeArguments()[0];
    }


    public int add(T t) throws Exception {
        // 获得一个形如 INSERT INTO table_name(col1, col2, ...) VALUES (?, ?, ...)的SQL语句，col列中不含id列
        String sql = this.getInsertSql();
        PreparedStatement statement = null;
        try {
            Pair<Object[], Object> colValue = this.getFieldsValue(t, "id");
            if (colValue == null) throw new Exception();
            statement = DBHelper.setPreparedStatementParam(DBHelper.getPreparedStatement(sql), colValue.getKey());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else throw new SQLException();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBHelper.release(statement, null);
        }
    }

    public <VType> void delete(String byColumn, VType byValue) throws Exception {
        // 获得一个形如 DELETE FROM table_name WHERE byColumn=byValue的SQL语句
        // Note：如果VType是String，则需要用单引号环绕byValue；如果是数值，则不需要加单引号
        String sql = this.getDeleteSql(byColumn);
        PreparedStatement statement = null;
        try {
            Object[] columnValue = new Object[1];
            columnValue[0] = byValue;
            statement = DBHelper.setPreparedStatementParam(DBHelper.getPreparedStatement(sql), columnValue);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBHelper.release(statement, null);
        }
    }


    public <VType> void update(T t, String byColumn, VType byValue) throws Exception {
        // 获得一个形如 UPDATE table_name SET col1=?,col2=?,... WHERE byColumn=byValue的SQL语句
        String sql = this.getUpdateSql(byColumn);
        PreparedStatement statement = null;
        try {
            Pair<Object[], Object> colValue = this.getFieldsValue(t, byColumn);
            if (colValue == null) throw new Exception();
            Object[] newColValue = Arrays.copyOf(colValue.getKey(), colValue.getKey().length + 1);
            newColValue[newColValue.length - 1] = byValue;  // 将byColumn放在最后
            statement = DBHelper.setPreparedStatementParam(DBHelper.getPreparedStatement(sql), newColValue);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBHelper.release(statement, null);
        }
    }


    public <VType> ArrayList<T> select(String byColumn, VType byValue) throws Exception {
        String sql = this.getSelectSql(byColumn);
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<T> objs = new ArrayList<>();
        try {
            Object[] columnValue = new Object[1];
            columnValue[0] = byValue;
            statement = DBHelper.setPreparedStatementParam(DBHelper.getPreparedStatement(sql), columnValue);
            rs = statement.executeQuery();
            Field fields[] = EntityClass.getDeclaredFields();
            getObjFromRs(rs, objs, fields);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBHelper.release(statement, rs);
        }
        return objs;
    }

    private void getObjFromRs(ResultSet rs, ArrayList<T> objs, Field[] fields) throws SQLException, InstantiationException, IllegalAccessException {
        while (rs.next()) {
            T obj = EntityClass.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(obj, rs.getObject(field.getName()));
            }
            objs.add(obj);
        }
    }

    public <VType> ArrayList<T> selectMulti(String byColumn, ArrayList<VType> byValueSet) throws Exception {
        String sql = this.getSelectMultiSql(byColumn, byValueSet.size());
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<T> objs = new ArrayList<>();
        try {
            statement = DBHelper.setPreparedStatementParam(DBHelper.getPreparedStatement(sql), byValueSet.toArray());
            rs = statement.executeQuery();
            Field fields[] = EntityClass.getDeclaredFields();
            getObjFromRs(rs, (ArrayList<T>) objs, fields);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBHelper.release(statement, rs);
        }
        return objs;
    }

    // 返回插入语句
    private String getInsertSql() {
        StringBuilder sql = new StringBuilder();
        Field fields[] = EntityClass.getDeclaredFields();
        sql.append("INSERT INTO ").append(EntityClass.getSimpleName()).append("(");
        for (int i = 0; fields != null && i < fields.length; i++) {
            fields[i].setAccessible(true);    //这句话必须要有,否则会抛出异常.
            String column = fields[i].getName();
            if (!column.equals("id"))
                sql.append(column).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES (");
        for (int i = 0; fields != null && i < fields.length - 1; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        return sql.toString();
    }

    private String getUpdateSql(String byColumn) {
        StringBuilder sql = new StringBuilder();
        Field fields[] = EntityClass.getDeclaredFields();
        sql.append("UPDATE ").append(EntityClass.getSimpleName()).append(" SET ");
        for (int i = 0; fields != null && i < fields.length; i++) {
            fields[i].setAccessible(true);
            String column = fields[i].getName();
            if (column.equals("id")) continue;
            sql.append(column).append("=").append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(String.format(" WHERE %s=?", byColumn));

        return sql.toString();
    }

    private String getDeleteSql(String byColumn) {
        return String.format("DELETE FROM %s WHERE %s=?", EntityClass.getSimpleName(), byColumn);
    }

    private String getSelectSql(String byColumn) {
        return String.format("SELECT * FROM %s WHERE %s=?", EntityClass.getSimpleName(), byColumn);
    }

    private String getSelectMultiSql(String byColumn, int length) {
        StringBuilder builder = new StringBuilder(String.format("SELECT * FROM %s WHERE %s in ", EntityClass.getSimpleName(), byColumn));
        builder.append("(");
        for (int i = 0; i < length; i++) {
            builder.append("?,");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }

    private Pair<Object[], Object> getFieldsValue(T entity, String excludeColumn) throws Exception {
        try {
            Field[] fields = EntityClass.getDeclaredFields();
            ArrayList<Object> obj = new ArrayList<>();
            Object excludeObj = null;
            boolean containExcludeCol = false;
            if (excludeColumn == null) containExcludeCol = true;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(excludeColumn)) {
                    excludeObj = field.get(entity);
                    containExcludeCol = true;
                } else obj.add(field.get(entity));
            }
            if (!containExcludeCol)
                throw new Exception(String.format("Do not have the column named '%s'", excludeColumn));
            return new Pair<Object[], Object>(obj.toArray(), excludeObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
