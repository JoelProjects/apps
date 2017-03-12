package asset.datamodel;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import members.utils.Utility;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import asset.ui.ContextManager;

/**
 * This is a manager class to generate an Excel file for reporting.
 *
 * @author Cheng-Hung Chou
 * @since 12/26/2007
 */
public class ExportManager
{
   private Connection con;
   public ExportManager(Connection con)
   {
      this.con = con;
   }
   
   public void export()
   {
      try
      {
         HSSFWorkbook wb = new HSSFWorkbook();
         HSSFSheet sheet = wb.createSheet("資產表");

         int row = 0;
         // an empty row
         sheet.createRow(row++);
         // date row         
         HSSFRow dateRow = sheet.createRow(row++);
         dateRow.createCell((short)1).setCellValue(Utility.getDateTimeString(new Date()));
         // an empty row
         sheet.createRow(row++);

         // header rows
         sheet.setColumnWidth((short)2, (short)2560);  // 10
         sheet.setColumnWidth((short)4, (short)12800);  // 50 characters
         sheet.setColumnWidth((short)5, (short)2560); 
         
         HSSFRow headerRow1 = sheet.createRow(row++);
         HSSFFont columnFont = wb.createFont();
         columnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
         columnFont.setFontHeightInPoints((short)12);
         HSSFRichTextString columnName = null;
         columnName = new HSSFRichTextString("序號");
         columnName.applyFont(columnFont);
         headerRow1.createCell((short)1).setCellValue(columnName);
         columnName = new HSSFRichTextString("條碼");
         columnName.applyFont(columnFont);
         headerRow1.createCell((short)2).setCellValue(columnName);
         columnName = new HSSFRichTextString("編碼");
         columnName.applyFont(columnFont);
         headerRow1.createCell((short)3).setCellValue(columnName);
         columnName = new HSSFRichTextString("名稱");
         columnName.applyFont(columnFont);
         headerRow1.createCell((short)4).setCellValue(columnName);
         columnName = new HSSFRichTextString("種類");
         columnName.applyFont(columnFont);         
         headerRow1.createCell((short)5).setCellValue(columnName);
         columnName = new HSSFRichTextString("類別");
         columnName.applyFont(columnFont); 
         headerRow1.createCell((short)6).setCellValue(columnName);
         columnName = new HSSFRichTextString("狀態");
         columnName.applyFont(columnFont);          
         headerRow1.createCell((short)7).setCellValue(columnName);     

         // contents
         AssetManager assetMgr = new AssetManager(con);
         List assets = assetMgr.getAssets(null, -1, -1, -1);            
         Iterator it = assets.iterator();
         int idx = 0;
         while(it.hasNext())
         {
            idx++;
            HSSFRow dataRow = sheet.createRow(row++);
            Asset asset = (Asset)it.next();
            dataRow.createCell((short)1).setCellValue(idx);
            dataRow.createCell((short)2).setCellValue(new HSSFRichTextString(asset.getAssetBarcode()));               
            dataRow.createCell((short)3).setCellValue(new HSSFRichTextString(asset.getAssetCode()));
            dataRow.createCell((short)4).setCellValue(new HSSFRichTextString(asset.getName()));
            dataRow.createCell((short)5).setCellValue(
               new HSSFRichTextString(ContextManager.getAssetCategory(asset.getAssetCategoryId()).getName()));
            dataRow.createCell((short)6).setCellValue(
               new HSSFRichTextString(ContextManager.getAssetType(asset.getAssetTypeId()).getName()));
            dataRow.createCell((short)7).setCellValue(
               new HSSFRichTextString(ContextManager.getAssetStatus(asset.getAssetStatusId()).getName()));             
         }

         FileOutputStream out = new FileOutputStream("assets.xls");
         wb.write(out);
         out.close();  
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
}
