package common.sunshine.dao;

import common.sunshine.utils.SortRule;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/18/16.
 */
public class BaseDao {
    @Resource
    protected SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Map<String, Object> handle(Map<String, Object> condition) {
        List<SortRule> constraints = (List<SortRule>) condition.get("sort");
        if (constraints != null && constraints.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (SortRule item : constraints) {
                sb.append(item.getName()).append(" ").append(item.getMethod()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            condition.remove("sort");
            condition.put("rule", sb.toString());
        }
        return condition;
    }
}
