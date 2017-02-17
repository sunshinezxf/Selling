package selling.sunshine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期处理类
 * @author wxd
 */
public class DateUtils {
	private String dateFormat ;
	private SimpleDateFormat format ;
	private List<String> dateList;
	private Logger logger=LoggerFactory.getLogger(DateUtils.class);

	public DateUtils() {
		dateFormat = "yyyy-MM-dd";
		format = new SimpleDateFormat(dateFormat);
		dateList=new ArrayList<>();
	}

	/**
	 * 以string类型把从date1起，到date2为止的每一天的日期加入到一个list中，返回给调用者使用
	 * @param date1
	 * @param date2
	 */
	public void process(String date1, String date2) {
		if (date1.equals(date2)) {
			dateList.add(date1);
			return;
		}

		String tmp;
		if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
			tmp = date1;
			date1 = date2;
			date2 = tmp;
		}
        dateList.add(date1);
		tmp = format.format(str2Date(date1).getTime() + 3600 * 24 * 1000);


		while (tmp.compareTo(date2) < 0) {
			dateList.add(tmp);
			tmp = format.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
		}
		 dateList.add(date2);
	}

	/**
	 * 把string类型的日期转化为date类型
	 * @param str
	 * @return
	 */
	private Date str2Date(String str) {
		if (str == null)
			return null;

		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}
	
	
}