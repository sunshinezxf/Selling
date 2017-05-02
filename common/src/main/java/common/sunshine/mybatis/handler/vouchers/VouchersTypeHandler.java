package common.sunshine.mybatis.handler.vouchers;

import common.sunshine.model.selling.vouchers.support.VouchersType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by wxd on 2017/5/2.
 */
public class VouchersTypeHandler extends BaseTypeHandler<VouchersType> {

    private Class<VouchersType> type;

    private final VouchersType[] enums;

    private Logger logger = LoggerFactory.getLogger(VouchersTypeHandler.class);

    public VouchersTypeHandler(Class<VouchersType> type) {
        if (type == null) {
            throw new IllegalArgumentException("参数status不能为空");
        }
        this.type = type;
        enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "不是一个枚举类型");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, VouchersType vouchersType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, vouchersType.getCode());
    }

    @Override
    public VouchersType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public VouchersType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public VouchersType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private VouchersType locateEnumStatus(int code) {
        for (VouchersType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}
