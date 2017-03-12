package jhongwun.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jhongwun.manager.ContextManager;
import jhongwun.model.QuestionSet;
import jhongwun.util.GuiUtil;

/**
 * This is the main program.
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class JhongWunTest
{
   private static final int APP_WIDTH = 800;
   private static final int APP_HEIGHT = 600;
   private static JFrame frame;

   public static void main(String[] args)
   {
      new JhongWunTest();
   }

   public JhongWunTest()
   {
      // initialize application context
      try
      {
         ContextManager ctxMgr = new ContextManager();
      }
      catch(Exception e)
      {
         GuiUtil.messageDialog(null, e.getMessage());
         System.exit(1);
      }

      try
      {
         // use system related look and feel
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch(Exception e)
      {
      }

      QuestionSet questionSet = ContextManager.getQuestionSet();
      String title = questionSet.getDescription();
      int numOfQuestions = questionSet.getNumberOfQuestions();
      double time = questionSet.getTime() / 1000.0;  // convert to seconds
      // splash screen
      SplashScreen splash = new SplashScreen(title, numOfQuestions, time);
      splash.setVisible(true);

      // wait until ready
      while(!splash.isClosed())
      {
         try
         {
            Thread.sleep(500);
         }
         catch(Exception e)
         {
         }
      }
      splash.hide();
      splash.dispose();

      frame = new JFrame();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      frame.setBounds(screenSize.width/2 - APP_WIDTH/2, screenSize.height/2 - APP_HEIGHT/2 - 50, 
            APP_WIDTH, APP_HEIGHT);
      frame.setTitle("中文能力測驗小擂台");
      frame.setIconImage(new ImageIcon(this.getClass().getResource(
            "images/icon.gif")).getImage());
      frame.setResizable(false);
      Container content = frame.getContentPane();
      content.setLayout(new BorderLayout());

      // add panels
      SubjectPanel subjectPanel = new SubjectPanel(title);
      content.add(subjectPanel, BorderLayout.NORTH);
      ChoicesPanel choicesPanel = new ChoicesPanel();
      content.add(choicesPanel, BorderLayout.CENTER);
      TimerPanel timerPanel = new TimerPanel();
      content.add(timerPanel, BorderLayout.SOUTH);
      AnswersPanel answersPanel = new AnswersPanel();
      content.add(answersPanel, BorderLayout.EAST);
      
      // register panels
      ContextManager.setSubjectPanel(subjectPanel);
      ContextManager.setChoicesPanel(choicesPanel);
      ContextManager.setTimerPanel(timerPanel);
      ContextManager.setAnswersPanel(answersPanel);
      
      // show question 1
      timerPanel.startTimer();
      choicesPanel.showQuestion(1);

      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
      frame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            int ans = JOptionPane.showConfirmDialog(null, "結束本測驗?", "結束", JOptionPane.YES_NO_OPTION);
            if(ans == JOptionPane.YES_OPTION)
               System.exit(0);
         }
      });

      frame.setSize(APP_WIDTH, APP_HEIGHT);
      frame.setVisible(true);
   }
}
