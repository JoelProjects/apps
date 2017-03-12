/* $Id: InvoiceManager.java,v 1.4 2008/10/24 05:55:21 joelchou Exp $
 * Created on Feb 2, 2008
 * 
 * $Log: InvoiceManager.java,v $
 * Revision 1.4  2008/10/24 05:55:21  joelchou
 * Changed for permission checking.
 *
 * Revision 1.3  2008/03/18 21:26:55  jlin
 * add Asset Transaction Report
 *
 * Revision 1.2  2008/03/11 01:25:14  joelchou
 * Added a function to generate receipts in PDF format.
 *
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JTable;

import members.datamodel.AccountInfo;
import members.manager.AccountManager;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.Invoice;
import store.ui.InvoiceTableModel;
import store.utils.HibernateUtil;

/**
 *
 *
 * @author Jeff Lin
 * @version  $Date: 2008/10/24 05:55:21 $
 */
public class InvoiceManager {
    private static Logger logger = Logger.getLogger(InvoiceManager.class);

    /**
     * 
     * @param reportsTable
     * @param accountInfo
     * @param startDate no time portion
     * @param endDate no time portion
     */
    public static void refreshReportsTable(JTable reportsTable,
        AccountInfo accountInfo, Date startDate, Date endDate) {
        Session session = null;
        try {
            AccountManager accountMgr = new AccountManager();
            String handledBy = null;
            if(!accountMgr.isInRole(accountInfo, ContextManager.getAdminRoles())) {
                handledBy = "handledBy = '" + accountInfo.getUsername() + "'";
            }
            
            session = HibernateUtil.getSession();
            session.beginTransaction();
            StringBuffer queryStr = new StringBuffer("FROM Invoice ");

            //build the query string
            if(startDate != null) {
                queryStr.append("WHERE invoiceDate >= ? ");
            }

            if(endDate != null) {
                if(startDate == null)
                    queryStr.append("WHERE invoiceDate < ? ");
                else
                    queryStr.append("AND invoiceDate < ? ");

                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                endDate = cal.getTime();
            }

            if(handledBy != null) {
                if(startDate == null && endDate == null)
                    queryStr.append("WHERE ").append(handledBy);
                else
                    queryStr.append("AND ").append(handledBy);
            }
            
            queryStr.append(" ORDER BY invoiceDate");

            //set the param
            int index = 0;
            Query query = session.createQuery(queryStr.toString());
            if(startDate != null)
                query.setDate(index++, startDate);
            if(endDate != null)
                query.setDate(index++, endDate);

            Iterator it = query.list().iterator();
            // clear table           
            InvoiceTableModel tableModel = (InvoiceTableModel)reportsTable
                .getModel();
            tableModel.clear();
            while(it.hasNext()) {
                Invoice item = (Invoice)it.next();
                tableModel.addRow(item);
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            if(session != null)
                session.getTransaction().rollback();
            logger.error(e);
        }
    }
}
