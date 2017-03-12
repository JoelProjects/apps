/* $Id: ContextManager.java,v 1.2 2008/10/19 06:38:12 joelchou Exp $ */
package members.manager;

import java.rmi.activation.ActivationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import members.datamodel.AccountRoleInfo;
import members.ui.AccountsManagementPanel;
import members.ui.MembersManagementPanel;
import members.utils.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Cheng-Hung Chou
 * @since 2/17/2007
 */
public class ContextManager {
    private static MembersManagementPanel membersManagementPanel = null;
    private static AccountsManagementPanel accountsManagementPanel = null;
    private static Map<String, Integer> roleMap = new HashMap<String, Integer>(); // (name, id)
    private static Vector<AccountRoleInfo> roleList = new Vector<AccountRoleInfo>();
    private static Logger logger = Logger.getLogger(ContextManager.class);

    protected ContextManager() {
    }

    public static void init() throws ActivationException {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session
                .createQuery("FROM AccountRoleInfo ORDER BY accountRoleName");
            Iterator it = query.list().iterator();
            while(it.hasNext()) {
                AccountRoleInfo role = (AccountRoleInfo)it.next();
                roleList.add(role);
                roleMap.put(role.getAccountRoleName(), role.getAccountRoleId());
            }
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.fatal(e);
            throw new ActivationException(e.getMessage());
        }
    }

    public static Vector<AccountRoleInfo> getRoleList() {
        return roleList;
    }

    public static int getRoleId(String name) {
        return roleMap.get(name);
    }
    
    public static MembersManagementPanel getMembersManagementPanel() {
        return membersManagementPanel;
    }

    public static void setMembersManagementPanel(
        MembersManagementPanel membersManagementPanel) {
        ContextManager.membersManagementPanel = membersManagementPanel;
    }

    public static AccountsManagementPanel getAccountsManagementPanel() {
        return accountsManagementPanel;
    }

    public static void setAccountsManagementPanel(
        AccountsManagementPanel accountsManagementPanel) {
        ContextManager.accountsManagementPanel = accountsManagementPanel;
    }    
}
