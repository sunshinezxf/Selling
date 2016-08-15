package selling.sunshine.handler.mybatis.cashback;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import selling.sunshine.model.cashback.CashBackLevel;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunshine on 8/10/16.
 */
public class LevelHandler extends BaseTypeHandler<CashBackLevel> {
    private Class<CashBackLevel> type;
    private final CashBackLevel[] enums;

    public LevelHandler(Class<CashBackLevel> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CashBackLevel billStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, billStatus.getCode());
    }

    @Override
    public CashBackLevel getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);

        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(i);
        }
    }

    @Override
    public CashBackLevel getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    @Override
    public CashBackLevel getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    private CashBackLevel locateEnumStatus(int code) {
        for (CashBackLevel status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + code + ",请核对" + type.getSimpleName());
    }
}
