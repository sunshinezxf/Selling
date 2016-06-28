package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Service;

import selling.sunshine.model.express.Express;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * Created by sunshine on 5/10/16.
 */
@Service
public class ToolServiceImpl implements ToolService {

    @Override
    public String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    @Override
    public JSONObject getParams(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = request.getReader();
            char[] buff = new char[1024];
            int length;
            while ((length = reader.read(buff)) != -1) {
                sb.append(buff, 0, length);
            }
            result = JSONObject.parseObject(sb.toString());
        } catch (IOException e) {
            return result;
        }
        return result;
    }

	@Override
	public ResultData exportExcel(OutputStream os,List<Express> expresseList) {
		ResultData resultData=new ResultData();
		
		WritableWorkbook book = null;
		WritableSheet sheet;
		try {

			book = Workbook.createWorkbook(os);
			sheet = book.createSheet( " 第一页 " , 0 );
	        String headerArr[] = {"运单编号","月结客户","寄件人","寄件电话","寄件省份","寄件城市","寄件区县","寄件详细地址",
	        		"收件人","收件电话","收件省份","收件城市","收件区县","收件详细地址","物品类型","物品重量","运费","代收货款","到付款","回单编号",
	        		"揽件人","保价金额","录单备注"};
			for (int i = 0; i < headerArr.length; i++) {
				 sheet.addCell(new Label( i , 0 , headerArr[i]));
			}
//		    sheet.addCell(new Label( headerArr.length , 0 , "短信状态（填写“是”或者“否”）"));
//		//   sheet.addCell(new Label( headerArr.length+1 , 0 , ""));
//		//    sheet.addCell(new Label( headerArr.length+2 , 0 , ""));
//		    sheet.addCell(new Label( headerArr.length , 1 , "短信通知寄件人"));
//		    sheet.addCell(new Label( headerArr.length+1 , 1 , "短信通知收件人"));
//	//	    sheet.addCell(new Label( headerArr.length+2 , 1 , ""));
//		    sheet.addCell(new Label( headerArr.length , 2 , "签收"));
//		    sheet.addCell(new Label( headerArr.length+1 , 2 , "揽件"));
//		    sheet.addCell(new Label( headerArr.length+2 , 3 , "派件"));
//
//		    String headerArr2[] = {"始发地","发件单位","发件部门","发件邮编","目的地","收件单位","收件邮编","长","宽","高","数量","订单号"};
//			for (int i = (headerArr.length+3); i < headerArr2.length; i++) {
//				 sheet.addCell(new Label( i , 0 , headerArr2[i]));
//			}
//		    sheet.mergeCells(headerArr.length, 0, headerArr.length+1, 0);
//		    sheet.mergeCells(headerArr.length, 0, headerArr.length+2, 0);
//		    sheet.mergeCells(headerArr.length+1, 1, headerArr.length+2, 1);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			for (int i = 0; i < expresseList.size(); i++) {
				String date = dateFormat.format(expresseList.get(i).getCreateAt());
				sheet.addCell(new Label(0,i+1,expresseList.get(i).getExpressNumber()));
				sheet.addCell(new Label(1,i+1,date));
				sheet.addCell(new Label(2,i+1,expresseList.get(i).getSenderName()));
				sheet.addCell(new Label(3,i+1,expresseList.get(i).getSenderPhone()));
				sheet.addCell(new Label(4,i+1,expresseList.get(i).getSenderAddress()));
				sheet.addCell(new Label(5,i+1,expresseList.get(i).getReceiverName()));
				sheet.addCell(new Label(6,i+1,expresseList.get(i).getReceiverPhone()));
				sheet.addCell(new Label(7,i+1,expresseList.get(i).getReceiverAddress()));
				sheet.addCell(new Label(8,i+1,expresseList.get(i).getGoodsName()));
			}
			
			//写入数据并关闭文件
            book.write();
            book.close();
            os.flush();
            os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		resultData.setData(book);
		return resultData;
	}
}
