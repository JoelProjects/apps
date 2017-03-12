package asset.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 10/28/2005
 */
public class SystemDataManager
{
   // this table only contains one row of data
   public static final String TABLE_NAME = "system_data";
   
   public static final String LAST_SCAN_DATE = "last_scan_date";
   
   public static final String GET_LAST_SCAN_DATE = 
      "SELECT " + LAST_SCAN_DATE + " FROM " + TABLE_NAME;   
   public static final String UPDATE_LAST_SCAN_DATE = 
      "UPDATE " + TABLE_NAME + " SET " + LAST_SCAN_DATE + "=?";
   
   private Connection con;
   
   public SystemDataManager(Connection con)
   {
      this.con = con;
   }   
   
   public Date getLastScanDate() throws SQLException
   {
      Date date = null;
      Statement stmt = null;      
      try
      {
         stmt = con.createStatement(); 
         ResultSet rs = stmt.executeQuery(GET_LAST_SCAN_DATE);
         
         if(rs.next())
         {
            date = rs.getTimestamp(1);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      finally
      {
         if(stmt != null)
            stmt.close();
      }

      return date;
   }            
   
   public int updateLastScanDate(Date scanDate) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         pstmt = con.prepareStatement(UPDATE_LAST_SCAN_DATE);
         pstmt.setTimestamp(1, new Timestamp(scanDate.getTime()));
         
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
}
