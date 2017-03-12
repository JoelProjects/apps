/* $Id: SearchCriteria.java,v 1.1 2008/02/29 13:01:20 jlin Exp $
 * Created on Jan 18, 2008
 * 
 * $Log: SearchCriteria.java,v $
 * Revision 1.1  2008/02/29 13:01:20  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.model;

/**
 *
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class SearchCriteria
{
	private String name;
	private Class dataType;
	private OperatorEnum startOperator;
	private OperatorEnum endOperator;
	private Object value;  // when criter is a range this is start value
	private Object endValue; //when criteria is a range this is a end value
	
	public enum OperatorEnum
	{
		EQUAL ("="),
		GREATER (">"),
		LESS ("<"),
		GREATER_EQUAL (">="),
		LESS_EQUAL ("<="),
		LIKE ("LIKE"),
		NOT_LIKE ("NOT LIKE");
		
		private String value;
		OperatorEnum(String value)
		{
			this.value =value;
		}
		
		public String toString()
		{
			return value;
		}
	}

	/**
	 * Set search criteria with a start value
	 * @param name - the name of the criter
	 * @param operator - one of the OperatorEnum
	 * @param value - the value of the criteria
	 */
	public SearchCriteria(String name, OperatorEnum operator, Object value)
	{
		this.name = name;
		this.startOperator = operator;
		this.value = value;
		dataType = value.getClass();
	}
	
	/**
	 * Set Search criteria with end value without specifying begin value
	 * @param name - the name of the criter
	 * @param endValue - the value of the criteria
	 * @param endOperator - one of the OperatorEnum
	 */
	public SearchCriteria(String name, Object endValue, OperatorEnum endOperator)
	{
		this.name = name;
		this.endOperator = endOperator;
		this.endValue = endValue;
		dataType = endValue.getClass();
	}
	
	/**
	 * When search criteria is a range use this constructor to specify the
	 * starting value and ending value and operator 
	 * @param name
	 * @param startOperator - operator for start value. e.g greater equal
	 * @param startValue - starting value of the range
	 * @param endOperator operator for end value. e.g. less equal
	 * @param endValue - ending value of the range
	 */
	public SearchCriteria(String name, OperatorEnum startOperator, Object startValue, 
			OperatorEnum endOperator, Object endValue)
	{
		this.name = name;
		this.startOperator = startOperator;
		this.value = startValue;
		this.dataType = startValue.getClass();
		this.endValue = endValue;
		this.endOperator = endOperator;
	}

	public Class getDataType()
	{
		return dataType;
	}

	public void setDataType(Class dataType)
	{
		this.dataType = dataType;
	}

	public Object getEndValue()
	{
		return endValue;
	}

	public void setEndValue(Object endValue)
	{
		this.endValue = endValue;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public OperatorEnum getOperator()
	{
		return startOperator;
	}

	public void setOperator(OperatorEnum operator)
	{
		this.startOperator = operator;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
	
	public Object getStartValue()
	{
		return value;
	}

	public void setStartValue(Object value)
	{
		this.value = value;
	}
	
	public OperatorEnum getEndOperator()
	{
		return endOperator;
	}

	public void setEndOperator(OperatorEnum operator)
	{
		this.endOperator = operator;
	}
	


}
