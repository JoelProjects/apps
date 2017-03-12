/* $Id: ExtMySQL5InnoDBDialect.java,v 1.1 2009/12/06 00:11:25 joelchou Exp $ */

package store.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLInnoDBDialect;

/**
 * An extension of MySQLInnoDBDialect to avoid error like 
 * "no dialect mapping for jdbc type: -1".
 * 
 * @author Cheng-Hung Chou
 * @since 12/3/2009
 */
public class ExtMySQL5InnoDBDialect extends MySQLInnoDBDialect {
    
    public ExtMySQL5InnoDBDialect() {
        super();
        registerHibernateType(-1, Hibernate.TEXT.getName());
        registerHibernateType(-4, Hibernate.BLOB.getName());
    }
}
