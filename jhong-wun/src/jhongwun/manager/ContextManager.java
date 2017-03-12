package jhongwun.manager;

import jhongwun.model.QuestionSet;
import jhongwun.ui.AnswersPanel;
import jhongwun.ui.SubjectPanel;
import jhongwun.ui.ChoicesPanel;
import jhongwun.ui.TimerPanel;

/**
 * @author Cheng-Hung Chou
 * @since 8/6/2005
 */
public class ContextManager
{
   private static QuestionSet questionSet;
   private static ScoreManager scoreMgr;
   private static SubjectPanel subjectPanel;
   private static ChoicesPanel choicesPanel;
   private static TimerPanel timerPanel;
   private static AnswersPanel answersPanel;

   public ContextManager() throws Exception
   {
      // load question set
      QuestionSetReader reader =
         new QuestionSetReader(".", "question_set.xml");
      questionSet = reader.process();

      // initialize socre manager
      scoreMgr = new ScoreManager(questionSet);
   }

   public static QuestionSet getQuestionSet()
   {
      return questionSet;
   }

   public static ScoreManager getScoreManager()
   {
      return scoreMgr;
   }

   public static SubjectPanel getSubjectPanel()
   {
      return subjectPanel;
   }

   public static void setSubjectPanel(SubjectPanel panel)
   {
      subjectPanel = panel;
   }

   public static ChoicesPanel getChoicesPanel()
   {
      return choicesPanel;
   }

   public static void setChoicesPanel(ChoicesPanel panel)
   {
      choicesPanel = panel;
   }

   public static TimerPanel getTimerPanel()
   {
      return timerPanel;
   }

   public static void setTimerPanel(TimerPanel panel)
   {
      timerPanel = panel;
   }
   
   public static AnswersPanel getAnswersPanel()
   {
      return answersPanel;
   }

   public static void setAnswersPanel(AnswersPanel panel)
   {
      answersPanel = panel;
   }   
}
