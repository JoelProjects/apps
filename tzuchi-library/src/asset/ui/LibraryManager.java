package asset.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import members.datamodel.AccountRoleInfo;
import members.manager.AccountManager;
import members.manager.AccountRoleEnum;
import members.ui.auth.AbstractLoginDialog;
import members.ui.utils.GuiUtil;
import members.utils.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import asset.datamodel.AssetManager;
import asset.datamodel.AssetStatus;
import asset.datamodel.MemberHistoryManager;
import asset.datamodel.SystemDataManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class LibraryManager
{  
   public static String configFile = Constants.LIB_CONFIG;
   private Logger logger = Logger.getLogger(LibraryManager.class);
   
   public static void main(String[] args)
   {
      if(args.length > 0)
         configFile = args[0];
      
      new LibraryManager();
   }

   public LibraryManager()
   {  
      // initialize application context
      try
      {
         ContextManager ctxMgr = new ContextManager();
      }
      catch(Exception e)
      {
         GuiUtil.messageDialog(null, e.getMessage());
         System.exit(1);
      }      

      try
      {
         // use system related look and feel
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch(Exception e)
      {
      }

      // login page
      if(ContextManager.isAuthRequired())
      {
         Vector<AccountRoleInfo> allowedRoles = new Vector<AccountRoleInfo>();      
         Session session = HibernateUtil.getSession();
         try
         {
            session.beginTransaction();
            AccountRoleInfo role = 
               (AccountRoleInfo)session.get(AccountRoleInfo.class, AccountRoleEnum.SYSTEM_ADMIN.getRoleId());      
            allowedRoles.add(role);
            role = 
               (AccountRoleInfo)session.get(AccountRoleInfo.class, AccountRoleEnum.STORE_ADMIN.getRoleId());      
            allowedRoles.add(role);
            role = 
               (AccountRoleInfo)session.get(AccountRoleInfo.class, AccountRoleEnum.STORE_STAFF.getRoleId());      
            allowedRoles.add(role);         
            session.getTransaction().commit();
         }
         catch(Exception e)
         {
            session.getTransaction().rollback();
            logger.error(e);
         }
      
         AbstractLoginDialog login = new LibraryManagerLogin();
         if(login.isCancelled())
         {
            login.dispose();
            System.exit(0);         
         }
      
         AccountManager accountMgr = new AccountManager();
         if(accountMgr.authenticate(login, 3, allowedRoles))
         {
            login.dispose();
            System.exit(0);
         }
      }
           
      JFrame frame = new JFrame();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      frame.setBounds(screenSize.width/2 - Constants.APP_WIDTH/2, screenSize.height/2 - Constants.APP_HEIGHT/2 - 50, 
            Constants.APP_WIDTH, Constants.APP_HEIGHT);      
      frame.setTitle(ContextManager.getFrameTitle());
      frame.setIconImage(new ImageIcon(this.getClass().getResource(
         "images/" + ContextManager.getImagePrefix() + "-icon.gif")).getImage());
      Container content = frame.getContentPane();      
      
      // system scan: check if there is any overdue asset
      Connection con = null;
      try
      {
         Date now = new Date();
         con = ContextManager.getConnection();
         SystemDataManager systemMgr = new SystemDataManager(con);
         Date lastScanDate = systemMgr.getLastScanDate();
         // once per day
         Calendar today = Calendar.getInstance();
         today.setTime(now);     
         Calendar scanDate = Calendar.getInstance();
         scanDate.setTime(lastScanDate);           
         if(today.get(Calendar.DAY_OF_YEAR) != scanDate.get(Calendar.DAY_OF_YEAR)
            || (today.get(Calendar.DAY_OF_YEAR) - scanDate.get(Calendar.DAY_OF_YEAR)) > 0)
         {
            System.out.println("Daily system Scan starts...");
            
            // check overdue assets
            MemberHistoryManager historyMgr = new MemberHistoryManager(con);
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            List overdueIds = historyMgr.getOverDueAssetIds(today.getTime());
            int num = overdueIds.size();
            System.out.println("Number of overdue assets: " + num);
            // update asset status
            AssetManager assetMgr = new AssetManager(con);
            for(int i = 0; i < num; i++)
            {
               int assetId = ((Integer)overdueIds.get(i)).intValue();
               assetMgr.updateStatus(assetId, AssetStatus.OVER_DUE, now);
            }
               
            systemMgr.updateLastScanDate(now);
            System.out.println("System Scan ends...");
         }
      }
      catch(Exception e)
      {
         System.out.println(e);
      }
      finally
      {
         try 
         {
            if(con != null)
               con.close();
         } 
         catch(Exception e){};
      }
      
      // tabs
      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.setPreferredSize(new Dimension(800, 600));

      tabbedPane.addTab("Service Center", new ServiceCenterPanel());
      tabbedPane.addTab("Member Management", new MemberManagementPanel());
      tabbedPane.addTab("Asset Management", new AssetManagementPanel());
      
      content.add(tabbedPane);

      //tabbedPane.addChangeListener(new TabChangeListener());

      frame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            System.exit(0);
         }
      });

      frame.pack();
      frame.setVisible(true);
   }
}
