package members.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{
   private static final SessionFactory sessionFactory;

   static
   {
      try
      {
         // Create the SessionFactory from hibernate.cfg.xml
         sessionFactory = new Configuration().configure("hibernate.cfg.members.xml").buildSessionFactory();
      }
      catch(Throwable ex)
      {
         // Make sure you log the exception, as it might be swallowed
         System.err.println("Initial SessionFactory creation failed." + ex);
         throw new ExceptionInInitializerError(ex);
      }
   }

   /*static
   {
      try
      {
         sessionFactory = new AnnotationConfiguration().buildSessionFactory();
      }
      catch(Throwable ex)
      {
         // Log exception!
         throw new ExceptionInInitializerError(ex);
      }
   }*/

   public static Session getSession() throws HibernateException
   {
      // using automatic session context management
      // session is bound to a Java thread
      return sessionFactory.getCurrentSession();
      // need to use closeSession() if openSession() is used
      //return sessionFactory.openSession();
   }   
}
