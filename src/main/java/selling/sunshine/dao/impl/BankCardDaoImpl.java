package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BankCardDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.BankCard;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/7/16.
 */
@Repository
public class BankCardDaoImpl extends BaseDao implements BankCardDao {
    private Logger logger = LoggerFactory.getLogger(BankCardDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData createBankCard(BankCard card) {
        ResultData result = new ResultData();
        card.setBankCardId(IDGenerator.generate("BKC"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.agent.bankcard.insert", card);
                result.setData(card);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryBankCard(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<BankCard> list = sqlSession.selectList("selling.agent.bankcard.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Transactional
    @Override
    public ResultData updateBankCard(BankCard card) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", card.getAgent().getAgentId());
        List<BankCard> list = sqlSession.selectList("selling.agent.bankcard.query", condition);
        condition.put("preferred", true);
        BankCard preferCard = sqlSession.selectOne("selling.agent.bankcard.query", condition);
        synchronized (lock) {
            try {
                if (preferCard != null) {
                    if (preferCard.getBankCardNo().equals(card.getBankCardNo())) {
                        result.setData(preferCard);
                        return result;
                    }
                    preferCard.setPreferred(false);
                    sqlSession.update("selling.agent.bankcard.update", preferCard);
                }
                for (BankCard item : list) {
                    if (item.getBankCardNo().equals(card.getBankCardNo())) {
                        item.setPreferred(true);
                        sqlSession.update("selling.agent.bankcard.update", item);
                        result.setData(item);
                        return result;
                    }
                }
                sqlSession.insert("selling.agent.bankcard.insert", card);
                result.setData(card);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }
}
