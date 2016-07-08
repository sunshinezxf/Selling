package selling.sunshine.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.sunshine.form.SortRule;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/7/16.
 */
public class WorkBookUtil {
    private static Logger logger = LoggerFactory.getLogger(WorkBookUtil.class);

    public static boolean createIndentTemplate(String context) {
        int length = 7;
        try {
            StringBuffer path = new StringBuffer(context);
            path.append(PlatformConfig.getValue("indent_template"));
            File file = new File(path.toString());
            if (file.exists()) {
                return true;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            Workbook workbook = new XSSFWorkbook();
            //设置sheet的名字
            Sheet sheet = workbook.createSheet("订货单");
            //设置订货单标题
            Row title = sheet.createRow(0);
            for (int i = 0; i < length; i++) {
                Cell cell = createCell(workbook, title, (short) i, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
                if (i == 0) {
                    cell.setCellValue("云草纲目平台订货单");
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, length - 1));
            //订货单编号和订货日期
            Row order = sheet.createRow(1);
            Cell orderNo = order.createCell(0);
            orderNo.setCellValue("订单编号");
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
            Cell orderDate = order.createCell(4);
            orderDate.setCellValue("订货日期");
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
            //供应方
            Row provider = sheet.createRow(2);
            Cell providerName = provider.createCell(0);
            providerName.setCellValue("供应方");
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
            Cell phone = provider.createCell(4);
            phone.setCellValue("电话");
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));
            //地址
            Row address = sheet.createRow(3);
            Cell location = address.createCell(0);
            location.setCellValue("地址");
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 6));
            FileOutputStream out = new FileOutputStream(path.toString());
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean generateExpress(String context) {
        int length = 38;

        return true;
    }

    private static Cell createCell(Workbook wb, Row row, short column, short halign, short valign) {
        Cell cell = row.createCell(column);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
        return cell;
    }
}
