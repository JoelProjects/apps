package jhongwun.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import jhongwun.model.QuestionSet;
import jhongwun.model.Question;

/**
 * This is a manager class for managing answers chosen by user.
 *
 * @author Cheng-Hung Chou
 * @since 8/7/2005
 */
public class ScoreManager
{
   private long elapsedTime = 0;  // in ms
   private QuestionSet questionSet;
   private int numOfCorrectAns = 0;
   private int numOfWrongAns = 0;
   private List answers = new ArrayList();

   public ScoreManager(QuestionSet questionSet)
   {
      this.questionSet = questionSet;
   }

   public int correctAnswers()
   {
      return numOfCorrectAns;
   }

   public int wrongAnswers()
   {
      return numOfWrongAns;
   }
   
   public String getTotalTime()
   {
      SimpleDateFormat formatter = new SimpleDateFormat("mm:ss.S", Locale.TAIWAN);
      Date time  = new Date(elapsedTime);
      
      return formatter.format(time);
   }

   /**
    * Verifies selected answer. If answer is selected from choices, then it's
    * 1-based.
    *
    * @param number question number (1-based).
    * @param choices
    * @param answer
    */
   public Vector verifyAnswer(int number, boolean choices, String answer, long time)
   {
      // accumulate elapsed time
      elapsedTime = elapsedTime + time;
      
      // verify answer
      Question question = questionSet.getQuestion(number);  // 1-based
      boolean isCorrect = false;
      String correctAnswer = question.getAnswer();
      if(answer != null && answer.equals(correctAnswer))
         isCorrect = true;

      if(isCorrect)
         numOfCorrectAns++;
      else
         numOfWrongAns++;
         
      Vector answerSet = new Vector();
      answerSet.add(new Integer(number));
      if(choices)
      {
         // for choices, the answers are in numbers
         // need to get answer contents
         if(answer == null)
            answerSet.add("");
         else
            answerSet.add(question.getChoice(Integer.parseInt(answer))); 
         answerSet.add(question.getChoice(Integer.parseInt(correctAnswer)));
      }
      else
      {
         if(answer == null)
            answerSet.add("");
         else
            answerSet.add(answer);
         answerSet.add(correctAnswer);        
      }
      
      addAnswer(answerSet);

      return answerSet;
   }

   public List getAnswers()
   {
      return answers;
   }

   public void addAnswer(Vector answer)
   {
      answers.add(answer);
   }
}
