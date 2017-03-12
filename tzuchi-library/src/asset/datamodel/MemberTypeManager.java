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
public class MemberTypeManager
{
   public static final String TABLE_NAME = "member_type";
   
   public static final String MEMBER_TYPE_ID = "member_type_id";
   public static final String NAME = "name";
   
   private static final String SELECT_ALL_TYPES = 
      "SELECT " + MEMBER_TYPE_ID + "," + 
      NAME + " FROM " + TABLE_NAME + " ORDER BY " + MEMBER_TYPE_ID;
    
   private Connection con;
   
   public MemberTypeManager(Connection con)
   {
      this.con = con;
   }
   
   public Vector getMemberTypeList() throws SQLException
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
            list.add(getMemberType(rs));
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
   
   private MemberType getMemberType(ResultSet rs) throws SQLException
   {
      MemberType type = new MemberType();
      type.setId(rs.getInt(MEMBER_TYPE_ID));
      type.setName(rs.getString(NAME));
    
      return type;      
   }
}
