package jhongwun.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class SubjectPanel extends JPanel
{
   private JLabel subjectLabel;

   public SubjectPanel(String title)
   {
      setBorder(BorderFactory.createRaisedBevelBorder());
      
      subjectLabel = new JLabel("<html><b><font size=+2>" + title);
      add(subjectLabel);
   }

   public void updateSubject(int number, String subject)
   {
      subjectLabel.setText("<html><b><font size=+2>ÃD¥Ø" + number  + ". " + subject);
   }
   
   public void updateSubject(String subject)
   {
      subjectLabel.setText("<html><b><font size=+2>" + subject);
   }   
}
