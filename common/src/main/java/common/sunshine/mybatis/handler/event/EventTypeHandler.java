package common.sunshine.mybatis.handler.event;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import common.sunshine.model.selling.event.support.EventType;

public class EventTypeHandler extends BaseTypeHandler<EventType>{

	private Class<EventType> type;

    private final EventType[] enums;

    public EventTypeHandler(Class<EventType> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, EventType eventType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, eventType.getCode());
    }

    @Override
    public EventType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public EventType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public EventType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private EventType locateEnumStatus(int code) {
        for (EventType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}
