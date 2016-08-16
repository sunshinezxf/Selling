package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.CashBackDao;
import selling.sunshine.dao.RefundDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.cashback.CashBackRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CashBackService;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunshine on 8/12/16.
 */
@Service
public class CashBackServiceImpl implements CashBackService {
    private Logger logger = LoggerFactory.getLogger(CashBackServiceImpl.class);

    @Autowired
    private CashBackDao cashBackDao;

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private RefundDao refundDao;

    @Override
    public ResultData fetchCashBackPerMonth(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = cashBackDao.queryMonthly(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<CashBack4AgentPerMonth>) response.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCashBackPerMonth(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = cashBackDao.queryMonthlyByPage(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCashBack(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = cashBackDao.queryByPage(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData produce(List<CashBack4AgentPerMonth> list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/cashback";
        File file = new File(parent + directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        Workbook summary = WorkBookUtil.getCashbackSummaryTemplate();
        Sheet sheet = summary.getSheetAt(0);
        int row = 3;
        double totalCashback = 0;
        for (CashBack4AgentPerMonth item : list) {
            Workbook detail = WorkBookUtil.getCashbackDetailTemplate();
            produce(detail, item);

            totalCashback += item.getAmount();
            Row current = sheet.createRow(row);
            Cell agent = current.createCell(0);
            agent.setCellValue(item.getAgent().getName());
            Cell total = current.createCell(1);
            total.setCellValue(item.getAmount());
            Cell month = current.createCell(2);
            month.setCellValue(item.getMonth());
            Cell sPieces = current.createCell(3);
            sPieces.setCellValue(item.getsPieces());
            Cell self = current.createCell(4);
            self.setCellValue(item.getSelf());
            Cell dPieces = current.createCell(5);
            dPieces.setCellValue(item.getdPieces());
            Cell direct = current.createCell(6);
            direct.setCellValue(item.getDirect());
            Cell iPieces = current.createCell(7);
            iPieces.setCellValue(item.getiPieces());
            Cell indirect = current.createCell(8);
            indirect.setCellValue(item.getIndirect());
            row++;
        }
        Row r = sheet.getRow(1);
        Cell month = r.createCell(1);
        Calendar calendar = Calendar.getInstance();
        Cell time = r.createCell(5);
        time.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        month.setCellValue(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        Cell total = r.createCell(3);
        total.setCellValue(totalCashback);
        String name = IDGenerator.generate("Cashback");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append("月度返现清单_").append(name).append(".xlsx");
        try {
            FileOutputStream out = new FileOutputStream(sb.toString());
            summary.write(out);
            out.flush();
            out.close();
            result.setData(sb.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }

    private void produce(Workbook template, CashBack4AgentPerMonth cashback) {
        Sheet sheet = template.getSheetAt(0);
        Row title = sheet.getRow(0);
        Cell agent = title.createCell(1);
        agent.setCellValue(cashback.getAgent().getName());
        title = sheet.getRow(1);
        Cell month = title.createCell(1);
        month.setCellValue(cashback.getMonth());
        title = sheet.getRow(2);
        Cell total = title.createCell(1);
        total.setCellValue(cashback.getAmount());
        Row self = sheet.createRow(4);
        Cell type = self.createCell(0);
        type.setCellValue("自销");
        Cell piece = self.createCell(1);
        piece.setCellValue(cashback.getsPieces());
        Cell amount = self.createCell(2);
        amount.setCellValue(cashback.getSelf());
        Map<String, Object> condition = new HashMap<>();
        condition.put("upperAgentId", cashback.getAgent().getAgentId());
        ResultData response = agentDao.queryAgent(condition);
        int row = 5;
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List) response.getData()).isEmpty()) {
            List<String> list = new ArrayList<>();
            for (Agent a : (List<Agent>) response.getData()) {
                list.add(a.getAgentId());
            }
            condition.clear();
            condition.put("agentIds", list);
            response = refundDao.queryRefundRecord(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CashBackRecord>) response.getData()).isEmpty()) {
                List<CashBackRecord> records = (List<CashBackRecord>) response.getData();
                for (CashBackRecord record : records) {
                    Row current = sheet.createRow(row);
                    type = current.createCell(0);
                    type.setCellValue("一级返现");
                    agent = current.createCell(1);
                    agent.setCellValue(record.getAgent().getName());
                    piece = current.createCell(2);
                    piece.setCellValue(record.getOrderPool().getQuantity());
                    amount = current.createCell(3);
                    amount.setCellValue(record.getAmount());
                    row++;
                }
            }
        }
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String m = cashback.getMonth().replace("-", "");
        String directory = "/material/journal/cashback" + "/" + m;
        File file = new File(parent + directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(cashback.getAgent().getName()).append(".xlsx");
        try {
            FileOutputStream out = new FileOutputStream(sb.toString());
            template.write(out);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return;
    }
}
