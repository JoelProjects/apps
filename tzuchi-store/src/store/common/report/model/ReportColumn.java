/* $Id: ReportColumn.java,v 1.2 2009/12/06 00:11:25 joelchou Exp $
 * Created on Feb 13, 2008
 * 
 * $Log: ReportColumn.java,v $
 * Revision 1.2  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.1  2008/02/29 13:01:20  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.model;

/**
 * This is the model of the report column
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class ReportColumn
{
	public enum DataType
	{
		TEXT,
		CURRENCY,
		INTEGER,
		DATE,
		FLOAT
	}
	
	private String columnName;
	private String displayName;
	private int order;  //0 bases;
	private DataType dataType;
	private boolean sumColumn;
	private int width; 
	
    /**
	 * Empty column provide the the column header without any data.  This is mainly
	 * for the column like comment.
	 */
	private boolean dummyColumn;
	

	/**
	 * Create ReportColumn Object without specifying the order
	 * @param columnName
	 * @param displayName
	 * @param dataType
	 * @param isSumColumn
	 * @param isDummyColumn
	 */
	public ReportColumn(String columnName,
						String displayName,
						DataType dataType,
						boolean isSumColumn,
						boolean isDummyColumn)
	{
		this.columnName = columnName;
		this.displayName = displayName;
		this.dataType = dataType;
		this.sumColumn = isSumColumn;
		this.dummyColumn = isDummyColumn;
		
		//order is set later
	}

    /**
     * Create ReportColumn Object without specifying the order
     * @param columnName
     * @param displayName
     * @param dataType
     * @param isSumColumn
     * @param isDummyColumn
     * @param width
     */
    public ReportColumn(String columnName,
                        String displayName,
                        DataType dataType,
                        boolean isSumColumn,
                        boolean isDummyColumn,
                        int width) {
        this.columnName = columnName;
        this.displayName = displayName;
        this.dataType = dataType;
        this.sumColumn = isSumColumn;
        this.dummyColumn = isDummyColumn;
        this.width = width;
        //order is set later
    }	
	
	/**
	 * Create ReportColumn object with all attributes
	 * @param columnName - 
	 * @param displayName
	 * @param order
	 * @param dataType
	 * @param isSumColumn
	 * @param isDummyColumn
	 */
	public ReportColumn(String columnName,
						String displayName,
						int order,
						DataType dataType,
						boolean isSumColumn,
						boolean isDummyColumn)
	{
		this.columnName = columnName;
		this.displayName = displayName;
		this.order = order;
		this.dataType = dataType;
		this.sumColumn = isSumColumn;
		this.dummyColumn = isDummyColumn;
	}

    /**
     * Create ReportColumn object with all attributes
     * @param columnName - 
     * @param displayName
     * @param order
     * @param dataType
     * @param isSumColumn
     * @param isDummyColumn
     * @param width
     */
    public ReportColumn(String columnName,
                        String displayName,
                        int order,
                        DataType dataType,
                        boolean isSumColumn,
                        boolean isDummyColumn,
                        int width)
    {
        this.columnName = columnName;
        this.displayName = displayName;
        this.order = order;
        this.dataType = dataType;
        this.sumColumn = isSumColumn;
        this.dummyColumn = isDummyColumn;
        this.width = width;
    }	
	
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public boolean isDummyColumn()
	{
		return dummyColumn;
	}

	public DataType getDataType()
	{
		return dataType;
	}

	public boolean isSumColumn()
	{
		return sumColumn;
	}

    public int getWidth() {
        return width * 256;  // 1/256th of a character width in POI
    }

    public void setWidth(int width) {
        this.width = width;
    }	
}
