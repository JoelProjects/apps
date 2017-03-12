package jhongwun.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the top-level class of a question set, which is a container of
 * Question objects.
 *
 * @author Cheng-Hung Chou
 * @since August 6, 2005
 */
public class QuestionSet
{
   private String description = "";
   private long time = 30*1000;  // 30 seconds
   private int questionCounter = 1;
   private List questions = new ArrayList();

   public int getNumberOfQuestions()
   {
      return questionCounter - 1;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public long getTime()
   {
      return time;
   }

   public void setTime(long time)
   {
      this.time = time * 1000;  // to milliseconds
   }

   /**
    * Creates and adds a new Question.
    *
    * @return
    */
   public Question newQuestion()
   {
      Question question = new Question(questionCounter);
      addQuestion(question);
      questionCounter++;

      return question;
   }

   public void addQuestion(Question question)
   {
      questions.add(question);
   }

   public Question getQuestion(int number)
   {
      // 1-based
      return (Question)questions.get(--number);
   }

   public List getQuestionList()
   {
      return questions;
   }
}
