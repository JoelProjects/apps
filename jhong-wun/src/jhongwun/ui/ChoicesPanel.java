package jhongwun.ui;

import java.util.List;
import java.util.Vector;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jhongwun.manager.ContextManager;
import jhongwun.manager.ScoreManager;
import jhongwun.model.QuestionSet;
import jhongwun.model.Question;

/**
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class ChoicesPanel extends JPanel
{
   private static final int MAX_CHOICES = 4;
   private int number;  // current question number
   private QuestionSet questionSet;
   private ScoreManager scoreMgr;
   private JLabel subjectLabel;
   private JLabel[] checkedLabels = new JLabel[MAX_CHOICES];
   private JLabel[] choiceLabels = new JLabel[MAX_CHOICES];
   private ImageIcon checkedIcon;

   public ChoicesPanel()
   {
      questionSet = ContextManager.getQuestionSet();
      scoreMgr = ContextManager.getScoreManager();
      checkedIcon = new ImageIcon(this.getClass().getResource("images/checked.gif"));

      setLayout(new BorderLayout());
      setBorder(new EmptyBorder(5, 5, 5, 5));
      init();
   }

   private void init()
   {
      JPanel mainPanel = new JPanel();
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      mainPanel.setLayout(gridbag);
      cons.anchor = GridBagConstraints.WEST;
      
      // subject
      cons.gridx = 0;
      cons.gridy = 0;
      cons.gridwidth = 2;
      cons.insets = new Insets(2, 0, 10, 0);
      subjectLabel = new JLabel();
      gridbag.setConstraints(subjectLabel, cons);
      mainPanel.add(subjectLabel);      
      
      MouseListener choiceListener = new ChoiceListener();
      cons.gridwidth = 1;
      cons.insets = new Insets(2, 0, 2, 0);
      for(int i = 0; i < MAX_CHOICES; i++)
      {
         // checked box
         cons.gridx = 0;
         cons.gridy = i + 1;
         checkedLabels[i] = new JLabel();
         gridbag.setConstraints(checkedLabels[i], cons);
         mainPanel.add(checkedLabels[i]);
         // choice
         cons.gridx = 1;
         cons.gridy = i + 1;
         choiceLabels[i] = new JLabel();
         gridbag.setConstraints(choiceLabels[i], cons);
         mainPanel.add(choiceLabels[i]);

         // register listeners
         choiceLabels[i].addMouseListener(choiceListener);
      }

      add(mainPanel, BorderLayout.CENTER);
   }
   
   public int getCurrentQuestionNumber()
   {
      return number;
   }
   
   public void nextQuestion()
   {
      if(number < questionSet.getNumberOfQuestions())
      {
         number++;
         showQuestion(number);
      }
      else
         showResults();
   }

   public void showQuestion(int number)
   {   
      if(number <= questionSet.getNumberOfQuestions())
      {  
         // show current results
         ContextManager.getTimerPanel().updateResults();
         
         // reset timer
         ContextManager.getTimerPanel().resetTimer();
         
         this.number = number;

         Question question = questionSet.getQuestion(number);  // 1-based

         // update subject panel
         String subject = question.getSubject();
         subjectLabel.setText("<html><b><font size=+2>第" + number  + "題. " + subject);

         // update choices panel
         List choices = question.getChoices();
         for(int i = 0; i < choices.size(); i++)
         {
            String choice = (String)choices.get(i);
            choiceLabels[i].setText("<html><font size=+1>(" + (i+1) + "). " + choice);
         } 
      }
   }
   
   public void showResults()
   {
      // clean all
      removeAll();
      ContextManager.getTimerPanel().stopTimer();
      ContextManager.getTimerPanel().removeAll();
      
      repaint();
      ContextManager.getTimerPanel().repaint();  
      
      // show results      
      JPanel mainPanel = new JPanel();
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      mainPanel.setLayout(gridbag);
      cons.anchor = GridBagConstraints.WEST;

      cons.gridx = 0;
      cons.gridy = 0;      
      cons.insets = new Insets(2, 0, 10, 0);
      JLabel titleLabel = new JLabel("<html><b><font size=+2>測驗結果");       
      gridbag.setConstraints(titleLabel, cons);
      mainPanel.add(titleLabel); 
      
      cons.gridx = 0;
      cons.gridy = 1;
      cons.insets = new Insets(2, 0, 2, 0);
      JLabel correctLabel = new JLabel("<html><font size=+2>答對 : " + scoreMgr.correctAnswers());
      gridbag.setConstraints(correctLabel, cons);
      mainPanel.add(correctLabel);
 
      cons.gridx = 0;
      cons.gridy = 2;
      JLabel wrongLabel = new JLabel("<html><font size=+2>答錯 : " + scoreMgr.wrongAnswers());
      gridbag.setConstraints(wrongLabel, cons);
      mainPanel.add(wrongLabel);

      cons.gridx = 0;
      cons.gridy = 3;
      JLabel timeLabel = new JLabel("<html><font size=+2>時間 : " + scoreMgr.getTotalTime());
      gridbag.setConstraints(timeLabel, cons);
      mainPanel.add(timeLabel);      
      
      setLayout(new BorderLayout());
      add(mainPanel, BorderLayout.CENTER);
      validate();
   }

   private int getSelectedAnswer(AWTEvent event)
   {
      int idx = -1;

      for(int i = 0; i < MAX_CHOICES; i++)
      {
         if(event.getSource() == choiceLabels[i])
         {
            idx = i;

            break;
         }
      }

      return idx;
   }

   /**
    * This is a listener for choices.
    */
   class ChoiceListener extends MouseAdapter
   {
      public void mouseClicked(MouseEvent event)
      {
         // chosen
         if(event.getClickCount() == 1)
         {              
            int idx = getSelectedAnswer(event);
            checkedLabels[idx].setIcon(checkedIcon);
            
            long time = ContextManager.getTimerPanel().getTime();
            Vector answerSet = 
               scoreMgr.verifyAnswer(number, true, Integer.toString(idx+1), time); // 1-based
            ContextManager.getAnswersPanel().addAnswerSet(answerSet);
            nextQuestion();
         }
      }

      public void mouseEntered(MouseEvent event)
      {
         // checked
         int idx = getSelectedAnswer(event);
         if(idx > -1)
            checkedLabels[idx].setIcon(checkedIcon);
      }

      public void mouseExited(MouseEvent event)
      {
         // unckecked
         int idx = getSelectedAnswer(event);
         if(idx > -1)
            checkedLabels[idx].setIcon(null);
      }
   }
}
