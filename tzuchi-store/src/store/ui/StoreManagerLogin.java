/* $Id: StoreManagerLogin.java,v 1.7 2010/08/08 05:55:17 joelchou Exp $ */
package store.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import members.ui.auth.AbstractLoginDialog;

/**
 * This is the login page.
 * 
 * @author Cheng-Hung Chou
 * @since 2/19/2007
 */
public class StoreManagerLogin extends AbstractLoginDialog
{
   private JLabel userLabel = new JLabel("帳號名稱: ", SwingConstants.RIGHT);
   private JLabel passwordLabel = new JLabel("密碼: ", SwingConstants.RIGHT);
   private JTextField usernameField = new JTextField(50);
   private JPasswordField passwordField = new JPasswordField(50);
   private JButton okButton = new JButton("OK");
   private JButton cancelButton = new JButton("Cancel");
   private static final int WIDTH = 350;
   private static final int HEIGHT = 300;

   public StoreManagerLogin()
   {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setBounds((screenSize.width-WIDTH)/2, (screenSize.height-HEIGHT)/2, WIDTH,
          HEIGHT);       
      
      setModal(true);

      Container pane = getContentPane();
      pane.setLayout(new BorderLayout());
      
      // logo panel
      JPanel logoPanel = new JPanel();
      logoPanel.setBackground(Color.WHITE);
      logoPanel.add(new JLabel("<html><center><FONT color=blue><B>慈濟<br>"
            + "書軒庫存管理" + "</B></FONT></center>", JLabel.CENTER));
      logoPanel.add(new JLabel(new ImageIcon(this.getClass().getResource(
            "images/tzuchi.jpg"))));
      pane.add(logoPanel, BorderLayout.NORTH);
      
      // login panel
      JPanel loginPanel = new JPanel();
      Border outerBorder = new EmptyBorder(15, 15, 15, 15);
      Border innerBorder = new TitledBorder(new EtchedBorder(), 
               "登入系統  ", TitledBorder.LEFT, TitledBorder.TOP);
      loginPanel.setBorder(new CompoundBorder(outerBorder, innerBorder));
      loginPanel.setLayout(new GridLayout(2, 2));
      // username
      loginPanel.add(userLabel);
      loginPanel.add(usernameField);
      usernameField.setText("");
      // password
      loginPanel.add(passwordLabel);
      loginPanel.add(passwordField);
      pane.add(loginPanel, BorderLayout.CENTER);
      
      // buttons panel
      JPanel buttonsPanel = new JPanel();
      buttonsPanel.add(okButton);
      buttonsPanel.add(cancelButton);
      pane.add(buttonsPanel, BorderLayout.SOUTH);

      // add action listener
      ActionListener al = new OKResponse();
      okButton.addActionListener(al);
      //usernameField.addActionListener(al);
      //passwordField.addActionListener(al);
      cancelButton.addActionListener(new CancelResponse());

      // window listener
      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            cancelled = true;
         }
      });
   }

   class OKResponse implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         try
         {
            Thread.sleep(500);
         }
         catch(InterruptedException ex)
         {
         }

         username = usernameField.getText();
         password = String.valueOf(passwordField.getPassword());

         // erase password
         passwordField.setText("");

         setVisible(false);
      }
   }

   class CancelResponse implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         setVisible(false);

         cancelled = true;
      }
   }
}
