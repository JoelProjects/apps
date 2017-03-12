package asset.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import members.ui.utils.GuiUtil;
import asset.datamodel.MemberInfo;
import asset.datamodel.MemberInfoManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 9/16/2005
 */
public class ServiceCenterPanel extends AbstractAssetPanel
{
   private boolean inUse = false;  // to indicate that it's used for a member
   private MemberInfo member = null;  // current member
   protected JPanel activeCenterPanel;  // BorrowedAssetsPanel, BorrowAssetsPanel or ReturnAssetsPanel
   protected JPanel activeButtonsPanel;
   
   private JComboBox searchOptions;   
   private JTextField searchField;
   private JLabel searchDescLabel;
   private JPanel memberPanel;
   private JLabel memberIdLabel;
   private JLabel memberEnglishNameLabel;
   private JLabel memberChineseNameLabel;
   private JLabel memberTypeLabel;
   private JPanel buttonsPanel;
   
   private JTable borrowedTable;
   
   public ServiceCenterPanel()
   {
      mainPanel = this;  // set this panel as the main panel
      setLayout(new BorderLayout());
      
      // main panel
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      
      // search panel
      JPanel searchPanel = new JPanel();
      searchPanel.add(new JLabel("Search member: "));
      searchOptions = new JComboBox();
      searchOptions.addItem(Constants.OPTION_MEMBER);
      searchOptions.addItem(Constants.OPTION_ASSET);      
      searchPanel.add(searchOptions);
      searchField = new JTextField(20);
      searchPanel.add(searchField);
      JButton searchBtn = new JButton("Search");
      searchPanel.add(searchBtn);
      searchDescLabel = new JLabel(Constants.OPTION_MEMBER_DESC);
      searchPanel.add(searchDescLabel);      
      
      mainPanel.add(searchPanel);
      
      // member info panel
      memberPanel = new JPanel();
      // ID
      JLabel memberIdTitle = new JLabel(GuiUtil.htmlFont("blue", "ID:"));
      memberPanel.add(memberIdTitle);
      memberIdLabel = new JLabel(); 
      memberPanel.add(memberIdLabel);
      // English name
      JLabel englishNameTitle = new JLabel(GuiUtil.htmlFont("blue", "English Name:"));
      memberPanel.add(englishNameTitle);
      memberEnglishNameLabel = new JLabel();
      memberPanel.add(memberEnglishNameLabel);
      // Chinese name
      JLabel chineseNameTitle = new JLabel(GuiUtil.htmlFont("blue", "Chinese Name:"));
      memberPanel.add(chineseNameTitle);
      memberChineseNameLabel = new JLabel();
      memberPanel.add(memberChineseNameLabel);      
      // member type
      JLabel memberTypeTitle = new JLabel(GuiUtil.htmlFont("blue", "Member Type:"));
      memberPanel.add(memberTypeTitle);
      memberTypeLabel = new JLabel();
      memberPanel.add(memberTypeLabel);   
      // Edit button
      JButton editMemberBtn = new JButton("Edit Member");
      memberPanel.add(editMemberBtn);
      
      memberPanel.setVisible(false);  // visible if member is entered
      mainPanel.add(memberPanel);
      
      add(mainPanel, BorderLayout.NORTH);
      
      // borrowed assets/borrow/return panel      
      
      // buttons panel    
      buttonsPanel = getButtonsPanel();
      buttonsPanel.setVisible(false);  // visible if member is entered
      activeButtonsPanel = replacePanel(activeButtonsPanel, buttonsPanel, BorderLayout.SOUTH);  
      
      searchBtn.addActionListener(new SearchListener());  
      editMemberBtn.addActionListener(new EditMemberListener());
      searchOptions.addActionListener(new SearchOptionsListener());
   }
   
   public JPanel getButtonsPanel()
   {
      JPanel myButtonsPanel = new JPanel();
      JButton borrowBtn = new JButton("Borrow Assets");
      myButtonsPanel.add(borrowBtn);
      JButton returnBtn = new JButton("Return Assets");
      myButtonsPanel.add(returnBtn);  
      JButton renewBtn = new JButton("Renew");
      myButtonsPanel.add(renewBtn);       
      
      borrowBtn.addActionListener(new BorrowListener()); 
      returnBtn.addActionListener(new ReturnListener());        
      renewBtn.addActionListener(new RenewListener());  
      
      return myButtonsPanel;
   }
   
