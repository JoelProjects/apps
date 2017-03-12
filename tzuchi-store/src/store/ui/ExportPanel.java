/* $Id: ExportPanel.java,v 1.10 2009/12/06 06:14:52 joelchou Exp $
 * Created on Feb 22, 2008
 * 
 * $Log: ExportPanel.java,v $
 * Revision 1.10  2009/12/06 06:14:52  joelchou
 * Added regions A and B for inventory list and changed width in some columns
 *
 * Revision 1.9  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.8  2009/12/01 05:45:36  joelchou
 * Fixed issues on monthly inventory report. Changed to use older version of MySQL JDBC driver since Hibernate was complaining "column u_quantity not found" when using new version of driver.
 *
 * Revision 1.7  2008/10/22 06:13:37  joelchou
 * Changed to show display name instead of username.
 *
 * Revision 1.6  2008/03/19 19:03:04  jlin
 * Add modified_by column to Asset Transation report.
 *
 * Revision 1.5  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.4  2008/03/13 20:29:18  jlin
 * add simple asset report
 *
 * Revision 1.3  2008/03/06 23:12:46  jlin
 * add new feature:  Sale Report by sale handlers
 *
 * Revision 1.2  2008/02/29 20:05:48  jlin
 * Add Daily Report for the backend and UI
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import members.datamodel.AccountInfo;
import members.ui.utils.GuiUtil;

import org.apache.log4j.Logger;

import store.common.report.ReportException;
import store.common.report.render.IReport;
import store.common.report.render.SimpleTableReport;
import store.common.report.utils.DateEntity;
import store.common.report.utils.Utils;
import store.manager.ContextManager;
import store.manager.ReportsManager;

/**
 * 
 * 
 * @author Jeff Lin
 * @version $Date: 2009/12/06 06:14:52 $
 */
public class ExportPanel extends JPanel
{
	private Logger logger = Logger.getLogger(this.getClass());

	/***************************************************************************
	 * Constants
	 **************************************************************************/
	private static final String[] REGIONS = { ReportsManager.REGION_A,
			ReportsManager.REGION_B };

	private static final String[] REPORT_TYPE = { "每月出售記錄表", "每日出售記錄表" ,
	    "每月出售記錄收款人名細", "庫存表", "每月庫存進銷表", "盤點清單"};

	private List<Integer> yearList = new ArrayList<Integer>();

	private static final String[] MONTH = { "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11", "12" };

