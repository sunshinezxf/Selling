package selling.sunshine.handler.mybatis.order;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import selling.sunshine.model.OrderType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunshine on 7/4/16.
 */
public class OrderTypeHandler extends BaseTypeHandler<OrderType> {
    private Class<OrderType> type;
    private final OrderType[] enums;

    public OrderTypeHandler(Class<OrderType> type) {
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, OrderType orderItemStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, orderItemStatus.getCode());
    }

    @Override
    public OrderType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);

        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(i);
        }
    }

    @Override
    public OrderType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    @Override
    public OrderType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    private OrderType locateEnumStatus(int code) {
        for (OrderType status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + code + ",请核对" + type.getSimpleName());
    }
}
