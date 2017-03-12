/*$Id: ReportsManager.java,v 1.18 2009/12/06 06:14:52 joelchou Exp $
 * 
 * $Log: ReportsManager.java,v $
 * Revision 1.18  2009/12/06 06:14:52  joelchou
 * Added regions A and B for inventory list and changed width in some columns
 *
 * Revision 1.17  2009/12/06 00:11:25  joelchou
 * Added inventory list
 *
 * Revision 1.16  2009/12/01 05:45:36  joelchou
 * Fixed issues on monthly inventory report. Changed to use older version of MySQL JDBC driver since Hibernate was complaining "column u_quantity not found" when using new version of driver.
 *
 * Revision 1.15  2008/10/22 06:13:37  joelchou
 * Changed to show display name instead of username.
 *
 * Revision 1.14  2008/04/18 06:55:44  joelchou
 * Fixed the issue on daily report. In the search criteria, the start date is inclusive and the end date is exclusive.
 *
 * Revision 1.13  2008/03/23 21:03:38  joelchou
 * Added the function to allow items in section B to be skipped in PDF report.
 *
 * Revision 1.12  2008/03/21 19:08:10  jlin
 * Set item category as sort column for reports
 *
 * Revision 1.11  2008/03/19 19:03:04  jlin
 * Add modified_by column to Asset Transation report.
 *
 * Revision 1.10  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 */
package store.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import members.datamodel.AccountInfo;
import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import store.common.report.ReportException;
import store.common.report.builder.AssetTransactionReportBuilder;
import store.common.report.builder.IReportBuilder;
import store.common.report.builder.InventoryReportBuilder;
import store.common.report.builder.SaleReportBuilder;
import store.common.report.builder.SingleTableReportBuilder;
import store.common.report.model.ReportHandler;
import store.common.report.model.SearchCriteria;
import store.common.report.model.ReportColumn.DataType;
import store.common.report.model.SearchCriteria.OperatorEnum;
import store.common.report.render.IReport;
import store.common.report.render.MSExcelReport;
import store.common.report.utils.DateEntity;
import store.datamodel.Invoice;
import store.datamodel.InvoiceItem;
import store.datamodel.ItemInfo;
import store.datamodel.ItemTypeInfo;
import store.utils.HibernateUtil;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This is a manager class for reports.
 * 
 * @author Cheng-Hung Chou, Jeff Ln
 * @version $Date: 2009/12/06 06:14:52 $
 * @since 5/20/2007
 *
 */
public class ReportsManager {
    private static Logger logger = Logger.getLogger(ReportsManager.class);
    private static ReportsManager instance;
    private ReportHandler saleItemsReportHandler;
    private ReportHandler dalilySaleItemsReportHandler;
    private ReportHandler saleHandlerReportHandler;
    private ReportHandler simpleAssetReportHandler;
    private ReportHandler assetTransactionReportHandler;
    private ReportHandler inventoryReportHandler;

    public static final String REGION_A = "A 區";
    public static final String REGION_B = "B 區";

