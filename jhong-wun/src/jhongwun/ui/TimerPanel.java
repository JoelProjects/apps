package jhongwun.ui;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import jhongwun.manager.ContextManager;
import jhongwun.manager.ScoreManager;

/**
 *
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class TimerPanel extends JPanel
{
   private int progressTimer;
   private JProgressBar progressBar;
   private Object lock = new Object();
   private boolean shouldStop;  // should thread to be stopped
   private Thread myThread;
   private JLabel correctLabel;
   private JLabel wrongLabel;
   private JLabel timeLabel;
   private ScoreManager scoreMgr;

   public TimerPanel()
   {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(1, 0, 1, 0), BorderFactory.createRaisedBevelBorder()));
      //setBorder(new EmptyBorder(5, 5, 5, 5));
      
      // results
      JPanel resultsPanel = new JPanel();
      correctLabel = new JLabel("<html><font color=green>對: 0");
      wrongLabel = new JLabel("<html><font color=red>錯: 0"); 
      timeLabel = new JLabel("<html><font color=blue>時間 : 0"); 
      resultsPanel.add(correctLabel);
      resultsPanel.add(wrongLabel);
      resultsPanel.add(timeLabel);
      add(resultsPanel);
      
      // timer
      progressTimer = (int)ContextManager.getQuestionSet().getTime() / 1000;  // in seconds
      progressBar = new JProgressBar();
      progressBar.setStringPainted(true);
      progressBar.setPreferredSize(new Dimension(200, 20));
      add(progressBar);
      
      scoreMgr = ContextManager.getScoreManager();
   }
   
   public void updateResults()
   {
      correctLabel.setText("<html><font color=green>對 : " + scoreMgr.correctAnswers());
      wrongLabel.setText("<html><font color=red>錯 : " + scoreMgr.wrongAnswers()); 
      timeLabel.setText("<html><font color=blue>時間 : " + scoreMgr.getTotalTime()); 
   }

   public void resetTimer()
   {
      progressBar.setValue(0);
      progressBar.setString("0/" + progressTimer);
   }
   
   public void startTimer()
   {
      shouldStop = false;
      myThread = new TimerThread(progressTimer);
      myThread.start();  // start the task thread
   }
   
   public long getTime()
   {
      if(myThread != null)
         return ((TimerThread)myThread).getTime();
      else
         return 0;
   }
   
   public void stopTimer()
   {
      if(myThread == null)
         return;
      
      synchronized(lock)
      {
        shouldStop = true;
        lock.notify();  // notify thread if stopped
      }      
   }

   // task will be executed in a thread and monitored by a progress bar
   class TimerThread extends Thread
   {
      public int steps = 1;
      public long timePeriod = 500;
      private int max;
      
      public TimerThread(int max)
      {
         this.max = max;
         
         steps = (int)(1000/timePeriod); // number of steps per second in progress bar
         progressBar.setValue(0);
         progressBar.setString("0/" + max);         
      }
      
      public long getTime()
      {
         return progressBar.getValue() * timePeriod;
      }

      public void run()
      {
         int min = 0;
         progressBar.setValue(min);
         progressBar.setMinimum(min);
         progressBar.setMaximum(max*steps);

         Runnable runner = new Runnable()
         {
            public void run()
            {
               int value = progressBar.getValue();
               // increase counter value
               value++;
               // change current value of progress bar
               progressBar.setValue(value);
               
               if((value % steps) == 0)
               {
                  progressBar.setString(value/steps + "/" + max);
                  
                  if(value >= max*steps)
                  {  
                     int number = ContextManager.getChoicesPanel().getCurrentQuestionNumber();
                     Vector answerSet = 
                        ContextManager.getScoreManager().verifyAnswer(number, true, null, getTime());
                     ContextManager.getAnswersPanel().addAnswerSet(answerSet);
                     ContextManager.getChoicesPanel().nextQuestion();
                  }                  
               }
            }
         };

         while(true)
         {
            synchronized(lock)
            {
               if(shouldStop)
               {
                  break;
               }
               try
               {
                  lock.wait(timePeriod);
               }
               catch(InterruptedException e)
               {
                  // Ignore Exception
               }
            }
            
            try
            { 
               // block until AWT events have been processed
               SwingUtilities.invokeAndWait(runner);
            }
            catch(InvocationTargetException e)
            {
               break;
            }
            catch(InterruptedException e)
            {
               // Ignore Exception
            }
         }

         myThread = null;
      }
   }
}
