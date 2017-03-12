package asset.ui;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is a listener for tabbed pane.
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
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

      if(!(comp instanceof ServiceCenterPanel))
      {
         // confirm before switching
         /*int option = JOptionPane.showConfirmDialog(null,
               "Are you sure to switch to other member?", "Switch Member", 
               JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);   
         if(option == JOptionPane.OK_OPTION)
            return;*/
      }
   }
}