   // borrowed list is the main panel
   protected void displayMainPanel(long memberId)
   {
      JPanel panel = new BorrowedAssetsPanel(memberId);
      borrowedTable = ((BorrowedAssetsPanel)panel).getBorrowedTable();
      activeCenterPanel = replacePanel(activeCenterPanel, panel, BorderLayout.CENTER);  
      activeButtonsPanel = replacePanel(activeButtonsPanel,  getButtonsPanel(), BorderLayout.SOUTH);      
   }
   
   protected JPanel replacePanel(JPanel activePanel, JPanel newPanel, Object constraints)
   {
      if(activePanel != null)
      {
         remove(activePanel);
         repaint();
      }
      
      add(newPanel, constraints); 
      validate();
      
      return newPanel;
   }

   // action listener of search options
   class SearchOptionsListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         searchField.setText("");
         if(searchOptions.getSelectedItem().equals(Constants.OPTION_MEMBER))
            searchDescLabel.setText(Constants.OPTION_MEMBER_DESC);
         else if(searchOptions.getSelectedItem().equals(Constants.OPTION_ASSET))
            searchDescLabel.setText(Constants.OPTION_ASSET_DESC);
      }
   }   
   
   // action listener of Search button
   class SearchListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String searchOption = (String)searchOptions.getSelectedItem();
         String searchStr = searchField.getText().trim();
         if(searchStr.equals(Constants.OPTION_ASSET))
         {
            GuiUtil.messageDialog(ServiceCenterPanel.this, "Asset barcode is empty.");     
            return;
         }
         int option = JOptionPane.OK_OPTION;
         if(inUse)
         {
            // confirm before switching
            option = JOptionPane.showConfirmDialog(ServiceCenterPanel.this,
                  "Are you sure to switch to other member?", "Switch Member", 
                  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
         }

         if(option == JOptionPane.OK_OPTION)
         {                    
            Connection con = null;
            try
            {
               con = ContextManager.getConnection();
               MemberInfoManager memberMgr = new MemberInfoManager(con);
               List members = memberMgr.searchMembers(searchStr, searchOption);

               switch(members.size())
               {
                  case 0:
                     inUse = false;
                     GuiUtil.messageDialog(ServiceCenterPanel.this, "Member Not Found. Search string: " + searchStr);
                     memberPanel.setVisible(false);
                     buttonsPanel.setVisible(false); 
                     activeCenterPanel = replacePanel(activeCenterPanel, new JPanel(), BorderLayout.CENTER);
                     member = null;
                     break;
                  case 1:
                     member = (MemberInfo)members.get(0);
                     break;
                  default:
                     MemberListDialog dialog = new MemberListDialog(GuiUtil.getOwnerFrame(getParent()), true, members);
                     dialog.pack();
                     dialog.setVisible(true);
                     dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                  
                     member = dialog.getSelectedMember();
                  
                     break;
               }

               if(member != null)
               {
                  inUse = true;
                  memberPanel.setVisible(true);
                  buttonsPanel.setVisible(true); 
               
                  memberIdLabel.setText(GuiUtil.htmlFont("black", Long.toString(member.getMemberId())));   
                  memberEnglishNameLabel.setText(GuiUtil.htmlFont("black", member.getName())); 
                  memberChineseNameLabel.setText(GuiUtil.htmlFont("black", member.getChineseName())); 
                  memberTypeLabel.setText(GuiUtil.htmlFont("black", ContextManager.getMemberType(member.getMemberTypeId()).getName())); 
                  
                  displayMainPanel(member.getMemberId());
               }
            }
            catch(Exception ex)
            {
               System.out.println(ex);
            }
            finally
            {
               try
               {
                  if(con != null)
                     con.close();
               }
               catch(Exception ex){}
            }        
         }
      }
   }
   
   /**
    * This is a listener for Edit Member button.
    */
   class EditMemberListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(member != null)
         {
            EditMemberInfoDialog dialog = null;            
            Connection con = null;
            try
            {
               con = ContextManager.getConnection();
               MemberInfoManager memberMgr = new MemberInfoManager(con);
               
               // bring up the edit dialog
               dialog =
                  new EditMemberInfoDialog(GuiUtil.getOwnerFrame(getParent()), true, member.getName());
               dialog.setMemberId(member.getMemberId());
               dialog.setName(member.getName());
               dialog.setChineseName(member.getChineseName());
               dialog.setHomePhone(member.getHomePhone());
               dialog.setCellPhone(member.getCellPhone());
               dialog.setAddress(member.getAddress());
               dialog.setEmail(member.getEmail());
               dialog.setMemberType(ContextManager.getMemberType(member.getMemberTypeId()));

               dialog.pack();
               dialog.setVisible(true);
               dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

               if(dialog.hasChanged())
               {    
                  member.setMemberId(dialog.getMemberId());
                  member.setName(dialog.getName());
                  member.setChineseName(dialog.getChineseName());
                  member.setHomePhone(dialog.getHomePhone());
                  member.setCellPhone(dialog.getCellPhone());
                  member.setAddress(dialog.getAddress());
                  member.setEmail(dialog.getEmail());
                  member.setMemberTypeId(dialog.getMemberType().getId());
                   
                  // update db
                  memberMgr.update(member);
                   
                  // update info 
                  memberEnglishNameLabel.setText(GuiUtil.htmlFont("black", member.getName())); 
                  memberChineseNameLabel.setText(GuiUtil.htmlFont("black", member.getChineseName())); 
                  memberTypeLabel.setText(GuiUtil.htmlFont("black", ContextManager.getMemberType(member.getMemberTypeId()).getName()));                   
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
            
            dialog.dispose();
         }
      }
   }   
   
   // action listener of Borrow button
   class BorrowListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(member != null)
         {
            AbstractAssetPanel panel = new BorrowAssetsPanel(member.getMemberId(), null);
            activeCenterPanel = replacePanel(activeCenterPanel, panel, BorderLayout.CENTER);
            activeButtonsPanel = replacePanel(activeButtonsPanel, panel.getButtonsPanel(), BorderLayout.SOUTH);
         }
      }
   }
   
   // action listener of Return button
   class ReturnListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(member != null)
         {
            String[] barcodes = null;
            int[] rowIds = borrowedTable.getSelectedRows();
            if(rowIds.length > 0)
            {
               barcodes = new String[rowIds.length];
               for(int i = 0; i < rowIds.length; i++)
               {
                  barcodes[i] = (String)borrowedTable.getValueAt(rowIds[i], 
                           BorrowedAssetsPanel.BAR_CODE_COL);
               }
            }
            
            AbstractAssetPanel panel = new ReturnAssetsPanel(member.getMemberId(), barcodes);            
            activeCenterPanel = replacePanel(activeCenterPanel, panel, BorderLayout.CENTER);
            activeButtonsPanel = replacePanel(activeButtonsPanel, panel.getButtonsPanel(), BorderLayout.SOUTH);
         }
      }
   } 
   
   // action listener of Renew button
   class RenewListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         if(member != null)
         {
            String[] barcodes = null;
            int[] rowIds = borrowedTable.getSelectedRows();
            if(rowIds.length > 0)
            {
               barcodes = new String[rowIds.length];
               for(int i = 0; i < rowIds.length; i++)
               {
                  barcodes[i] = (String)borrowedTable.getValueAt(rowIds[i], 
                           BorrowedAssetsPanel.BAR_CODE_COL);
               }

               AbstractAssetPanel panel = new BorrowAssetsPanel(member.getMemberId(), barcodes);
               activeCenterPanel = replacePanel(activeCenterPanel, panel, BorderLayout.CENTER);
               activeButtonsPanel = replacePanel(activeButtonsPanel, panel.getButtonsPanel(), BorderLayout.SOUTH);
            }
            else
               GuiUtil.messageDialog(ServiceCenterPanel.this, "No items selected");
         }
      }
   }   
}
