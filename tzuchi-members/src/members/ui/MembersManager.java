/* $Id: MembersManager.java,v 1.5 2008/10/19 06:38:12 joelchou Exp $ */
package members.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.activation.ActivationException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import members.datamodel.AccountRoleInfo;
import members.manager.AccountManager;
import members.manager.AccountRoleEnum;
import members.manager.ContextManager;
import members.ui.auth.AbstractLoginDialog;
import members.ui.utils.GuiUtil;
import members.utils.Constants;
import members.utils.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * The is the main page after logging in.
 * 
 * @author Cheng-Hung Chou
 * @since 12/16/2006
 */
public class MembersManager {
    private Logger logger = Logger.getLogger(MembersManager.class);

    public static void main(String[] args) {
        new MembersManager();
    }

    public MembersManager() {
        // initialize application context
        try {
            ContextManager.init();
        }
        catch(ActivationException e) {
            GuiUtil.messageDialog(null, e.getMessage());
            System.exit(1);
        }

        try {
            // use system related look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
        }

        // login page
        Vector<AccountRoleInfo> allowedRoles = new Vector<AccountRoleInfo>();
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            AccountRoleInfo role = (AccountRoleInfo)session
                .get(AccountRoleInfo.class, AccountRoleEnum.SYSTEM_ADMIN
                    .getRoleId());
            allowedRoles.add(role);
            session.getTransaction().commit();
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        AbstractLoginDialog login = new MembersManagerLogin();
        if(login.isCancelled()) {
            login.dispose();
            System.exit(0);
        }

        AccountManager accountMgr = new AccountManager();
        if(accountMgr.authenticate(login, 3, allowedRoles)) {
            login.dispose();
            System.exit(0);
        }

        JFrame frame = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(screenSize.width / 2 - Constants.APP_WIDTH / 2,
            screenSize.height / 2 - Constants.APP_HEIGHT / 2 - 50,
            Constants.APP_WIDTH, Constants.APP_HEIGHT);
        frame.setTitle("Tzu-Chi Members Manager");
        frame.setIconImage(new ImageIcon(this.getClass().getResource(
            "images/tzuchi-icon.jpg")).getImage());
        Container content = frame.getContentPane();

        // tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(800, 600));

        MembersManagementPanel membersManagementPanel = new MembersManagementPanel();
        ContextManager.setMembersManagementPanel(membersManagementPanel);
        tabbedPane.addTab("Member Management", membersManagementPanel);
        AccountsManagementPanel accountsManagementPanel = new AccountsManagementPanel();
        ContextManager.setAccountsManagementPanel(accountsManagementPanel);
        tabbedPane.addTab("Account Management", accountsManagementPanel);

        content.add(tabbedPane);

        //tabbedPane.addChangeListener(new TabChangeListener());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }
}
