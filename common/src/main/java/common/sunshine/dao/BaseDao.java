package common.sunshine.dao;

import common.sunshine.utils.SortRule;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 该类用于管理持久层与数据库的连接sql session, 所有的Dao需要继承此方法
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

    /**
     * 该方法用于过滤查询条件, 若查询条件中包含sort, 需将sort转变成sql中的排序字符串
     *
     * @param condition
     * @return
     */
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
