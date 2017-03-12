/* $Id: AbstractItemsPanel.java,v 1.3 2008/04/07 05:29:22 joelchou Exp $ */
package store.ui;

import javax.swing.JPanel;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/24/2005
 */
abstract public class AbstractItemsPanel extends JPanel
{
   // main panel used to contain other panels
   protected static JPanel mainPanel;
     
   abstract public JPanel getButtonsPanel();
}
