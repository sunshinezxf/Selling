package common.sunshine.mybatis.handler.goods;

import common.sunshine.model.selling.goods.support.GoodsType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunshine on 2017/1/26.
 */

/**
 * Created by sunshine on 2016/11/2.
 */
public class GoodsTypeHandler extends BaseTypeHandler<GoodsType> {
    private Logger logger = LoggerFactory.getLogger(GoodsTypeHandler.class);

    private Class<GoodsType> type;

    private final GoodsType[] enums;

    public GoodsTypeHandler(Class<GoodsType> type) {
        if (type == null) {
            throw new IllegalArgumentException("参数type不能为空");
        }
        this.type = type;
        enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "不是一个枚举类型");
        }
    }

    @Override
    public GoodsType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public GoodsType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public GoodsType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GoodsType goodsType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, goodsType.getCode());
    }

    private GoodsType locateEnumStatus(int code) {
        for (GoodsType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}

