/* $Id: UnmatchedPropertyTypeException.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Jan 11, 2008
 * 
 * $Log: UnmatchedPropertyTypeException.java,v $
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
public class UnmatchedPropertyTypeException extends Exception
{

	/**
	 * 
	 */
	public UnmatchedPropertyTypeException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnmatchedPropertyTypeException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnmatchedPropertyTypeException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnmatchedPropertyTypeException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
