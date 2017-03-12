package members.ui.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class GuiUtil
{
   /**
    * Gets owner frame of a specific component.
    *
    * @param component the component to be checked
    *
    * @return a Frame object
    */
   public static Frame getOwnerFrame(Component component)
   {
      while((component != null) && !(component instanceof Frame))
      {
         component = component.getParent();
      }

      return (Frame)component;
   }

   /**
    * Pops up a dialog to display error message.
    *
    * @param parent the parent component
    * @param mesgList a list of messages to be shown
    */
   public static void showErrorMessage(Component parent, java.util.List mesgList)
   {
      StringBuffer buffer = new StringBuffer();

      buffer.append("<HTML>");
      for(int i = 0; i < mesgList.size(); i++)
      {
         buffer.append("<P>* ").append((String)mesgList.get(i)).append("</P>");
      }

      messageDialog(parent, buffer.toString());
   }

   /**
    * Pops up a dialog to display error message.
    *
    * @param parent the parent component
    * @param mesg error message to be shown
    */
   public static void showErrorMessage(Component parent, String mesg)
   {
      StringBuffer buffer = new StringBuffer();

      // use \n to replace <BR>
      buffer.append("<HTML>");
      StringTokenizer st = new StringTokenizer(mesg, "\n");
      while(st.hasMoreTokens())
      {
         buffer.append("<P>* ").append(st.nextToken()).append("</P>");
      }

      messageDialog(parent, buffer.toString());
   }

   /**
    * Shows a list of messages in a dialog.
    *
    * @param parent the parent component
    * @param mesg a list of messages
    */
   public static void messageDialog(Component parent, String mesg)
   {
      JTextArea textArea = new JTextArea(mesg);
      //textArea.setVerticalAlignment(SwingConstants.TOP);
      textArea.setPreferredSize(new Dimension(300, 55));
      textArea.setLineWrap(true);
      JScrollPane mesgPane = new JScrollPane(textArea);
      //mesgPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      //mesgPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      JOptionPane.showMessageDialog(parent, mesgPane, "Error",
            JOptionPane.ERROR_MESSAGE);
   }

   /**
    * Print out error message.
    *
    * @param mesg error message to be shown
    */
   public static void showErrorMessage(String mesg)
   {
      System.out.println(mesg);
   }

   public static void setColumnWidths(JTable table, int[] width)
         throws ArrayIndexOutOfBoundsException
   {
      if(width == null)
         return;

      if(width.length > 0 && width.length <= table.getColumnCount())
      {
         for(int i = 0; i < width.length; i++)
         {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
         }
      }
      else
      {
         throw new ArrayIndexOutOfBoundsException();
      }
   }
   
   public static String htmlFont(String color, String text)
   {
      return "<html><font color='" + color + "'>" + text + "</font>";
   }
   
   public static String htmlFont(String color, String size, String text)
   {
      return "<html><font color='" + color + "' size='" + size + "'>" + text + "</font>";
   }   
}
