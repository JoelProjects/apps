/* $Id: UnmatchedPropertyException.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 11, 2008
 * 
 * $Log: UnmatchedPropertyException.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report;

/**
 *
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class UnmatchedPropertyException extends Exception
{

	/**
	 * 
	 */
	public UnmatchedPropertyException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnmatchedPropertyException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnmatchedPropertyException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnmatchedPropertyException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
