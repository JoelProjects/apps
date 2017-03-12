package asset.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author Cheng-Hung Chou
 * @since 3/11/2005
 */
public class AssetCategoryManager
{
   public static final String TABLE_NAME = "asset_category";
   
   public static final String ASSET_CATEGORY_ID = "asset_category_id";
   public static final String NAME = "name";
   
   private static final String SELECT_ALL_CATEGORIES = 
      "SELECT " + ASSET_CATEGORY_ID + "," + 
      NAME + " FROM " + TABLE_NAME + " ORDER BY " + ASSET_CATEGORY_ID;
    
   private Connection con;
   
   public AssetCategoryManager(Connection con)
   {
      this.con = con;
   }
   
   public Vector getAssetCategoryList() throws SQLException
   {
      Vector list = new Vector();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_ALL_CATEGORIES);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getAssetCategory(rs));
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
   
   private AssetCategory getAssetCategory(ResultSet rs) throws SQLException
   {
      AssetCategory category = new AssetCategory();
      category.setId(rs.getInt(ASSET_CATEGORY_ID));
      category.setName(rs.getString(NAME));
    
      return category;      
   }
}
