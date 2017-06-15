package at.mtel.denza.alfresco.scan;

import java.util.Calendar;

public class UtilClass {
	
	public static Integer getDate() {
		StringBuilder sb = new StringBuilder();
		sb.append(getYear());
		sb.append(getMonth());
		sb.append(getDay());
		return Integer.valueOf(sb.toString());
	}
	
	public static Integer getTime() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHours());
		sb.append(getMinutes());
		sb.append(getSeconds());
		return Integer.valueOf(sb.toString());
	}
	
	public static Integer getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static Integer getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static Integer getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static Integer getHours() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	public static Integer getMinutes() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static Integer getSeconds() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}	
}