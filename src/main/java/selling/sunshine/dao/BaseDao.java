package selling.sunshine.dao;

import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;

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
}
