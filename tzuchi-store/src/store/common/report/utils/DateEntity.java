/* $Id: DateEntity.java,v 1.6 2009/12/06 00:11:25 joelchou Exp $
 * Created on Feb 20, 2008
 * 
 * $Log: DateEntity.java,v $
 * Revision 1.6  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.5  2009/12/01 05:45:36  joelchou
 * Fixed issues on monthly inventory report. Changed to use older version of MySQL JDBC driver since Hibernate was complaining "column u_quantity not found" when using new version of driver.
 *
 * Revision 1.4  2008/04/18 06:55:44  joelchou
 * Fixed the issue on daily report. In the search criteria, the start date is inclusive and the end date is exclusive.
 *
 * Revision 1.3  2008/03/31 14:59:54  jlin
 * bugfix - daily sale report: transactions at next date is shown.
 *
 * Revision 1.2  2008/02/29 20:05:48  jlin
 * Add Daily Report for the backend and UI
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import members.utils.Utility;

/**
 * Utility class for date manipulation
 *
 * @author Jeff Lin
 * @version  $Date: 2009/12/06 00:11:25 $
 */
public class DateEntity
{
	private final Calendar entity;
	
	/**
	 * defualt constructor, that create the current time
	 *
	 */
	public DateEntity()
	{
		entity = Calendar.getInstance();
		resetTime(entity);
	}
	
	/**
	 * Instanciate the DateEntity object by passing a Calendar Object
	 * @param calendar
	 */
	public DateEntity(Calendar calendar)
	{
		entity = calendar;
	}
	
	public DateEntity(int year) {
        entity = Calendar.getInstance();
        resetTime(entity);
        entity.set(Calendar.YEAR, year);
        entity.set(Calendar.MONTH, 0);
        entity.set(Calendar.DAY_OF_MONTH, 1);        
	}
	
	/**
	 * create the DateEntity by passing year and the month (1 base)
	 */
	public DateEntity(int year, int month)
	{
		entity = Calendar.getInstance();
		resetTime(entity);
		entity.set(Calendar.YEAR, year);
		entity.set(Calendar.MONTH, month-1);  // month is 0 bases
		entity.set(Calendar.DATE, 1); //set to the first date of the month
	}
	
	/**
	 * create the DateEntity by passing year, the month (1 base) and date (1 bses)
	 */
	public DateEntity(int year, int month, int date)
	{
		entity = Calendar.getInstance();
		resetTime(entity);
		entity.set(Calendar.YEAR, year);
		entity.set(Calendar.MONTH, month-1);  // month is 0 bases
		entity.set(Calendar.DATE, date); //set to the first date of the month
	}
	
	/**
	 * Get the first date of the month of the entity
	 * @return
	 */
	public Date getMonthStartDate()
	{
		Calendar temp = (Calendar)entity.clone();
		temp.set(Calendar.DATE, 1);
		return temp.getTime();
	}
	
	public int getLastDateOfMonth()
	{
		Calendar temp = (Calendar)entity.clone();
		temp.set(Calendar.DATE, 1);  //set to beginning of the month
		temp.add(Calendar.MONTH, 1); //move to next month
		temp.add(Calendar.DATE, -1); //move back one date in order to get the last date
		
		return temp.get(Calendar.DATE);
	}
		
	/**
	 * Get the last date of the month of the entity
	 * @return
	 */
	public Date getMonthEndDate()
	{
		Calendar temp = (Calendar)entity.clone();
		temp.set(Calendar.DATE, 1);  //set to beginning of the month
		temp.add(Calendar.MONTH, 1); //move to next month
		temp.add(Calendar.DATE, -1); //move back one date in order to get the last date
		
		return temp.getTime();
	}
	
    /**
     * Get the first date of next month of the entity
     * @return
     */
    public Date getNextMonthFirstDate()
    {
        Calendar temp = (Calendar)entity.clone();
        temp.set(Calendar.DATE, 1);  //set to beginning of the month
        temp.add(Calendar.MONTH, 1); //move to next month
        
        return temp.getTime();
    }	
	
    /**
     * Get the first day of the year of the entity
     * @return
     */
    public Date getYearStartDate() {
        Calendar temp = (Calendar)entity.clone();
        temp.set(Calendar.DAY_OF_YEAR, 1);
        return temp.getTime();
    }    
    
    /**
     * Get the first day of next year of the entity
     * @return
     */
    public Date getNextYearStartDate() {
        Calendar temp = (Calendar)entity.clone();
        temp.add(Calendar.YEAR, 1);
        temp.set(Calendar.DAY_OF_YEAR, 1);
        return temp.getTime();
    }    
    
	/**
	 * get End datetime of entity date (exclusive)
	 * @return
	 */
	public Date getEndDate()
	{
	   // should be exclusive (<) in the criteria
		Calendar temp = (Calendar)entity.clone();
		temp.add(Calendar.DATE, 1); //move to next date
		return temp.getTime();
	}
	
	/**
	 * get start datetime of entity date (inclusive)
	 * @return
	 */
	public Date getStartDate()
	{
	   // should be inclusive (>=) in the criteria
		Calendar temp = (Calendar)entity.clone();
		return temp.getTime();
	}
	
	public int getYear() {
	    return entity.get(Calendar.YEAR);
	}
	
	/**
	 * Compare another DateEntity value.  Two object are equal only when year, month
	 * date value are equal.
	 * @return fales when two DateEntity object are not equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DateEntity other = (DateEntity) obj;
		if (entity == null)
		{
			if (other.entity != null)
				return false;
		} else if(entity.get(Calendar.YEAR) != other.entity.get(Calendar.YEAR))
			return false;
		else if(entity.get(Calendar.MONTH) != other.entity.get(Calendar.MONTH))
			return false;
		else if(entity.get(Calendar.DATE) != other.entity.get(Calendar.DATE))
			return false;
		
		return true;
	}
	
	/**
	 * formate the current date as YYYY/MM/DD
	 * @return
	 */
	public String getFormatedDateString()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(entity.getTime());
	}
	
	/**
	 * formate the current date as YYYY/MM
	 * @return
	 */
	public String getFormatedMonthString()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
		return formatter.format(entity.getTime());
	}

    /**
     * Formats the current date as YYYY
     * @return
     */
    public String getFormatedYearString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(entity.getTime());
    }	
	
	private void resetTime(Calendar cal)
	{
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);	 
      cal.set(Calendar.SECOND, 0); 
      cal.set(Calendar.MILLISECOND, 0);
	}
}
