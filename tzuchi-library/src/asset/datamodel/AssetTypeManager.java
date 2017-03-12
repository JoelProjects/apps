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
public class AssetTypeManager
{
   public static final String TABLE_NAME = "asset_type";
   
   public static final String ASSET_TYPE_ID = "asset_type_id";
   public static final String NAME = "name";
   
   private static final String SELECT_ALL_TYPES = 
      "SELECT " + ASSET_TYPE_ID + "," + 
      NAME + " FROM " + TABLE_NAME + " ORDER BY " + NAME;
    
   private Connection con;
   
   public AssetTypeManager(Connection con)
   {
      this.con = con;
   }
   
   public Vector getAssetTypeList() throws SQLException
   {
      Vector list = new Vector();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_ALL_TYPES);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getAssetType(rs));
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
   
   private AssetType getAssetType(ResultSet rs) throws SQLException
   {
      AssetType type = new AssetType();
      type.setId(rs.getInt(ASSET_TYPE_ID));
      type.setName(rs.getString(NAME));
    
      return type;      
   }
}