    // for PDF fonts
    // CJK fonts
    private static Font hugeBoldChineseFont;
    private static Font hugeChineseFont;
    private static Font largeBoldChineseFont;
    private static Font largeChineseFont;
    private static Font normalBoldChineseFont;
    private static Font normalChineseFont;
    private static Font smallBoldChineseFont;
    private static Font smallChineseFont;
    static {
        try {
            BaseFont bfChinese = BaseFont.createFont("MSungStd-Light",
                "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
            hugeBoldChineseFont = new Font(bfChinese, 16, Font.BOLD);
            hugeChineseFont = new Font(bfChinese, 16, Font.NORMAL);
            largeBoldChineseFont = new Font(bfChinese, 14, Font.BOLD);
            largeChineseFont = new Font(bfChinese, 14, Font.NORMAL);
            normalBoldChineseFont = new Font(bfChinese, 12, Font.BOLD);
            normalChineseFont = new Font(bfChinese, 12, Font.NORMAL);
            smallBoldChineseFont = new Font(bfChinese, 10, Font.BOLD);
            smallChineseFont = new Font(bfChinese, 10, Font.NORMAL);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    private static Font hugeBoldFont = FontFactory.getFont(
        FontFactory.TIMES_ROMAN, 16, Font.BOLD);
    private static Font hugeFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,
        16);
    private static Font largeFont = FontFactory.getFont(
        FontFactory.TIMES_ROMAN, 14);
    private static Font normalBoldFont = FontFactory.getFont(
        FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    private static Font normalFont = FontFactory.getFont(
        FontFactory.TIMES_ROMAN, 12);
    private static Font smallFont = FontFactory.getFont(
        FontFactory.TIMES_ROMAN, 10);

    private ReportsManager() {

    }

    public static synchronized ReportsManager getInstance() {
        if(instance == null)
            instance = new ReportsManager();

        return instance;
    }

    private ReportHandler getSaleItemsReportHandler() {
        if(saleItemsReportHandler == null) {
            saleItemsReportHandler = new ReportHandler(InvoiceItem.class);
            saleItemsReportHandler.addBeanClass(ItemTypeInfo.class);
            saleItemsReportHandler.addBeanClass(Invoice.class);
            saleItemsReportHandler.addBeanClass(ItemInfo.class);

            saleItemsReportHandler.addReportColumn("itemtypeinfo.typeName",
                "類    別", DataType.TEXT, false, false, 12);
            saleItemsReportHandler.addReportColumn("iteminfo.itemNo", "編    號",
                DataType.TEXT, false, false, 12);
            saleItemsReportHandler.addReportColumn("iteminfo.name", "名    稱",
                DataType.TEXT, false, false, 50);
            saleItemsReportHandler.addReportColumn("invoiceitem.price",
                "售    價", DataType.CURRENCY, false, false);
            saleItemsReportHandler.addReportColumn("invoiceitem.quantity",
                "數    量", DataType.INTEGER, true, false);
            saleItemsReportHandler.addReportColumn("invoiceitem.total",
                "金    額", DataType.CURRENCY, true, false);
            saleItemsReportHandler.addReportColumn("dummy", "備    註",
                DataType.TEXT, false, true);

            saleItemsReportHandler
                .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
            saleItemsReportHandler
                .setSortColumn("iteminfo.typeInfo, iteminfo.itemId");
        }

        return saleItemsReportHandler;
    }

    private ReportHandler getSaleHandlerReportHandler() {
        if(saleHandlerReportHandler == null) {
            saleHandlerReportHandler = new ReportHandler(InvoiceItem.class);
            saleHandlerReportHandler.addBeanClass(ItemTypeInfo.class);
            saleHandlerReportHandler.addBeanClass(Invoice.class);
            saleHandlerReportHandler.addBeanClass(ItemInfo.class);

            saleHandlerReportHandler.addReportColumn("invoice.invoiceDate",
                "日    期", DataType.DATE, false, false);
            saleHandlerReportHandler.addReportColumn("iteminfo.itemNo",
                "編    號", DataType.TEXT, false, false, 12);
            saleHandlerReportHandler.addReportColumn("iteminfo.name", "名    稱",
                DataType.TEXT, false, false, 50);
            saleHandlerReportHandler.addReportColumn("invoiceitem.price",
                "單    價", DataType.CURRENCY, false, false);
            saleHandlerReportHandler.addReportColumn("invoiceitem.quantity",
                "數量", DataType.INTEGER, true, false);
            saleHandlerReportHandler.addReportColumn("invoiceitem.total",
                "總金額", DataType.CURRENCY, true, false);
            saleHandlerReportHandler.addReportColumn("invoice.purchasedBy",
                "請購人", DataType.TEXT, false, false);
            saleHandlerReportHandler.addReportColumn("invoice.handledBy",
                "收款人", DataType.TEXT, false, false);
            saleHandlerReportHandler.addReportColumn("dummy", "總簽收",
                DataType.TEXT, false, true);

            saleHandlerReportHandler
                .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
            saleHandlerReportHandler
                .setSortColumn("iteminfo.typeInfo, iteminfo.itemId");
        }

        return saleHandlerReportHandler;
    }

    private ReportHandler getDailySaleItemsReportHandler() {
        if(dalilySaleItemsReportHandler == null) {
            dalilySaleItemsReportHandler = new ReportHandler(InvoiceItem.class);
            dalilySaleItemsReportHandler.addBeanClass(ItemTypeInfo.class);
            dalilySaleItemsReportHandler.addBeanClass(Invoice.class);
            dalilySaleItemsReportHandler.addBeanClass(ItemInfo.class);

            dalilySaleItemsReportHandler.addReportColumn("iteminfo.itemNo",
                "編    號", DataType.TEXT, false, false, 12);
            dalilySaleItemsReportHandler.addReportColumn("iteminfo.name",
                "名    稱", DataType.TEXT, false, false, 50);
            dalilySaleItemsReportHandler.addReportColumn("invoiceitem.price",
                "單    價", DataType.CURRENCY, false, false);
            dalilySaleItemsReportHandler.addReportColumn(
                "invoiceitem.quantity", "數量", DataType.INTEGER, true, false);
            dalilySaleItemsReportHandler.addReportColumn("invoiceitem.total",
                "總金額", DataType.CURRENCY, true, false);
            dalilySaleItemsReportHandler.addReportColumn("invoice.purchasedBy",
                "請購人", DataType.TEXT, false, false);
            dalilySaleItemsReportHandler.addReportColumn("invoice.handledBy",
                "收款人", DataType.TEXT, false, false);
            dalilySaleItemsReportHandler.addReportColumn("dummy", "總簽收",
                DataType.TEXT, false, true);
            dalilySaleItemsReportHandler.addReportColumn("dummy1", "備    註",
                DataType.TEXT, false, true);

            dalilySaleItemsReportHandler
                .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
            dalilySaleItemsReportHandler
                .setSortColumn("iteminfo.typeInfo, iteminfo.itemId");
        }

        return dalilySaleItemsReportHandler;
    }

    private ReportHandler getSimpleAssetReportHandler() {
        if(simpleAssetReportHandler == null) {
            simpleAssetReportHandler = new ReportHandler(ItemInfo.class);

            simpleAssetReportHandler.addReportColumn("iteminfo.itemNo",
                "編    號", DataType.TEXT, false, false, 12);
            simpleAssetReportHandler.addReportColumn("iteminfo.name", "名    稱",
                DataType.TEXT, false, false, 50);
            simpleAssetReportHandler.addReportColumn("iteminfo.barcode",
                "條    碼", DataType.TEXT, true, false, 20);
            simpleAssetReportHandler.addReportColumn("iteminfo.price",
                "單    價", DataType.CURRENCY, false, false);
            simpleAssetReportHandler.addReportColumn("iteminfo.quantity",
                "總數量", DataType.INTEGER, true, false);

            simpleAssetReportHandler
                .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
            simpleAssetReportHandler
                .setSortColumn("iteminfo.typeInfo, iteminfo.itemId");
        }

        return simpleAssetReportHandler;
    }

    private ReportHandler getAssetTransactionReportHandler() {
        if(assetTransactionReportHandler == null) {
            assetTransactionReportHandler = new ReportHandler(ItemInfo.class);

            assetTransactionReportHandler.setNativeQuery(true);
            assetTransactionReportHandler.addReportColumn("typeinfo.type_name",
                "類    別", DataType.TEXT, false, false, 12);
            assetTransactionReportHandler.addReportColumn("iteminfo.item_no",
                "編    號", DataType.TEXT, false, false, 12);
            assetTransactionReportHandler.addReportColumn("iteminfo.name",
                "名    稱", DataType.TEXT, false, false, 50);
            assetTransactionReportHandler.addReportColumn(
                "history1.u_quantity 1_quantity", "當月更動", DataType.INTEGER,
                false, false);
            assetTransactionReportHandler.addReportColumn(
                "history1.invoice_no", "進貨收據", DataType.TEXT, false, false, 20);
            assetTransactionReportHandler.addReportColumn(
                "history1.modified_by", "變更人", DataType.TEXT, false, false);
            assetTransactionReportHandler.addReportColumn(
                "invoiceitem1.quantity 1i_quantity", "當月銷貨", DataType.INTEGER,
                false, false);
            //iteminfo.quantity, , history2.u_quanity 2_quantity, , invoiceitem2.quantity 2i_quantity
            assetTransactionReportHandler
                .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
            assetTransactionReportHandler.setGroupColumn("iteminfo.item_id");
            assetTransactionReportHandler
                .setSortColumn("iteminfo.type_id, iteminfo.item_id");

        }

        return assetTransactionReportHandler;
    }

    private ReportHandler getInventoryReportHandler(int year) {
            
        inventoryReportHandler = new ReportHandler(ItemInfo.class);

        inventoryReportHandler.setNativeQuery(true);
        inventoryReportHandler.addReportColumn("typeinfo.type_name",
            "類    別", DataType.TEXT, false, false, 12);
        inventoryReportHandler.addReportColumn("iteminfo.item_no",
            "編    號", DataType.TEXT, false, false, 12);
        inventoryReportHandler.addReportColumn("iteminfo.name",
            "名    稱", DataType.TEXT, false, false, 50);
        inventoryReportHandler.addReportColumn(
            "last_year", (year - 1) + "實際盤點", DataType.INTEGER,
            false, true);             
        inventoryReportHandler.addReportColumn(
            "item_in", "進貨", DataType.TEXT,
            false, false, 35);            
        inventoryReportHandler.addReportColumn(
            "in_quantity", "小計", DataType.INTEGER,
            false, false);
        inventoryReportHandler.addReportColumn(
            "item_out", "銷貨", DataType.TEXT, false, false, 20);
        inventoryReportHandler.addReportColumn(
                "out_quantity", "小計", DataType.INTEGER, false, false);
        inventoryReportHandler.addReportColumn(
            "iteminfo.quantity", "庫存總數", DataType.INTEGER,
            false, false);
        inventoryReportHandler.addReportColumn(
            "this_year", "實際盤點", DataType.INTEGER,
            false, true);            

        inventoryReportHandler
            .setRenderType(IReport.ReportRenderEnum.EXCEL_RENDER);
        // GROUP BY
        inventoryReportHandler.setGroupColumn("iteminfo.item_id");
        // ORDER BY
        inventoryReportHandler
            .setSortColumn("iteminfo.type_id, iteminfo.item_id");

        return inventoryReportHandler;
    }    
    
    
    public IReport getSaleItemsReportByDate(String region, DateEntity date) {
        ReportHandler handler = getDailySaleItemsReportHandler();

        /*==================
         * set up the criteria
         *================+*/
        List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
        SearchCriteria a = new SearchCriteria("invoice.invoiceDate",
            OperatorEnum.GREATER_EQUAL, date.getStartDate(), OperatorEnum.LESS,
            date.getEndDate());
        criterias.add(a);

        SearchCriteria b = null;
        if(REGION_B.equals(region)) {
            b = new SearchCriteria("iteminfo.itemNo", OperatorEnum.LIKE, "%b");
            criterias.add(b);
        }
        else {
            b = new SearchCriteria("iteminfo.itemNo", OperatorEnum.NOT_LIKE,
                "%b");
            criterias.add(b);
        }

        handler.setCriteria(criterias);

        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            }
            catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new SaleReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        Map<String, String> parameters = report.getParameters();
        StringBuffer title = new StringBuffer("每日靜思文化出售記錄表");
        if(date != null)
            title.append(": ").append(date.getFormatedDateString());

        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Daily");

        return report;
    }

    public IReport getSaleItemsReportByMonth(String region, DateEntity month) {
        ReportHandler handler = getSaleItemsReportHandler();

        /*==================
         * set up the criteria
         *================+*/
        List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
        SearchCriteria a = new SearchCriteria("invoice.invoiceDate",
            OperatorEnum.GREATER_EQUAL, month.getMonthStartDate(),
            OperatorEnum.LESS_EQUAL, month.getMonthEndDate());
        criterias.add(a);

        SearchCriteria b = null;
        if(REGION_B.equals(region)) {
            b = new SearchCriteria("iteminfo.itemNo", OperatorEnum.LIKE, "%b");
            criterias.add(b);
        }
        else {
            b = new SearchCriteria("iteminfo.itemNo", OperatorEnum.NOT_LIKE,
                "%b");
            criterias.add(b);
        }

        handler.setCriteria(criterias);

        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            }
            catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new SaleReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        StringBuffer title = new StringBuffer("每月靜思文化出售記錄表");
        if(month != null)
            title.append(": ").append(month.getFormatedMonthString());
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Monthly");

        return report;
    }

    /**
     * get the Sales Item Report by sale handler within a month period
     * @param region 
     * @param handler
     * @param month
     * @return
     */
    public IReport getSaleItemsReportByHandler(String handlerName,
        DateEntity month) {
        ReportHandler handler = getSaleHandlerReportHandler();

        /*==================
         * set up the criteria
         *================+*/
        List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
        SearchCriteria a = new SearchCriteria("invoice.invoiceDate",
            OperatorEnum.GREATER_EQUAL, month.getMonthStartDate(),
            OperatorEnum.LESS_EQUAL, month.getMonthEndDate());
        SearchCriteria b = new SearchCriteria("invoice.handledBy",
            OperatorEnum.EQUAL, handlerName);

        criterias.add(a);
        criterias.add(b);

        handler.setCriteria(criterias);

        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            }
            catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new SaleReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        StringBuffer title = new StringBuffer("每月靜思文化出售記錄收款人名細: ");
        title.append(handlerName);
        if(month != null)
            title.append(": ").append(month.getFormatedMonthString());
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Monthly");

        return report;
    }

