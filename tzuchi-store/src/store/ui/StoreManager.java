/* $Id: StoreManager.java,v 1.14 2008/10/24 05:55:21 joelchou Exp $ */
package store.ui;

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

import members.datamodel.AccountInfo;
import members.datamodel.AccountRoleInfo;
import members.manager.AccountManager;
import members.manager.AccountRoleEnum;
import members.ui.auth.AbstractLoginDialog;
import members.ui.utils.GuiUtil;
import members.utils.Constants;
import members.utils.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import store.manager.ContextManager;

/**
 * The is the main page after logging in.
 * 
 * @author Cheng-Hung Chou
 * @since 2/21/2007
 */
public class StoreManager {
    private Logger logger = Logger.getLogger(StoreManager.class);

    public static void main(String[] args) {
        new StoreManager();
    }

    public StoreManager() {
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
        Vector<AccountRoleInfo> adminRoles = new Vector<AccountRoleInfo>();
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            AccountRoleInfo role = (AccountRoleInfo)session
                .get(AccountRoleInfo.class, AccountRoleEnum.SYSTEM_ADMIN
                    .getRoleId());
            allowedRoles.add(role);
            adminRoles.add(role);
            role = (AccountRoleInfo)session.get(AccountRoleInfo.class,
                AccountRoleEnum.STORE_ADMIN.getRoleId());
            allowedRoles.add(role);
            adminRoles.add(role);
            role = (AccountRoleInfo)session.get(AccountRoleInfo.class,
                AccountRoleEnum.STORE_STAFF.getRoleId());
            allowedRoles.add(role);
            session.getTransaction().commit();
            ContextManager.setAdminRoles(adminRoles);
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        AbstractLoginDialog login = new StoreManagerLogin();
        if(login.isCancelled()) {
            login.dispose();
            System.exit(0);
        }

        AccountManager accountMgr = new AccountManager();
        if(accountMgr.authenticate(login, 3, allowedRoles)) {
            login.dispose();
            System.exit(0);
        }

        AccountInfo accountInfo = accountMgr.getAccountInfo();
        String username = accountInfo.getUsername();

        JFrame frame = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((screenSize.width - Constants.APP_WIDTH) / 2,
            (screenSize.height - Constants.APP_HEIGHT) / 2,
            Constants.APP_WIDTH, Constants.APP_HEIGHT);
        frame.setTitle("慈濟書軒庫存管理     帳號: " + username + "  姓名: "
            + accountInfo.getMemberInfo().getName());
        frame.setIconImage(new ImageIcon(this.getClass().getResource(
            "images/tzuchi-icon.jpg")).getImage());
        Container content = frame.getContentPane();

        // tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(800, 600));

        tabbedPane.addTab("書軒櫃台", new POSPanel(accountInfo));
        tabbedPane.addTab("出售記錄", new ReportsPanel(accountInfo));
        tabbedPane.addTab("物品登記", new ItemsManagementPanel(accountInfo));        
        session = HibernateUtil.getSession();
        session.beginTransaction();
        accountMgr = new AccountManager(session);
        if(accountMgr.isInRole(accountInfo, adminRoles)) {
            tabbedPane.addTab("報表輸出", new ExportPanel());
        }
        session.getTransaction().commit();

        content.add(tabbedPane);

        tabbedPane.addChangeListener(new TabChangeListener());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }
}
