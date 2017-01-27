package at.mtel.denza.alfresco.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static Date getDateFromString(String dateString) {
	    Date date;
	    try {
	       date = new Date(dateString);
	       return date;
	    } catch(Exception e) {
	        try {
				date = DATE_FORMAT.parse(dateString);
				return date;
			}
			catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
	    }
	}
}
