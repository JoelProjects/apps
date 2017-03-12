package members.utils.id;

import java.io.Serializable;

import members.utils.Utility;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * MemberIdGenerator is the ID generator for member info.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 */
public class MemberIdGenerator implements IdentifierGenerator
{
   public Serializable generate(SessionImplementor session, Object object) 
      throws HibernateException
   {
      return Long.parseLong(Utility.getLongDateTimeStr());
   }
}
