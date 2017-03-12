package jhongwun.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import jhongwun.model.QuestionSet;
import jhongwun.model.Question;

/**
 * This class is responsible for reading and parsing the question set XML file and
 * constructing related objects.
 *
 * @author Cheng-Hung Chou
 * @since August 6, 2005
 */
public class QuestionSetReader
{
   // XML tags
   public static final String QUESTION_SET_TAG = "question-set";
   public static final String DESCRIPTION_TAG = "description";
   public static final String TIME_TAG = "time";
   public static final String QUESTION_TAG = "question";
   public static final String SUBJECT_TAG = "subject";
   public static final String CHOICES_TAG = "choices";
   public static final String CHOICE_TAG = "choice";
   public static final String ANSWER_TAG = "answer";

   private DocumentBuilder docBuilder;
   private Document doc;
   private QuestionSet questionSet;
   private Question question;
   private String currentTag = "";

   public QuestionSetReader(String fileDir, String fileName)
      throws Exception
   {
      init(fileDir, fileName);
   }

   /**
    * Processes the question set file.
    *
    * @return
    */
   public QuestionSet process()
   {
      questionSet = new QuestionSet();
      NodeList nodes = doc.getElementsByTagName(QUESTION_SET_TAG);
      int len = nodes.getLength();
      if(len == 0)
         return null;

      process(nodes.item(0).getChildNodes());

      return questionSet;
   }

   /**
    * Processes the question set file.
    *
    * @return
    */
   public void process(NodeList nodes)
   {
      int len = nodes.getLength();
      for(int i = 0; i < len; i++)
      {
         Node node = nodes.item(i);
         String nodeName = node.getNodeName().trim().toLowerCase();
         switch(node.getNodeType())
         {
            case Node.ELEMENT_NODE:
               currentTag = nodeName;
               //System.out.println(currentTag);
               if(nodeName.equals(QUESTION_TAG))
               {
                  question = questionSet.newQuestion();
                  processQuestion(node.getChildNodes());
               }
               else
                  process(node.getChildNodes());
               break;
            case Node.TEXT_NODE:
               String nodeValue = node.getNodeValue().trim();
               if(nodeValue.length() > 0)
               {
                  if(currentTag.equals(DESCRIPTION_TAG))
                     questionSet.setDescription(nodeValue);
                  else if(currentTag.equals(TIME_TAG))
                     questionSet.setTime(Long.parseLong(nodeValue));
               }

               break;
            default:
         }
      }
   }

   /**
    * Processes <question>.
    *
    * @param nodes
    */
   private void processQuestion(NodeList nodes)
   {
      int len = nodes.getLength();
      for(int i = 0; i < len; i++)
      {
         Node node = nodes.item(i);
         String nodeName = node.getNodeName().trim().toLowerCase();
         switch(node.getNodeType())
         {
            case Node.ELEMENT_NODE:
               currentTag = nodeName;
               //System.out.println(currentTag);
               if(nodeName.equals(CHOICES_TAG))
               {
                  processChoices(node.getChildNodes());
               }
               else
                  processQuestion(node.getChildNodes());
               break;
            case Node.TEXT_NODE:
               String nodeValue = node.getNodeValue().trim();
               if(nodeValue.length() > 0)
               {
                  if(currentTag.equals(SUBJECT_TAG))
                     question.setSubject(nodeValue);
                  else if(currentTag.equals(ANSWER_TAG))
                     question.setAnswer(nodeValue);
               }

               break;
            default:
         }
      }
   }

   /**
    * Processes <choices>.
    *
    * @param nodes
    */
   private void processChoices(NodeList nodes)
   {
      int len = nodes.getLength();
      for(int i = 0; i < len; i++)
      {
         Node node = nodes.item(i);
         switch(node.getNodeType())
         {
            case Node.ELEMENT_NODE:
               currentTag = node.getNodeName().toLowerCase();
               // handle childen
               if(node.hasChildNodes())
                 processChoices(node.getChildNodes());
               break;
            case Node.TEXT_NODE:
               String nodeValue = node.getNodeValue().trim();
               if(nodeValue.length() > 0)
               {
                  if(currentTag.equals(CHOICE_TAG))
                     question.addChoice(nodeValue);
               }

               break;
            default:
         }
      }
   }

   /**
    * Initializes DOM parser and parses the XML document.
    *
    * @throws Exception
    */
   private void init(String fileDir, String fileName) throws Exception
   {
      File file = null;
      try
      {
         DocumentBuilderFactory docBuilderFactory =
            DocumentBuilderFactory.newInstance();
         docBuilderFactory.setValidating(true);
         docBuilder = docBuilderFactory.newDocumentBuilder();
         //docBuilder.setErrorHandler(new UAPTErrorHandler());

         file = new File(fileDir, fileName);

         doc = docBuilder.parse(file);
         doc.normalize();
      }
      catch(IOException ioe)
      {
         System.out.println("Error in accessing question set file: " + file);
         throw new Exception(ioe);
      }
      catch(SAXException saxe)
      {
         System.out.println("Error in parsing question set file: " + file);
         throw new Exception(saxe);
      }
   }

   public static void main(String[] args)
   {
      try
      {
         QuestionSetReader reader =
            new QuestionSetReader(".", "question_set.xml");
         QuestionSet questionSet = reader.process();

         System.out.println(questionSet.getDescription());
         System.out.println(questionSet.getTime());

         List questions = questionSet.getQuestionList();
         for(int i = 0; i < questions.size(); i++)
         {
            Question question = (Question)questions.get(i);

            System.out.println(question.getSubject());
            System.out.println(question.getChoices());
            System.out.println(question.getAnswer());
         }
      }
      catch(Exception e)
      {
         System.out.println(e);
      }
   }
}
