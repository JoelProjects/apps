/* $Id: ReportException.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Feb 20, 2008
 * 
 * $Log: ReportException.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;

/**
 * Any report related exception
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class ReportException extends Exception
{

	/**
	 * 
	 */
	public ReportException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ReportException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ReportException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReportException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
