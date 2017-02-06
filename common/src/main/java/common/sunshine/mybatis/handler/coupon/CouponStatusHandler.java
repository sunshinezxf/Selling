package common.sunshine.mybatis.handler.coupon;

import common.sunshine.model.selling.coupon.support.CouponStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunshine on 2017/1/27.
 */
public class CouponStatusHandler extends BaseTypeHandler<CouponStatus> {
    private Class<CouponStatus> status;

    private final CouponStatus[] enums;

    private Logger logger = LoggerFactory.getLogger(CouponStatusHandler.class);

    public CouponStatusHandler(Class<CouponStatus> status) {
        if (status == null) {
            throw new IllegalArgumentException("参数status不能为空");
        }
        this.status = status;
        enums = status.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(status.getSimpleName() + "不是一个枚举类型");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CouponStatus couponStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, couponStatus.getCode());
    }

    @Override
    public CouponStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public CouponStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public CouponStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private CouponStatus locateEnumStatus(int code) {
        for (CouponStatus status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.status.getSimpleName());
    }
}

