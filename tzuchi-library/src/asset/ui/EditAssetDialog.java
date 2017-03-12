package asset.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import members.ui.utils.GuiUtil;
import members.utils.Utility;
import asset.datamodel.AssetCategory;
import asset.datamodel.AssetStatus;
import asset.datamodel.AssetType;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class EditAssetDialog extends JDialog
{
   private boolean newAsset = false;
   private boolean hasChanged = false;

   private JTextField assetCodeField;
   private JTextField assetBarcodeField;
   private JTextField vendorBarcodeField;
   private JTextField assetNameField;
   private JTextField authorField;
   private JTextField publishingComField;
   private JTextField publishedYearField;
   private JTextField publishedMonthField;
   private JTextField publishedDayField;   
   private JComboBox catCombo;
   private JComboBox typeCombo;
   private JComboBox statusCombo;

   private JButton okButton;
   private JButton cancelButton;

   // for new asset
   public EditAssetDialog(Frame owner, boolean modal)
   {
      super(owner, "New Asset", modal);

      newAsset = true;
      
      init();
   }

   // for existing asset
   public EditAssetDialog(Frame owner, boolean modal, String assetName)
   {
      super(owner, modal);

      setTitle("Edit Asset " + assetName);

      init();
   }

   public String getAssetCode()
   {
      return (String)assetCodeField.getText().trim();
   }

   public void setAssetCode(String assetCode)
   {
      assetCodeField.setText(assetCode);
   }

   public String getAssetBarcode()
   {
      return (String)assetBarcodeField.getText().trim();
   }

   public void setAssetBarcode(String assetBarcode)
   {
      assetBarcodeField.setText(assetBarcode);
   }

   public String getVendorBarcode()
   {
      return (String)vendorBarcodeField.getText().trim();
   }

   public void setVendorBarcode(String vendorBarcode)
   {
      vendorBarcodeField.setText(vendorBarcode);
   }   
   
   public String getAssetName()
   {
      return (String)assetNameField.getText().trim();
   }

   public void setAssetName(String assetName)
   {
      assetNameField.setText(assetName);
   }
   
   public String getPublishingCompany()
   {
      return (String)publishingComField.getText().trim();
   }

   public void setPublishingCompany(String publishingCompany)
   {
      publishingComField.setText(publishingCompany);
   }   

   public String getAuthor()
   {
      return (String)authorField.getText().trim();
   }

   public void setAuthor(String author)
   {
      authorField.setText(author);
   }
   
   public Date getPublishedDate()
   {
      String yearStr = publishedYearField.getText().trim();
      String monthStr = publishedMonthField.getText().trim();
      String dayStr = publishedDayField.getText().trim();

      Date publishedDate = null;
      if(yearStr.length() != 0 || monthStr.length() != 0 || dayStr.length() != 0)
      {
         int year = 1900;
         if(yearStr.length() != 0)
            year = Integer.parseInt(yearStr);
         int month = 0;  // 0-based
         if(monthStr.length() != 0)
            month = Integer.parseInt(monthStr) - 1;
         int day = 1;
         if(dayStr.length() != 0)
            day = Integer.parseInt(dayStr);
      
         Calendar calendar = Calendar.getInstance();
         calendar.set(year, month, day);
         publishedDate = calendar.getTime();
      }
      
      return publishedDate;
   }

   public void setPublishedDate(Date publishedDate)
   {
      if(publishedDate != null)
      {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(publishedDate);
      
         publishedYearField.setText(String.valueOf(calendar.get(Calendar.YEAR)));
         publishedMonthField.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));  // 0-based
         publishedDayField.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
      }
   }   
   
   public AssetCategory getCategory()
   {
      return (AssetCategory)catCombo.getSelectedItem();
   }

   public void setCategory(AssetCategory obj)
   {
      catCombo.setSelectedItem(obj);
   }   
   
   public AssetType getType()
   {
      return (AssetType)typeCombo.getSelectedItem();
   }

   public void setType(AssetType obj)
   {
      typeCombo.setSelectedItem(obj);
   }    
   
   public AssetStatus getStatus()
   {
      return (AssetStatus)statusCombo.getSelectedItem();
   }

   public void setStatus(AssetStatus obj)
   {
      statusCombo.setSelectedItem(obj);
   }       
   
   public boolean hasChanged()
   {
      return hasChanged;
   }

   private void init()
   {
      // not resizable
      setResizable(false);

      Container content = getContentPane();
      // set layout
      content.setLayout(new BorderLayout());

      JPanel dataPanel = new JPanel();
      dataPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints cons = new GridBagConstraints();
      dataPanel.setLayout(gridbag);
      cons.anchor = GridBagConstraints.WEST;
      cons.insets = new Insets(2, 0, 2, 0);

      // label for asset code
      cons.gridx = 0;
      cons.gridy = 0;
      JLabel assetCodeLabel = new JLabel("Asset Code ");
      gridbag.setConstraints(assetCodeLabel, cons);
      dataPanel.add(assetCodeLabel);
      // text field for asset code
      cons.gridx = 1;      
      cons.gridy = 0;
      assetCodeField = new JTextField(50);
      gridbag.setConstraints(assetCodeField, cons);
      dataPanel.add(assetCodeField);

      // label for asset name
      cons.gridx = 0;
      cons.gridy = 1;
      JLabel assetNameLabel = new JLabel("Asset Name ");
      gridbag.setConstraints(assetNameLabel, cons);
      dataPanel.add(assetNameLabel);
      // text field for asset name
      cons.gridx = 1;
      cons.gridy = 1;
      assetNameField = new JTextField(50);
      gridbag.setConstraints(assetNameField, cons);
      dataPanel.add(assetNameField);

      // label for barcode
      cons.gridx = 0;
      cons.gridy = 2;
      JLabel barcodeLabel = new JLabel("Asset Barcode ");
      gridbag.setConstraints(barcodeLabel, cons);
      dataPanel.add(barcodeLabel);
      // text field for barcode
      cons.gridx = 1;      
      cons.gridy = 2;
      assetBarcodeField = new JTextField(50);
      gridbag.setConstraints(assetBarcodeField, cons);
      dataPanel.add(assetBarcodeField);      
      
      // label for publishing company
      cons.gridx = 0;
      cons.gridy = 3;
      JLabel publishingComLabel = new JLabel("Publishing Company ");
      gridbag.setConstraints(publishingComLabel, cons);
      dataPanel.add(publishingComLabel);
      // text field for publishing company
      cons.gridx = 1;
      cons.gridy = 3;
      publishingComField = new JTextField(50);
      gridbag.setConstraints(publishingComField, cons);
      dataPanel.add(publishingComField);

      // label for author
      cons.gridx = 0;
      cons.gridy = 4;
      JLabel authorLabel = new JLabel("Author ");
      gridbag.setConstraints(authorLabel, cons);
      dataPanel.add(authorLabel);
      // text field for author
      cons.gridx = 1;
      cons.gridy = 4;
      authorField = new JTextField(50);
      gridbag.setConstraints(authorField, cons);
      dataPanel.add(authorField);  
      
      // label for published date
      cons.gridx = 0;
      cons.gridy = 5;
      JLabel publishedDateLabel = new JLabel("Published Date ");
      gridbag.setConstraints(publishedDateLabel, cons);
      dataPanel.add(publishedDateLabel);
      // text field for publishedDate
      JPanel publishedDatePanel = new JPanel();
      publishedYearField = new JTextField(4); 
      publishedDatePanel.add(publishedYearField); 
      publishedDatePanel.add(new JLabel("-"));       
      publishedMonthField = new JTextField(2);
      publishedDatePanel.add(publishedMonthField);
      publishedDatePanel.add(new JLabel("-"));       
      publishedDayField = new JTextField(2);
      publishedDatePanel.add(publishedDayField); 
      publishedDatePanel.add(new JLabel(" (yyyy-mm-dd)"));       
      
      cons.gridx = 1;
      cons.gridy = 5;
      gridbag.setConstraints(publishedDatePanel, cons);      
      dataPanel.add(publishedDatePanel);
      
      // label for original barcode on the item
      cons.gridx = 0;
      cons.gridy = 6;
      JLabel vendorBarcodeLabel = new JLabel("Vendor Barcode ");
      gridbag.setConstraints(vendorBarcodeLabel, cons);
      dataPanel.add(vendorBarcodeLabel);
      // text field for original barcode
      cons.gridx = 1;      
      cons.gridy = 6;
      vendorBarcodeField = new JTextField(50);
      gridbag.setConstraints(vendorBarcodeField, cons);
      dataPanel.add(vendorBarcodeField);        
      
      // label for category
      cons.gridx = 0;
      cons.gridy = 7;
      JLabel catLabel = new JLabel("Category ");
      gridbag.setConstraints(catLabel, cons);
      dataPanel.add(catLabel);
      // combo box for category
      cons.gridx = 1;
      cons.gridy = 7;      
      catCombo = new JComboBox(ContextManager.getAssetCategoryList());
      gridbag.setConstraints(catCombo, cons);
      dataPanel.add(catCombo);    
      
      // label for type
      cons.gridx = 0;
      cons.gridy = 8;
      JLabel typeLabel = new JLabel("Type ");
      gridbag.setConstraints(typeLabel, cons);
      dataPanel.add(typeLabel);
      // combo box for type
      cons.gridx = 1;
      cons.gridy = 8;      
      typeCombo = new JComboBox(ContextManager.getAssetTypeList());
      gridbag.setConstraints(typeCombo, cons);
      dataPanel.add(typeCombo);       
      
      // label for status
      cons.gridx = 0;
      cons.gridy = 9;
      JLabel statusLabel = new JLabel("Status ");
      gridbag.setConstraints(statusLabel, cons);
      dataPanel.add(statusLabel);
      // combo box for status
      cons.gridx = 1;
      cons.gridy = 9;      
      statusCombo = new JComboBox(ContextManager.getAssetStatusList());
      gridbag.setConstraints(statusCombo, cons);
      dataPanel.add(statusCombo);      
      
      content.add(dataPanel, BorderLayout.NORTH);

      // button panel
      JPanel buttonPanel = new JPanel();

      okButton = new JButton("Ok");
      cancelButton = new JButton("Cancel");
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);

      content.add(buttonPanel, BorderLayout.SOUTH);

      // register listeners
      MyActionListener actionListener = new MyActionListener();
      okButton.addActionListener(actionListener);
      cancelButton.addActionListener(actionListener);
      assetCodeField.addFocusListener(new AssetCodeListener());
      
      // window listener
      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            closeDialog();
         }
      });
   }

   private void closeDialog()
   {
   }
   
   /**
    * This is the listener for asset code field. Category will be changed automatically 
    * based on the first digit of the asset code. 
    */
   class AssetCodeListener extends FocusAdapter
   {
      public void focusLost(FocusEvent e) 
      {
         String assetCode = getAssetCode();
         if(!Utility.isEmpty(assetCode))
         {
            String categoryCode = assetCode.substring(0, 1);  // get the 1st digit of the asset code
            int categoryId = 0;
            try
            {
               categoryId = Integer.parseInt(categoryCode);
            }
            catch(NumberFormatException ex)
            {
            }
            AssetCategory category = ContextManager.getAssetCategory(categoryId);
            setCategory(category);
         }
      }
   }

   /**
    * This is the listener for Ok, Cancel and Help buttons.
    */
   class MyActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(okButton == e.getSource()) // OK button
         {
            if(Utility.isEmpty(getAssetCode()))
            {
               GuiUtil.messageDialog(EditAssetDialog.this,
                     "Asset Code should not be empty.");
               return;
            }

            if(Utility.isEmpty(getAssetName()))
            {
               GuiUtil.messageDialog(EditAssetDialog.this,
                     "Asset Name should not be empty.");
               return;
            }

            // for creating a new asset, check if this asset code exists
            /*if(newAsset)
            {
               Connection con = null;
               try
               {
                  con = ContextManager.getConnection();

                  AssetManager assetMgr = new AssetManager(con);
                  if(assetMgr.exists(getAssetCode()))
                  {
                     GuiUtil.messageDialog(EditAssetDialog.this,
                           "Duplicate Asset Code.");
                     return;
                  }
               }
               catch(Exception se)
               {
                  System.out.println(se);
               }
               finally
               {
                  try
                  {
                     con.close();
                  }
                  catch(Exception ce){}
               }
            }*/

            hasChanged = true;
         }
         else if(cancelButton == e.getSource()) // Cancel button
         {
            closeDialog();
         }

         setVisible(false);
      }
   }
}
