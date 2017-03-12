package asset.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asset.datamodel.Asset;

/**
 * @author Cheng-Hung Chou
 * @since 3/6/2005
 */
public class AssetManager
{
   public static final String TABLE_NAME = "asset";
   
   public static final String ASSET_ID = "asset_id";
   public static final String ASSET_CODE = "asset_code";
   public static final String ASSET_BARCODE = "asset_barcode";
   public static final String VENDOR_BARCODE = "vendor_barcode";
   public static final String NAME = "name";
   public static final String AUTHOR = "author";
   public static final String PUBLISHING_COMPANY = "publishing_company";
   public static final String PUBLISHED_DATE = "published_date";
   public static final String ASSET_CATEGORY_ID = "asset_category_id";
   public static final String ASSET_TYPE_ID = "asset_type_id";
   public static final String ENTRY_DATE = "entry_date";
   public static final String ASSET_STATUS_ID = "asset_status_id";
   public static final String USER_ID = "user_id";
   public static final String STATUS_MOD_DATE = "status_mod_date";
   
   private static final String SELECT_ASSET_TEMPLATE = 
      "SELECT " + 
      ASSET_ID + "," +       
      ASSET_CODE + "," + 
      ASSET_BARCODE + "," + 
      VENDOR_BARCODE + "," + 
      NAME + "," + 
      AUTHOR + "," +
      PUBLISHING_COMPANY + "," +
      PUBLISHED_DATE + "," +      
      ASSET_CATEGORY_ID + "," +
      ASSET_TYPE_ID + "," + 
      ASSET_STATUS_ID + 
      " FROM " + TABLE_NAME + " WHERE {0}=?";    
   
   private static final String INSERT_ASSET = 
      "INSERT INTO " + TABLE_NAME + "(" + 
      ASSET_CODE + "," + 
      ASSET_BARCODE + "," + 
      NAME + "," + 
      AUTHOR + "," + 
      PUBLISHING_COMPANY + "," +
      PUBLISHED_DATE + "," +      
      ASSET_CATEGORY_ID + "," +
      ASSET_TYPE_ID + "," +
      ASSET_STATUS_ID + "," + 
      VENDOR_BARCODE + 
      ") VALUES (?,?,?,?,?,?,?,?,?,?)";
   
   private static final String UPDATE_ASSET = 
      "UPDATE " + TABLE_NAME + " SET " + 
      ASSET_CODE + "=?," +       
      ASSET_BARCODE + "=?," + 
      NAME + "=?," + 
      AUTHOR + "=?," + 
      PUBLISHING_COMPANY + "=?," +
      PUBLISHED_DATE + "=?," +      
      ASSET_CATEGORY_ID + "=?," +
      ASSET_TYPE_ID + "=?," +
      ASSET_STATUS_ID + "=?," +
      VENDOR_BARCODE + "=?" + 
      " WHERE " + ASSET_ID + "=?";   
   
   private static final String UPDATE_ASSET_STATUS = 
      "UPDATE " + TABLE_NAME + " SET " + 
      ASSET_STATUS_ID + "=?," +
      STATUS_MOD_DATE + "=?" + 
      " WHERE " + ASSET_ID + "=?";
      
   private static final String SELECT_ASSETS_TEMPLATE = 
      "SELECT " + 
      ASSET_ID + "," +       
      ASSET_CODE + "," + 
      ASSET_BARCODE + "," + 
      VENDOR_BARCODE + "," + 
      NAME + "," + 
      AUTHOR + "," +
      PUBLISHING_COMPANY + "," +
      PUBLISHED_DATE + "," +      
      ASSET_CATEGORY_ID + "," +
      ASSET_TYPE_ID + "," + 
      ASSET_STATUS_ID + 
      " FROM " + TABLE_NAME + " {0} ORDER BY " + ASSET_CODE; 
   private static final String SELECT_ALL_ASSETS = 
      MessageFormat.format(SELECT_ASSETS_TEMPLATE, new Object[]{""});
   
