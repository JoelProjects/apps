/* $Id: EditDialogHelper.java,v 1.4 2008/10/19 06:38:12 joelchou Exp $ */
package members.ui.utils;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import members.datamodel.AccountInfo;
import members.datamodel.MemberInfo;
import members.ui.EditAccountInfoDialog;
import members.ui.EditMemberInfoDialog;
import members.utils.HibernateUtil;
import members.utils.Utility;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class EditDialogHelper {
    private Logger logger = Logger.getLogger(EditDialogHelper.class);
    private Frame parentFrame;

    public EditDialogHelper(Frame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public AccountInfo editAccountInfo(String username) {
        EditAccountInfoDialog dialog = null;
        Session session = null;
        AccountInfo account = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();
            account = (AccountInfo)session.get(AccountInfo.class, username);

            // bring up the edit dialog
            dialog = new EditAccountInfoDialog(parentFrame, true, "");
            dialog.setUsername(account.getUsername());
            dialog.setRoles(account.getRoleNames());

            session.getTransaction().commit();

            dialog.pack();
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            if(dialog.hasChanged()) {
                session = HibernateUtil.getSession();
                session.beginTransaction();

                account.setUsername(dialog.getUsername());
                if(dialog.isPasswordChanged()) {
                    String md5Password = Utility.digestPassword(String
                        .valueOf(dialog.getNewPassword()));
                    account.setPassword(md5Password);
                }
                account.setRoles(dialog.getRoles());

                // update db
                session.update(account);
                session.getTransaction().commit();
            }
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        dialog.dispose();

        return account;
    }

    public MemberInfo editMemberInfo(String username) {
        Session session = null;
        AccountInfo account = null;
        MemberInfo member = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();
            account = (AccountInfo)session.get(AccountInfo.class, username);
            long memberId = account.getMemberId();
            session.getTransaction().commit();
            if(memberId != -1) {
                // existing member
                member = editMemberInfo(memberId);
            }
            else {
                // new member
                member = addNewMemberInfo(account);
            }
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        return member;
    }
        
    // for existing member
    public MemberInfo editMemberInfo(long memberId) {
        EditMemberInfoDialog dialog = null;
        Session session = null;
        MemberInfo member = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();
            member = (MemberInfo)session.get(MemberInfo.class, memberId);

            // bring up the edit dialog
            dialog = new EditMemberInfoDialog(parentFrame, true, "");
            dialog.setMemberId(member.getMemberId());
            dialog.setFirstName(member.getFirstName());
            dialog.setLastName(member.getLastName());
            dialog.setChFirstName(member.getChFirstName());
            dialog.setChLastName(member.getChLastName());
            dialog.setHomePhone(member.getHomePhone());
            dialog.setCellPhone(member.getCellPhone());
            dialog.setWorkPhone(member.getWorkPhone());
            dialog.setEmail(member.getEmail());

            session.getTransaction().commit();

            dialog.pack();
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            if(dialog.hasChanged()) {
                session = HibernateUtil.getSession();
                session.beginTransaction();

                member.setMemberId(dialog.getMemberId());
                member.setFirstName(dialog.getFirstName());
                member.setLastName(dialog.getLastName());
                member.setChFirstName(dialog.getChFirstName());
                member.setChLastName(dialog.getChLastName());
                member.setHomePhone(dialog.getHomePhone());
                member.setCellPhone(dialog.getCellPhone());
                member.setWorkPhone(dialog.getWorkPhone());
                member.setEmail(dialog.getEmail());

                // update db
                session.update(member);
                session.getTransaction().commit();
            }
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
        }

        dialog.dispose();

        return member;
    }

    // for new member
    public MemberInfo addNewMemberInfo() {
        return addNewMemberInfo(null);
    }

    // for new member that will link to an account
    public MemberInfo addNewMemberInfo(AccountInfo account) {
        EditMemberInfoDialog dialog = new EditMemberInfoDialog(parentFrame,
            true);
        dialog.setMemberId(Long.parseLong(Utility.getLongDateTimeStr()));
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        MemberInfo member = null;
        if(dialog.hasChanged()) {
            member = new MemberInfo();
            member.setMemberId(dialog.getMemberId());
            member.setFirstName(dialog.getFirstName());
            member.setLastName(dialog.getLastName());
            member.setChFirstName(dialog.getChFirstName());
            member.setChLastName(dialog.getChLastName());
            member.setHomePhone(dialog.getHomePhone());
            member.setCellPhone(dialog.getCellPhone());
            member.setWorkPhone(dialog.getWorkPhone());
            member.setEmail(dialog.getEmail());

            Session session = HibernateUtil.getSession();
            try {
                // insert into the db
                session.beginTransaction();
                session.save(member);
                if(account != null) {
                    // link the new member to this account
                    account.setMemberInfo(member);
                    session.update(account);
                }
                session.getTransaction().commit();
            }
            catch(Exception e) {
                session.getTransaction().rollback();
                logger.error(e);
            }
        }

        return member;
    }

    // for a new account that will link to a member
    public AccountInfo addNewAccountInfo(long memberId) {
        // bring the edit dialog
        EditAccountInfoDialog dialog = new EditAccountInfoDialog(parentFrame, true);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        AccountInfo account = null;
        if(dialog.hasChanged()) {
            account = new AccountInfo();
            account.setUsername(dialog.getUsername());
            String md5Password = Utility.digestPassword(String
                .valueOf(dialog.getNewPassword()));
            account.setPassword(md5Password);
            account.setRoles(dialog.getRoles());

            Session session = HibernateUtil.getSession();
            try {
                // insert into the db
                session.beginTransaction();
                MemberInfo member = (MemberInfo)session.get(MemberInfo.class, memberId);
                account.setMemberInfo(member);
                session.save(account);
                session.getTransaction().commit();
            }
            catch(Exception e) {
                session.getTransaction().rollback();
                logger.error(e);
            }
        }  
        
        return account;
    }
}