    /**
     * get the up-to-date Simple Asset Report
     * @param region 
     * @param handler
     * @param month
     * @return
     */
    public IReport getSimpleAssetReportByHandler(String region) {
        ReportHandler handler = getSimpleAssetReportHandler();

        List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
        // to make it case-insensitive, make sure table collation is *_ci
        SearchCriteria a = null;
        if(REGION_B.equals(region)) {
            a = new SearchCriteria("iteminfo.itemNo", OperatorEnum.LIKE, "%b");
            criterias.add(a);
        }
        else {
            a = new SearchCriteria("iteminfo.itemNo", OperatorEnum.NOT_LIKE,
                "%b");
            criterias.add(a);
        }        
        handler.setCriteria(criterias);
        
        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            }
            catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new SingleTableReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        StringBuffer title = new StringBuffer("靜思文化庫存量");
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Inventory");

        return report;
    }
    
    public IReport getInventoryList(DateEntity date) {
        
        List<SearchCriteria> extraParams = new ArrayList<SearchCriteria>();
        SearchCriteria a = new SearchCriteria("a", OperatorEnum.GREATER_EQUAL,
            date.getYearStartDate());
        SearchCriteria b = new SearchCriteria("b", OperatorEnum.LESS,
            date.getNextYearStartDate());
        SearchCriteria c = new SearchCriteria("c", OperatorEnum.GREATER_EQUAL,
            date.getYearStartDate());
        SearchCriteria d = new SearchCriteria("d", OperatorEnum.LESS,
            date.getNextYearStartDate());

        extraParams.add(a);
        extraParams.add(b);
        extraParams.add(c);
        extraParams.add(d);

        ReportHandler handler = getInventoryReportHandler(date.getYear());        
        handler.setExtraParams(extraParams);

        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            } catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new InventoryReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        StringBuffer title = new StringBuffer("盤點清單");
        if(date != null)
            title.append(": ").append(date.getFormatedYearString());
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Inventory List");

        return report;
    }

    /**
     * get the up-to-date Simple Asset Report
     * @param region 
     * @param handler
     * @param month
     * @return
     */
    public IReport getAssetTransactionReport(DateEntity month) {
        ReportHandler handler = getAssetTransactionReportHandler();

        /*==================
         * extra parameters
         *================+*/
        List<SearchCriteria> extraParams = new ArrayList<SearchCriteria>();
        SearchCriteria a = new SearchCriteria("a", OperatorEnum.GREATER_EQUAL,
            month.getMonthStartDate());
        SearchCriteria b = new SearchCriteria("b", OperatorEnum.LESS,
            month.getNextMonthFirstDate());
        SearchCriteria c = new SearchCriteria("c", OperatorEnum.GREATER_EQUAL,
            month.getMonthStartDate());
        SearchCriteria d = new SearchCriteria("d", OperatorEnum.LESS,
            month.getNextMonthFirstDate());

        extraParams.add(a);
        extraParams.add(b);
        extraParams.add(c);
        extraParams.add(d);

        handler.setExtraParams(extraParams);

        //turn on validation in debug mode
        if(logger.isDebugEnabled()) {
            try {
                handler.validate();
            }
            catch(Exception e) {
                logger.error(e);
            }
        }

        IReportBuilder builder = new AssetTransactionReportBuilder(handler);

        builder.executeQuery();
        IReport report = builder.getReport();

        StringBuffer title = new StringBuffer("每月靜思文化進銷量");
        if(month != null)
            title.append(": ").append(month.getFormatedMonthString());
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_DOC_TITLE, title.toString());
        parameters.put(MSExcelReport.PARAM_DOC_SHORT_NAME, "Asset in/out");

        return report;
    }

    public void renderSaleItemReportByHandler(String fileName,
        String handlerName, DateEntity month) throws ReportException {
        IReport report = getSaleItemsReportByHandler(handlerName, month);

        renderSaleItemsReport(report, fileName);
    }

    public void renderSaleItemsReportByMonth(String fileName, String region,
        DateEntity month) throws ReportException {
        IReport report = getSaleItemsReportByMonth(region, month);

        renderSaleItemsReport(report, fileName);

    }

    public void renderSaleItemsReportByDate(String fileName, String region,
        DateEntity date) throws ReportException {
        IReport report = getSaleItemsReportByDate(region, date);

        renderSaleItemsReport(report, fileName);

    }

    /**
     * Render Generic Sale Report (Monthly or Daliy) 
     * @param report
     * @param fileName
     * @param date
     * @throws ReportException
     */
    public void renderSaleItemsReport(IReport report, String fileName)
        throws ReportException {
        Map<String, String> parameters = report.getParameters();
        parameters.put(MSExcelReport.PARAM_FILE_NAME, fileName);

        String msg = (String)report.renderReport(null);

        if(msg != null)
            throw new ReportException(msg);
    }

    public void renderAssetTransactionReportByMonth(String fileName,
        DateEntity month) throws ReportException {
        IReport report = getAssetTransactionReport(month);

        renderSaleItemsReport(report, fileName);
    }

    /**
     * get the list of sales handler by month
     * @param month
     * @return
     */
    public static List<String> getListOfSaleHandlersByMonth(DateEntity month) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<String> list = session.createQuery(
            "SELECT invoice.handledBy FROM Invoice invoice WHERE invoice.invoiceDate >= ? "
                + " and invoice.invoiceDate <= ? group by invoice.handledBy")
            .setDate(0, month.getMonthStartDate()).setDate(1,
                month.getMonthEndDate()).list();
        session.getTransaction().commit();
        return list;
    }

    /**
     * Generates the PDF file for an invoice. 
     * 
     * @param invoice
     * @param file
     * @parm includeSectionB true if items in section B need to be included
     * @throws DocumentException
     * @throws IOException
     */
    public void generateReceipt(Invoice invoice, File file,
        boolean includeSectionB) throws DocumentException, IOException {
        // creation of a document object for PDF
        Document doc = new Document(PageSize.LETTER, 10, 10, 20, 10);
        // create a PDF writer that listens to this document
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        // set file properties
        doc.addAuthor("Tzu Chi");
        doc.addSubject(invoice.getInvoiceNumber());
        // open this document
        doc.open();
        // logo
        Image logo = Image.getInstance(this.getClass().getResource(
            "/store/ui/images/tzuchi.jpg"));
        Paragraph logoPar = new Paragraph();
        logoPar.setAlignment(Paragraph.ALIGN_CENTER);
        logo.setAlignment(Image.MIDDLE);
        logo.scalePercent(50);
        logoPar.add(new Chunk(logo, 0, -50f));
        doc.add(logoPar);
        // title
        Paragraph titlePar = new Paragraph();
        titlePar.add("\n\n\n\n");
        titlePar.add(new Chunk("號碼Number: " + invoice.getInvoiceNumber(),
            normalBoldChineseFont));
        titlePar.add(new Chunk("     日期Date: "
            + Utility.getDateString(invoice.getInvoiceDate()),
            normalChineseFont));
        titlePar.add(new Chunk("\n"));
        AccountInfo accountInfo = 
            ContextManager.getAccountInfo(invoice.getHandledBy());
        String cashier = "";
        if(accountInfo != null)
            cashier = accountInfo.getMemberInfo().getName();
        else
            cashier = invoice.getHandledBy();
        titlePar.add(new Chunk("經收人Cashier: " + cashier, normalChineseFont));        
        titlePar.add(new Chunk("\n\n"));
        doc.add(titlePar);
        // items table
        float[] widths = {0.1f, 0.5f, 0.1f, 0.1f, 0.1f};
        PdfPTable itemsTable = new PdfPTable(widths);
        itemsTable.setWidthPercentage(100); // 100% of the available space
        // headers
        itemsTable.addCell(new PdfPCell(
            new Phrase("編號#", normalBoldChineseFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("名稱Name",
            normalBoldChineseFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("單價Price",
            normalBoldChineseFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("數量Quantity",
            normalBoldChineseFont)));
        itemsTable.addCell(new PdfPCell(new Phrase("金額Subtotal",
            normalBoldChineseFont)));
        Iterator<InvoiceItem> it = invoice.getItems().iterator();
        double total = 0;
        while(it.hasNext()) {
            InvoiceItem item = it.next();
            ItemInfo info = item.getItemInfo();
            String itemNo = item.getItemInfo().getItemNo();
            // skip items in section B if they don't need to be included
            if(!Utility.isEmpty(itemNo)) {
                if(!includeSectionB && itemNo.toLowerCase().endsWith("b"))
                    continue;
            }
            itemsTable.addCell(new PdfPCell(new Phrase(itemNo,
                normalChineseFont)));
            itemsTable.addCell(new PdfPCell(new Phrase(info.getName(),
                normalChineseFont)));
            PdfPCell priceCell = new PdfPCell(new Phrase(Utility
                .numberFormatter(item.getPrice()), normalFont));
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemsTable.addCell(priceCell);
            PdfPCell quantityCell = new PdfPCell(new Phrase(Utility
                .numberFormatter(item.getQuantity()), normalFont));
            quantityCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemsTable.addCell(quantityCell);
            double subtotal = item.getTotal();
            PdfPCell subtotalCell = new PdfPCell(new Phrase(Utility
                .numberFormatter(subtotal), normalFont));
            subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemsTable.addCell(subtotalCell);
            total = total + subtotal;
        }
        PdfPCell totalCell = new PdfPCell(new Paragraph("總金額Total: "
            + Utility.numberFormatter(total), normalBoldChineseFont));
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setColspan(5);
        itemsTable.addCell(totalCell);
        doc.add(itemsTable);

        // close this document
        doc.close();
    }
}