   private static final String SELECT_ASSET_CODE = 
      "SELECT " + ASSET_CODE + " FROM " + TABLE_NAME + " WHERE " + ASSET_CODE + "=?";
   
   private Connection con;
   
   public AssetManager(Connection con)
   {
      this.con = con;
   }
   
   public Asset getAsset(int assetId) throws SQLException
   {
      Asset asset = null;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         String sql = MessageFormat.format(SELECT_ASSET_TEMPLATE, new Object[]{ASSET_ID});
         pstmt = con.prepareStatement(sql);
         pstmt.setInt(1, assetId);
         rs = pstmt.executeQuery();

         if(rs.next())
         {
            asset = getAsset(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(rs != null)
            rs.close();
         if(pstmt != null)
            pstmt.close();
      }

      return asset;
   }    
   
   public Asset getAsset(String barcode) throws SQLException
   {
      Asset asset = null;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         // like asset ID, asset barcode is unique too
         String sql = MessageFormat.format(SELECT_ASSET_TEMPLATE, new Object[]{ASSET_BARCODE});
         pstmt = con.prepareStatement(sql);
         pstmt.setString(1, barcode);
         rs = pstmt.executeQuery();

         if(rs.next())
         {
            asset = getAsset(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(rs != null)
            rs.close();
         if(pstmt != null)
            pstmt.close();
      }

      return asset;
   }       
   
   public List getAssets() throws SQLException
   {
      List list = new ArrayList();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_ALL_ASSETS);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getAsset(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(rs != null)
            rs.close();
         if(pstmt != null)
            pstmt.close();
      }

      return list;
   } 
   
   public List getAssets(String name, int categoryId, int typeId, int statusId) 
      throws SQLException
   {
      List list = new ArrayList();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         String selector = assetSelector(name, categoryId, typeId, statusId);
         String sql = MessageFormat.format(SELECT_ASSETS_TEMPLATE, new Object[]{selector});
         pstmt = con.prepareStatement(sql);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getAsset(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(rs != null)
            rs.close();
         if(pstmt != null)
            pstmt.close();
      }

      return list;
   }    
   
   private String assetSelector(String name, int categoryId, int typeId, int statusId)
   {
      StringBuffer selector = new StringBuffer();
      
      boolean appendAnd = false;   
      // name or barcode
      if(name != null)
      {
         name = name.trim();
         if(name.length() > 0)
         {
            
            selector = selector.append("WHERE (").append(NAME).append(" LIKE '%").append(name).append("%' ");  
            selector = selector.append("OR ").append(ASSET_BARCODE).append(" LIKE '%").append(name).append("%')");  
            appendAnd = true;
         }
      }
      
      if(categoryId > 0 || typeId > 0 || statusId > 0)
      {
         if(!appendAnd)
            selector.append("WHERE ");
         
         if(categoryId > 0)
         {
            if(appendAnd)
               selector.append(" AND ");            
            selector.append(ASSET_CATEGORY_ID).append("=").append(categoryId);
            appendAnd = true;
         }
         if(typeId > 0)
         {
            if(appendAnd)
               selector.append(" AND ");
            
            selector.append(ASSET_TYPE_ID).append("=").append(typeId);
            
            appendAnd = true;
         }
         if(statusId > 0)
         {
            if(appendAnd)
               selector.append(" AND ");
            
            selector = selector.append(ASSET_STATUS_ID).append("=").append(statusId);
            appendAnd = true;
         }
      }
      
      return selector.toString();
   }
   
   public int insert(Asset asset) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         if(asset == null)
            return result;

         pstmt = con.prepareStatement(INSERT_ASSET);
         pstmt.setString(1, asset.getAssetCode());
         pstmt.setString(2, asset.getAssetBarcode());
         pstmt.setString(3, asset.getName());
         pstmt.setString(4, asset.getAuthor());
         pstmt.setString(5, asset.getPublishingCompany());
         if(asset.getPublishedDate() != null)         
            pstmt.setDate(6, new java.sql.Date(asset.getPublishedDate().getTime())); 
         else
            pstmt.setDate(6, null);
         pstmt.setInt(7, asset.getAssetCategoryId());
         pstmt.setInt(8, asset.getAssetTypeId());
         pstmt.setInt(9, asset.getAssetStatusId());
         pstmt.setString(10, asset.getVendorBarcode());

         result = pstmt.executeUpdate();
         
         pstmt.close();
         
         // get inserted ID
         pstmt = con.prepareStatement("SELECT @@IDENTITY FROM " + TABLE_NAME);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
         {
            result = rs.getInt(1);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(pstmt != null)
            pstmt.close();
      }

      return result;
   }
 
