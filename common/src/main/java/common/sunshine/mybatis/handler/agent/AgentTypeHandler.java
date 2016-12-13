package common.sunshine.mybatis.handler.agent;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import common.sunshine.model.selling.agent.support.AgentType;

public class AgentTypeHandler extends BaseTypeHandler<AgentType>{
    private Class<AgentType> type;

    private final AgentType[] enums;

    public AgentTypeHandler(Class<AgentType> type) {
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, AgentType agentType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, agentType.getCode());
    }

    @Override
    public AgentType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public AgentType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public AgentType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private AgentType locateEnumStatus(int code) {
        for (AgentType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}
