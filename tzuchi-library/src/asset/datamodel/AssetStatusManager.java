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
public class AssetStatusManager
{
   public static final String TABLE_NAME = "asset_status";
   
   public static final String ASSET_STATUS_ID = "asset_status_id";
   public static final String NAME = "name";
   
   private static final String SELECT_ALL_STATUSES = 
      "SELECT " + ASSET_STATUS_ID + "," + 
      NAME + " FROM " + TABLE_NAME + " ORDER BY " + NAME;
    
   private Connection con;
   
   public AssetStatusManager(Connection con)
   {
      this.con = con;
   }
   
   public Vector getAssetStatusList() throws SQLException
   {
      Vector list = new Vector();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_ALL_STATUSES);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getAssetStatus(rs));
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
   
   private AssetStatus getAssetStatus(ResultSet rs) throws SQLException
   {
      AssetStatus status = new AssetStatus();
      status.setId(rs.getInt(ASSET_STATUS_ID));
      status.setName(rs.getString(NAME));
    
      return status;      
   }
}