   public int update(Asset asset) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         if(asset == null)
            return result;

         pstmt = con.prepareStatement(UPDATE_ASSET);
         pstmt.setString(1, asset.getAssetCode());         
         pstmt.setString(2, asset.getAssetBarcode());
         pstmt.setString(3, asset.getName());
         pstmt.setString(4, asset.getAuthor());
         pstmt.setString(5, asset.getPublishingCompany());
         if(asset.getPublishedDate() != null)
            pstmt.setDate(6, new java.sql.Date(asset.getPublishedDate().getTime())); 
         else
            pstmt.setDate(6, null);
         pstmt.setInt(7, asset.getAssetCategoryId());
         pstmt.setInt(8, asset.getAssetTypeId());
         pstmt.setInt(9, asset.getAssetStatusId());
         pstmt.setString(10, asset.getVendorBarcode());
         // key for update
         pstmt.setInt(11, asset.getAssetId());
         
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(pstmt != null)
            pstmt.close();
      }

      return result;
   }   
   
   public int updateStatus(int assetId, int statusId, Date modDate) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         pstmt = con.prepareStatement(UPDATE_ASSET_STATUS);
         pstmt.setInt(1, statusId);
         pstmt.setTimestamp(2, new Timestamp(modDate.getTime()));
         // key for update
         pstmt.setInt(3, assetId);
         
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(pstmt != null)
            pstmt.close();
      }

      return result;
   }      
   
   public boolean exists(String assetCode) throws SQLException
   {
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      boolean existed = false;
      try
      {
         pstmt = con.prepareStatement(SELECT_ASSET_CODE);
         pstmt.setString(1, assetCode);
         rs = pstmt.executeQuery();

         if(rs.next())
         {
            existed = true;
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(rs != null)
            rs.close();
         if(pstmt != null)
            pstmt.close();
      }

      return existed;
   }  
   
   /**
    * Deletes an asset.
    *
    * @param con a database connection
    * @param assetId asset ID of assset to be removed
    * @return the row count
    */
   public int delete(int assetId) throws SQLException
   { 
      int result = -1;
     
      try 
      {
         String query = "DELETE FROM " + TABLE_NAME +
                        " WHERE " +
                        ASSET_ID + "=?";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, assetId); 
       
         result = pstmt.executeUpdate();   
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());  
      }
     
      return result;
   }   
   
   private Asset getAsset(ResultSet rs) throws SQLException
   {
      Asset asset = new Asset();
      asset.setAssetId(rs.getInt(ASSET_ID));
      asset.setAssetCode(rs.getString(ASSET_CODE));
      asset.setAssetBarcode(rs.getString(ASSET_BARCODE));
      asset.setVendorBarcode(rs.getString(VENDOR_BARCODE));
      asset.setName(rs.getString(NAME));
      asset.setAuthor(rs.getString(AUTHOR));
      asset.setPublishingCompany(rs.getString(PUBLISHING_COMPANY));   
      asset.setPublishedDate(rs.getDate(PUBLISHED_DATE));       
      asset.setAssetCategoryId(rs.getInt(ASSET_CATEGORY_ID));
      asset.setAssetTypeId(rs.getInt(ASSET_TYPE_ID));
      asset.setAssetStatusId(rs.getInt(ASSET_STATUS_ID));
    
      return asset;
   }   
}
