package asset.ui;

import javax.swing.JPanel;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/24/2005
 */
abstract public class AbstractAssetPanel extends JPanel
{
   // main panel used to contain other panels
   protected static JPanel mainPanel;
     
   abstract public JPanel getButtonsPanel();
}
