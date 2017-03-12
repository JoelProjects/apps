package asset.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;

public class BorrowedAssetsDialog extends JDialog
{
   public BorrowedAssetsDialog(Frame owner, boolean modal, long memberId)
   {
      super(owner, modal);

      setTitle("Borrowed Assets");

      init(memberId);
   }
   
   private void init(long memberId)
   {
      Container content = getContentPane();
      // set layout
      content.setLayout(new BorderLayout());      
      content.add(new BorrowedAssetsPanel(memberId), BorderLayout.CENTER);
   }
}
