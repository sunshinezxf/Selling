package common.sunshine.mybatis.handler.event;

import common.sunshine.model.selling.event.support.ChoiceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunshine on 8/26/16.
 */
public class ChoiceTypeHandler extends BaseTypeHandler<ChoiceType> {

    private Class<ChoiceType> type;

    private final ChoiceType[] enums;

    public ChoiceTypeHandler(Class<ChoiceType> type) {
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ChoiceType choiceType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, choiceType.getCode());
    }

    @Override
    public ChoiceType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public ChoiceType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public ChoiceType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private ChoiceType locateEnumStatus(int code) {
        for (ChoiceType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}
