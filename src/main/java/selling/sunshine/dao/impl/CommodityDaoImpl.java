package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.CommodityDao;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.goods.Thumbnail;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao {
    private Logger logger = LoggerFactory.getLogger(CommodityDaoImpl.class);

    private Object lock = new Object();

    /**
     * 插入商品信息
     *
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public ResultData insertGoods4Customer(Goods4Customer goods) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                goods.setGoodsId(IDGenerator.generate("COM"));
                sqlSession.insert("selling.goods.insert", goods);
                result.setData(goods);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            } finally {
                return result;
            }
        }
    }

    /**
     * 更新商品信息
     *
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public ResultData updateGoods4Customer(Goods4Customer goods) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.goods.update", goods);
                result.setData(goods);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 代理商查询符合条件的商品信息列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryGoods4Agent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Goods4Agent> list = sqlSession.selectList("selling.goods.query4Agent", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    /**
     * 顾客查询符合条件的商品信息列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryGoods4Customer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Goods4Agent> list = sqlSession.selectList("selling.goods.query4Customer", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryGoods4CustomerByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Goods4Customer> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryGoods4Customer(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<Goods4Customer> current = queryGoods4CustomerByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Override
    @Transactional
    public ResultData updateGoodsThumbnail(List<Thumbnail> thumbnails) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
            	for (Thumbnail thumbnail:thumbnails) {
            		sqlSession.update("selling.goods.thumbnail.update", thumbnail);
				}                
                result.setData(thumbnails);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData insertGoodsThumbnail(Thumbnail thumbnail) {
        ResultData result = new ResultData();
        thumbnail.setThumbnailId(IDGenerator.generate("THB"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.goods.thumbnail.insert", thumbnail);
                result.setData(thumbnail);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 查询某一页的商品信息
     *
     * @param condition
     * @param start
     * @param length
     * @return
     */
    private List<Goods4Customer> queryGoods4CustomerByPage(Map<String, Object> condition, int start, int length) {
        List<Goods4Customer> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.goods.query4Customer", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

	@Override
	@Transactional
	public ResultData deleteGoodsThumbnail(String thumbnailId) {
		  ResultData result = new ResultData();
	      synchronized (lock) {
	            try {
	                sqlSession.delete("selling.goods.thumbnail.delete", thumbnailId);
	                result.setData(thumbnailId);
	            } catch (Exception e) {
	                logger.error(e.getMessage());
	                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	                result.setDescription(e.getMessage());
	            }
	        }
	       return result;
	}

	@Override
	public ResultData queryThumbnail(Map<String, Object> condition) {
		 ResultData result = new ResultData();
	        try {
	            List<Thumbnail> list = sqlSession.selectList("selling.goods.thumbnail.query", condition);
	            result.setData(list);
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(e.getMessage());
	        } finally {
	            return result;
	        }
	}

	@Override
	public ResultData queryThumbnail() {
		 ResultData result = new ResultData();
	        try {
	            List<Thumbnail> list = sqlSession.selectList("selling.goods.thumbnail.query_Goods_NULL");
	            result.setData(list);
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(e.getMessage());
	        } finally {
	            return result;
	        }
	}

}
