/* $Id: ContextManager.java,v 1.5 2008/10/24 05:55:21 joelchou Exp $ */
package store.manager;

import java.rmi.activation.ActivationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import members.datamodel.AccountInfo;
import members.datamodel.AccountRoleInfo;
import members.manager.AccountManager;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import store.datamodel.ItemTypeInfo;
import store.utils.HibernateUtil;

/**
 * @author Cheng-Hung Chou
 * @since 2/21/2007
 */
public class ContextManager {
    private static Map<String, Integer> typeMap = new HashMap<String, Integer>(); // (name, id)
    private static Vector<ItemTypeInfo> typeList = new Vector<ItemTypeInfo>();
    private static Vector<AccountRoleInfo> adminRoles = new Vector<AccountRoleInfo>();
    private static Map<String, AccountInfo> accountMap = null;
    private static Logger logger = Logger.getLogger(ContextManager.class);

    protected ContextManager() {
    }

    public static void init() throws ActivationException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM ItemTypeInfo ORDER BY typeName");
            Iterator<ItemTypeInfo> it = query.list().iterator();
            while(it.hasNext()) {
                ItemTypeInfo type = it.next();
                typeList.add(type);
                typeMap.put(type.getTypeName(), type.getTypeId());
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.fatal(e);
            throw new ActivationException(e.getMessage());
        }

        AccountManager accountMgr = new AccountManager();
        accountMap = accountMgr.getAllAccounts();
    }

    public static Vector<ItemTypeInfo> getTypeList() {
        return typeList;
    }

    public static int getTypeId(String name) {
        return typeMap.get(name);
    }
    
    public static Vector<AccountRoleInfo> getAdminRoles() {
        return adminRoles;
    }    
    
    public static void setAdminRoles(Vector<AccountRoleInfo> myAdminRoles) {
        adminRoles = myAdminRoles;
    }      
    
    public static AccountInfo getAccountInfo(String username) {
        return accountMap.get(username);
    }
}