	private static final Integer[] DATE = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
			29, 30, 31 };

	private static Calendar currentDateTime = Calendar.getInstance(); // @jve:decl-index=0:

	private static JFileChooser chooser;

	private static final long serialVersionUID = 1L;

	private JPanel searchPanel = null;

	private JPanel viewPanel = null;

	private JComboBox regionComboBox = null;

	private JComboBox reportTypeComboBox = null;

	private JComboBox yearComboBox = null;

	private JComboBox monthComboBox = null;

	private JButton exportButton = null;

	private JButton previewButton = null;

	private JScrollPane viewTableScrollPane = null;

	private JTable viewTable = null;

	private String currentReportType;

	private String currentRegion;

	private DateEntity currentDate;

	private IReport currentReport;

	private JComboBox dateComboBox = null;

	private JComboBox saleHandlersComboBox = null;

	/**
	 * 
	 */
	public ExportPanel()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
        int currentYear = currentDateTime.get(Calendar.YEAR);
        for(int year = currentYear; year >= 2008; year--)
            yearList.add(new Integer(year));
	    
		this.setSize(800, 600);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(800, 600));
		this.add(getSearchPanel(), BorderLayout.NORTH);
		this.add(getViewPanel(), BorderLayout.CENTER);		
	}

	/**
	 * This method initializes searchPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSearchPanel()
	{
		if (searchPanel == null)
		{
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(10);
			flowLayout.setVgap(15);
			searchPanel = new JPanel();
			searchPanel.setLayout(flowLayout);
			searchPanel.setPreferredSize(new Dimension(800, 50));
			searchPanel.add(getRegionComboBox(), null);
			searchPanel.add(getReportTypeComboBox(), null);
			searchPanel.add(getYearComboBox(), null);
			searchPanel.add(getMonthComboBox(), null);
			searchPanel.add(getDateComboBox(), null);
			searchPanel.add(getSaleHandlersComboBox(), null);
			searchPanel.add(getPreviewButton(), null);
			searchPanel.add(getExportButton(), null);
		}
		return searchPanel;
	}

	/**
	 * This method initializes viewPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getViewPanel()
	{
		if (viewPanel == null)
		{
			viewPanel = new JPanel();
			viewPanel.setLayout(new FlowLayout());
			viewPanel.setPreferredSize(new Dimension(800, 530));
			viewPanel.add(getViewTableScrollPane(), null);
		}
		return viewPanel;
	}

	/**
	 * This method initializes regionComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getRegionComboBox()
	{
		if (regionComboBox == null)
		{
			regionComboBox = new JComboBox(REGIONS);
		}
		return regionComboBox;
	}

	/**
	 * This method initializes reportTypeComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getReportTypeComboBox()
	{
		if (reportTypeComboBox == null)
		{
			reportTypeComboBox = new JComboBox(REPORT_TYPE);
			reportTypeComboBox.addItemListener(new java.awt.event.ItemListener()
			{
				public void itemStateChanged(java.awt.event.ItemEvent e)
				{
					//turn on every thing by default
					regionComboBox.setVisible(true);
					yearComboBox.setVisible(true);
					monthComboBox.setVisible(true);
					dateComboBox.setVisible(true);
					saleHandlersComboBox.setVisible(true);
					
					if(reportTypeComboBox.getSelectedItem().equals("每月出售記錄表"))
					{
						dateComboBox.setVisible(false);
						saleHandlersComboBox.setVisible(false);
						return;
					}
					
					if(reportTypeComboBox.getSelectedItem().equals("每日出售記錄表"))
					{
						saleHandlersComboBox.setVisible(false);
						return;
					}
					
					
					if(reportTypeComboBox.getSelectedItem().equals("每月庫存進銷表"))
					{
						regionComboBox.setVisible(false);
						dateComboBox.setVisible(false);
						saleHandlersComboBox.setVisible(false);
						return;
					}
					
					if(reportTypeComboBox.getSelectedItem().equals("庫存表"))
					{
						yearComboBox.setVisible(false);
						monthComboBox.setVisible(false);
						dateComboBox.setVisible(false);
						saleHandlersComboBox.setVisible(false);
						return;
					}

                    if(reportTypeComboBox.getSelectedItem().equals("盤點清單")) {
                        regionComboBox.setVisible(false);
                        monthComboBox.setVisible(false);
                        dateComboBox.setVisible(false);
                        saleHandlersComboBox.setVisible(false);
                        return;
                    }					
					
					if(reportTypeComboBox.getSelectedItem().equals("每日出售記錄表"))
					{
						int year = getSelectedYear();
						int month = getSelectedMonth();
						refreshDateComboBox(getDateComboBox(), new DateEntity(year, month));
						saleHandlersComboBox.setVisible(false);
						return;
					}
					
					if(reportTypeComboBox.getSelectedItem().equals("每月出售記錄收款人名細"))
					{
						int year = getSelectedYear();
						int month = getSelectedMonth();
						List<String> list = ReportsManager.getListOfSaleHandlersByMonth(new DateEntity(year, month));
						refreshSaleHandlersComboBox(saleHandlersComboBox, list);
						dateComboBox.setVisible(false);
						return;
					}
						
				}
			});
		}
		return reportTypeComboBox;
	}

	/**
	 * This method initializes yearComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getYearComboBox()
	{
		if (yearComboBox == null)
		{
			yearComboBox = new JComboBox(yearList.toArray());
			yearComboBox
					.setSelectedItem(currentDateTime.get(Calendar.YEAR));
			yearComboBox.addItemListener(new MyItemListener());
		}
		return yearComboBox;
	}

	/**
	 * This method initializes monthComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getMonthComboBox()
	{
		if (monthComboBox == null)
		{
			monthComboBox = new JComboBox(MONTH);
			monthComboBox.setSelectedIndex(currentDateTime
					.get(Calendar.MONTH));
			monthComboBox.addItemListener(new MyItemListener());
		}
		return monthComboBox;
	}

	/**
	 * This method initializes exportButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportButton()
	{
		if (exportButton == null)
		{
			exportButton = new JButton();
			exportButton.setText("輸出");
			exportButton.addActionListener(new MyActionListener(this));
		}
		return exportButton;
	}

	private JFileChooser getFileChooser()
	{
		if (chooser == null)
		{
			chooser = new JFileChooser();
			chooser.setApproveButtonText("Save");
			chooser.setFileFilter(new FileFilter()
			{
				@Override
				public boolean accept(File f)
				{
					if (f.isDirectory())
					{
						return true;
					}

					String extension = Utils.getExtension(f);
					if (extension != null)
					{
						if (extension.equals(Utils.XLS))
							return true;
						else
							return false;
					}

					return false;
				}

				@Override
				public String getDescription()
				{
					return Utils.XLS;
				}

			});
		}
		return chooser;
	}

	/**
	 * This method initializes previewButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPreviewButton()
	{
		if (previewButton == null)
		{
			previewButton = new JButton();
			previewButton.setText("預覽");
			previewButton.addActionListener(new MyActionListener(this));
		}
		return previewButton;
	}

	private int getSelectedYear()
	{
		return (Integer)getYearComboBox().getSelectedItem();
	}

	private int getSelectedMonth()
	{
		return getMonthComboBox().getSelectedIndex() + 1;
	}
	
	private String getSelectedSaleHandler()
	{
	    SaleHandler handler = (SaleHandler)getSaleHandlersComboBox().getSelectedItem();
		return handler.getUsername();
	}

	private int getSelectedDate()
	{
		return getDateComboBox().getSelectedIndex() + 1;
	}

	private class MyActionListener implements ActionListener
	{
	    JComponent parent;
		private int[] columnWidth;

		public MyActionListener(JComponent parent) {
		    this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e)
		{
		    parent.setCursor(new Cursor(Cursor.WAIT_CURSOR)); 
		    
			IReport report = generateReport(); // this is excel report render
			// type
			ReportsManager manager = ReportsManager.getInstance();

			parent.setCursor(Cursor.getDefaultCursor());
			
			if (e.getSource().equals(getExportButton()))
			{
				// handle export
				JFileChooser chooser = getFileChooser();
				int returnVal = chooser.showOpenDialog(ExportPanel.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				String fileName = chooser.getSelectedFile().getParent() + "/"
						+ chooser.getSelectedFile().getName();
				try
				{
					manager.renderSaleItemsReport(report, fileName);
				} catch (ReportException re)
				{
					logger.error(e);
					// TODO pop up the error message
				}

				// TODO pop up messge for saving file successfully

			} else if (e.getSource().equals(getPreviewButton()))
			{			    
				// handle preview
				IReport tableReport = new SimpleTableReport(report);
				TableModel tableModel = (TableModel) tableReport
						.renderReport(null);
				getViewTable().setModel(tableModel);
				GuiUtil.setColumnWidths(getViewTable(), columnWidth);
			}
		}

		private IReport generateReport()
		{
			/**
			 * optimze the report. If user ask the same type of report and the
			 * same date, we use the cached report
			 */
			int year = getSelectedYear();
			int month = getSelectedMonth();
			int dateOfMonth = getSelectedDate();
			DateEntity date = new DateEntity(year, month, dateOfMonth);
			String reportType = reportTypeComboBox.getSelectedItem().toString();
			String region = regionComboBox.getSelectedItem().toString();
			IReport report = null;

			if (region.equals(currentRegion)
					&& reportType.equals(currentReportType)
					&& currentDate.equals(date))
				report = currentReport;

			if (report == null)
			{ // no cached report, create a new one
				ReportsManager manager = ReportsManager.getInstance();

				if (reportTypeComboBox.getSelectedItem().equals("每月出售記錄表"))
				{ // handle monthly sale report

					report = manager.getSaleItemsReportByMonth(region, date);
					//define column width
					columnWidth = new int[] { 30, 25, 200, 30, 30, 30, 30 }; 

				} else if (reportTypeComboBox.getSelectedItem().equals("每日出售記錄表"))
				{ // handle daily sale report

					report = manager.getSaleItemsReportByDate(region, date);
					columnWidth = new int[] { 30, 200, 30, 30, 30, 30, 30, 20, 20};

				} else if (reportTypeComboBox.getSelectedItem().equals(
						"每月出售記錄收款人名細"))
				{ 
					report = manager.getSaleItemsReportByHandler(
							getSelectedSaleHandler(), date);
					
					columnWidth = new int[] { 30, 30, 200, 30, 30, 30, 30, 30, 20};
				} else if(reportTypeComboBox.getSelectedItem().equals("庫存表"))
				{
					report = manager.getSimpleAssetReportByHandler(region);
					columnWidth = new int[] { 30, 300, 30, 30, 30};
				} else if(reportTypeComboBox.getSelectedItem().equals("每月庫存進銷表"))
				{
					report = manager.getAssetTransactionReport(date);
					columnWidth = new int[] { 30, 30, 300, 25, 25, 40, 25};
				} else if(reportTypeComboBox.getSelectedItem().equals("盤點清單")) {
                    report = manager.getInventoryList(date);
                    columnWidth = new int[] { 30, 30, 150, 20, 100, 20, 100, 20, 30};
                }
				else
				{
					logger.warn(reportTypeComboBox.getSelectedItem().toString()
							+ " report type is not implemented");
				}

				currentRegion = region;
				currentDate = date;
				currentReportType = reportType;
				currentReport = report;
			}

			return report;
		}

	}

	/**
	 * This method initializes viewTableScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getViewTableScrollPane()
	{
		if (viewTableScrollPane == null)
		{
			viewTableScrollPane = new JScrollPane();
			viewTableScrollPane.setPreferredSize(new Dimension(780, 520));
			viewTableScrollPane.setViewportView(getViewTable());
		}
		return viewTableScrollPane;
	}

	/**
	 * This method initializes viewTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getViewTable()
	{
		if (viewTable == null)
		{
			viewTable = new JTable();
		}
		return viewTable;
	}

	/**
	 * This method initializes dateComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDateComboBox()
	{
		if (dateComboBox == null)
		{
			dateComboBox = new JComboBox(DATE);
			DateEntity month = new DateEntity(currentDateTime);
			refreshDateComboBox(dateComboBox, month);
			//selectedIndex is 0 base
			dateComboBox.setSelectedIndex(currentDateTime.get(Calendar.DATE) - 1);
			dateComboBox.setVisible(false);  //start with invisiable
		}
		return dateComboBox;
	}

	/**
	 * reload the Date combobox base on year and month
	 * 
	 * @param month
	 */
	private void refreshDateComboBox(JComboBox dateCombo, DateEntity month)
	{
		int lastDate = month.getLastDateOfMonth();

		/*
		 * remove the date until 28 and add it up to the the last date
		 */
		dateCombo.removeItem(new Integer(29)); // date 29
		dateCombo.removeItem(new Integer(30)); // date 30
		dateCombo.removeItem(new Integer(31)); // date 31

		for (int i = 29; i <= lastDate; i++)
			dateCombo.addItem(new Integer(i));
	}
	
	private void refreshSaleHandlersComboBox(JComboBox comboBox, List<String> list)
	{
		comboBox.removeAllItems();
		
		for(String username: list) {
		    AccountInfo accountInfo = ContextManager.getAccountInfo(username);
		    String name = "";
		    if(accountInfo != null)
		        name = accountInfo.getMemberInfo().getName();
		    else
		        name = username;
			comboBox.addItem(new SaleHandler(username, name));
		}
	}

	private class SaleHandler {
	    private String username;
	    private String displayName;
	    
	    SaleHandler(String username, String displayName) {
	        this.username = username;
	        this.displayName = displayName;
	    }
	    
	    public String getUsername() {
	        return username;
	    }
	    
	    public String toString() {
	        return displayName;
	    }
	}
	
	private class MyItemListener implements ItemListener
	{

		public void itemStateChanged(ItemEvent e)
		{
			int year = getSelectedYear();
			int month = getSelectedMonth();
			//refresh only when dateComboBox is visable
			if(dateComboBox.isVisible())
				refreshDateComboBox(getDateComboBox(), new DateEntity(year, month));
			//refresh only when saleHandlersComboBox is visable
			if(saleHandlersComboBox.isVisible())
			{
				List<String> list = ReportsManager.getListOfSaleHandlersByMonth(new DateEntity(year, month));
				refreshSaleHandlersComboBox(getSaleHandlersComboBox(), list);
			}
		}

	}

	/**
	 * This method initializes saleHandlersComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getSaleHandlersComboBox()
	{
		if (saleHandlersComboBox == null)
		{
			saleHandlersComboBox = new JComboBox();
			saleHandlersComboBox.setVisible(false); //start with invisiable
		}
		return saleHandlersComboBox;
	}

}
