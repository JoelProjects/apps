/* $Id: TabChangeListener.java,v 1.4 2008/04/07 05:29:22 joelchou Exp $ */
package store.ui;

import java.awt.Component;
import java.util.Calendar;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is a listener for tabbed pane.
 * 
 * @author Cheng-Hung Chou
 * @since 2/17/2008
 */
public class TabChangeListener implements ChangeListener
{
   public TabChangeListener()
   {
   }

   public void stateChanged(ChangeEvent e)
   {
      JTabbedPane pane = (JTabbedPane)e.getSource();
      Component comp = pane.getSelectedComponent();

      if(comp instanceof ReportsPanel)
      {
         ReportsPanel report = (ReportsPanel)comp;
         // time portion is not used
         Calendar today = Calendar.getInstance();
         Calendar startDate = (Calendar)today.clone();
         startDate.set(Calendar.DAY_OF_MONTH, 1);       
         report.setReportStartDate(startDate.getTime());
         report.setReportEndDate(today.getTime());
         
         report.refresh();
      }
   }
}
