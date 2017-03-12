package jhongwun.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.JButton;

import jhongwun.util.GuiUtil;

/**
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class SplashScreen extends JWindow
{
   private boolean isClosed = false;

   public SplashScreen(String title, int numOfQuestions, double time)
   {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setBounds(screenSize.width / 2 - 125, screenSize.height / 2 - 125, 250,
            250);

      Container content = getContentPane();
      // set layout
      content.setLayout(new BorderLayout());
      content.setBackground(GuiUtil.BACKGROUND_COLOR);
      content.add(new JLabel("<html><center><FONT><B>" + title + "</B></FONT></center>", JLabel.CENTER),
            BorderLayout.NORTH);      
      content.add(new JLabel("<html><center><FONT>本測驗包含 " + numOfQuestions + " 題<br>"
            + "每題 " + time + " 秒<br>" + "<br></FONT></center>", JLabel.CENTER),
            BorderLayout.CENTER);

      JButton startButton = new JButton("開始本測驗");
      content.add(startButton, BorderLayout.SOUTH);

      startButton.addActionListener(new MyActionListener());
   }

   public boolean isClosed()
   {
      return isClosed;
   }

   /**
    * This is the listener for Ok, Cancel and Help buttons.
    */
   class MyActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         isClosed = true;
      }
   }
}
