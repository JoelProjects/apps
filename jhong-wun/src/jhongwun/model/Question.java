package jhongwun.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an individual question in a question set.
 *
 * @author Cheng-Hung Chou
 * @since August 6, 2005
 */
public class Question
{
   private int number = 0;
   private String subject = "";
   private String answer = "";
   private List choices = new ArrayList();

   public Question(int number)
   {
      this.number = number;
   }

   public int getQuestionNumber()
   {
      return number;
   }

   public String getSubject()
   {
      return subject;
   }

   public void setSubject(String subject)
   {
      this.subject = subject;
   }

   public void addChoice(String choice)
   {
      choices.add(choice);;
   }

   public List getChoices()
   {
      return choices;
   }

   /**
    * @param number 1-based
    * @return
    */
   public String getChoice(int number)
   {
      return (String)choices.get(number-1);
   }

   public String getAnswer()
   {
      return answer;
   }

   public void setAnswer(String answer)
   {
      this.answer = answer;
   }
}
