package com.forzatune.backend.handler;

import com.forzatune.backend.entity.Notification;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通知类型枚举类型处理器
 * 处理Java枚举与数据库字符串值之间的转换
 */
@MappedTypes(Notification.NotificationType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class NotificationTypeHandler extends BaseTypeHandler<Notification.NotificationType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Notification.NotificationType parameter, JdbcType jdbcType) throws SQLException {
        // Java枚举转换为数据库字符串值
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Notification.NotificationType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : Notification.NotificationType.fromCode(value);
    }

    @Override
    public Notification.NotificationType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : Notification.NotificationType.fromCode(value);
    }

    @Override
    public Notification.NotificationType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : Notification.NotificationType.fromCode(value);
    }
}
