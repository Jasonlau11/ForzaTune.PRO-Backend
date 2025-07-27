package com.forzatune.backend.handler;

import com.forzatune.backend.entity.Car;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 车辆分类枚举类型处理器
 * 处理Java枚举与数据库字符串值之间的转换
 */
@MappedTypes(Car.CarCategory.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class CarCategoryTypeHandler extends BaseTypeHandler<Car.CarCategory> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Car.CarCategory parameter, JdbcType jdbcType) throws SQLException {
        // Java枚举转换为数据库字符串值
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Car.CarCategory getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : Car.CarCategory.fromValue(value);
    }

    @Override
    public Car.CarCategory getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : Car.CarCategory.fromValue(value);
    }

    @Override
    public Car.CarCategory getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : Car.CarCategory.fromValue(value);
    }
} 