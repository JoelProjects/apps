/* $Id: AccountManager.java,v 1.5 2008/10/24 05:54:16 joelchou Exp $ */
package members.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import members.datamodel.AccountInfo;
import members.datamodel.AccountRoleInfo;
import members.ui.auth.AbstractLoginDialog;
import members.utils.HibernateUtil;
import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This is a manager class for accounts.
 * 
 * @author Cheng-Hung Chou
 * @since 1/26/2007
 *
 */
public class AccountManager {
    private static Logger logger = Logger.getLogger(AccountManager.class);
    private Session session;
    private AccountInfo accountInfo;

    public AccountManager() {
    }

    public AccountManager(Session session) {
        this.session = session;
    }

    public boolean authenticate(AbstractLoginDialog login, int retry,
        Vector<AccountRoleInfo> allowedRoles) {
        boolean isFailed = true;
        for(int i = 0; i < retry; i++) {
            login.setVisible(true);
            if(login.isCancelled())
                break;
            String username = login.getUsername();
            String password = Utility.digestPassword(login.getPassword());
            session = HibernateUtil.getSession();
            session.beginTransaction();
            if(authenticate(username, password)
                && isInRole(username, allowedRoles))
                isFailed = false;
            session.getTransaction().commit();
            if(!isFailed)
                break;
        }

        return isFailed;
    }

    public boolean authenticate(String username, String password) {
        boolean isAuthenticated = false;

        accountInfo = (AccountInfo)session.get(AccountInfo.class, username);
        if(accountInfo == null)
            return isAuthenticated;
        String thePassword = accountInfo.getPassword();
        if(thePassword != null) {
            if(password.equals(thePassword))
                isAuthenticated = true;
        }
        else {
            isAuthenticated = true;
        }

        return isAuthenticated;
    }

    /**
     * Gets authenticated account info.
     * 
     * @return
     */
    public AccountInfo getAccountInfo() {
        return accountInfo;
    }
    
    /**
     * Gets all accounts.
     * 
     * @return a map of <username, AccountInfo>
     */
    public Map<String, AccountInfo> getAllAccounts() {
        Session session = HibernateUtil.getSession();
        Map<String, AccountInfo> accountMap = new HashMap<String, AccountInfo>();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM AccountInfo ORDER BY username");
            Iterator<AccountInfo> it = query.list().iterator();
            while(it.hasNext()) {
                AccountInfo accountInfo = it.next();
                accountMap.put(accountInfo.getUsername(), accountInfo);
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }        
        
        return accountMap;
    }    

    public boolean isInRole(String username,
        Vector<AccountRoleInfo> allowedRoles) {
        // assume that the user is authenticated and the object is loaded from 
        // the database
        AccountInfo account = (AccountInfo)session.load(AccountInfo.class,
            username);
        // get user role(s)
        Set<AccountRoleInfo> roles = account.getRoles();
        boolean isAuthorized = false;
        if(allowedRoles != null && allowedRoles.size() > 0) {
            Iterator<AccountRoleInfo> it = allowedRoles.iterator();
            while(it.hasNext()) {
                AccountRoleInfo role = it.next();
                isAuthorized = roles.contains(role);
                if(isAuthorized)
                    break;
            }
        }

        return isAuthorized;
    }

    public boolean isInRole(AccountInfo account,
        Vector<AccountRoleInfo> allowedRoles) {
        if(account == null)
            return false;
        // get user role(s)
        Set<AccountRoleInfo> roles = account.getRoles();
        boolean isAuthorized = false;
        if(allowedRoles != null && allowedRoles.size() > 0) {
            Iterator<AccountRoleInfo> it = allowedRoles.iterator();
            while(it.hasNext()) {
                AccountRoleInfo role = it.next();
                isAuthorized = roles.contains(role);
                if(isAuthorized)
                    break;
            }
        }

        return isAuthorized;
    }    
    
    public static boolean accountExists(String username) {
        boolean existed = false;
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM AccountInfo WHERE username=:username");
            query.setParameter("username", username);
            Iterator<AccountInfo> it = query.list().iterator();
            if(it.hasNext()) {
                existed = true;
            }

            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        return existed;
    }
}
