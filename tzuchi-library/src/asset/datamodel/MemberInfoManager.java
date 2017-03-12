package asset.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import asset.datamodel.MemberInfo;
import asset.ui.Constants;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/27/2005
 */
public class MemberInfoManager
{
   public static final String TABLE_NAME = "member_info";
   
   public static final String MEMBER_ID = "member_id";
   public static final String NAME = "name";
   public static final String CHINESE_NAME = "chinese_name";
   public static final String HOME_PHONE = "home_phone";
   public static final String CELL_PHONE = "cell_phone";
   public static final String ADDRESS = "address";
   public static final String EMAIL = "email";
   public static final String MEMBER_TYPE_ID = "member_type_id";
   
   private static final String SELECT_MEMBER_TEMPLATE = 
      "SELECT " + 
      MEMBER_ID + "," + 
      NAME + "," + 
      CHINESE_NAME + "," +
      HOME_PHONE + "," +
      CELL_PHONE + "," +      
      ADDRESS + "," +
      EMAIL + "," +
      MEMBER_TYPE_ID + 
      " FROM " + TABLE_NAME + " WHERE {0}";    
   
   private static final String SELECT_MEMBER_BY_CHECKED_OUT_ASSET =
      "member_id = (SELECT member_id FROM member_history " + 
      "WHERE is_checked_in = false AND asset_id = "+
      "(SELECT asset_id FROM asset WHERE asset_barcode=?))";
   
   private static final String INSERT_MEMBER = 
      "INSERT INTO " + TABLE_NAME + "(" + 
      MEMBER_ID + "," + 
      NAME + "," + 
      CHINESE_NAME + "," +
      HOME_PHONE + "," +
      CELL_PHONE + "," +      
      ADDRESS + "," +
      EMAIL + "," +
      MEMBER_TYPE_ID + 
      ") VALUES (?,?,?,?,?,?,?,?)";
   
   private static final String UPDATE_MEMBER = 
      "UPDATE " + TABLE_NAME + " SET " +  
      NAME + "=?," + 
      CHINESE_NAME + "=?," + 
      HOME_PHONE + "=?," +
      CELL_PHONE + "=?," +      
      ADDRESS + "=?," +
      EMAIL + "=?," +
      MEMBER_TYPE_ID + "=?" +
      " WHERE " + MEMBER_ID + "=?";
   
   private static final String SELECT_ALL_MEMBERS = 
      "SELECT * FROM " + TABLE_NAME + " ORDER BY " + NAME + "," + CHINESE_NAME;
    
   private Connection con;
   
   public MemberInfoManager(Connection con)
   {
      this.con = con;
   }
   
   public MemberInfo getMemberById(long memberId) throws SQLException
   {
      MemberInfo member = null;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         String sql = MessageFormat.format(SELECT_MEMBER_TEMPLATE, new Object[]{MEMBER_ID + "=?"});
         pstmt = con.prepareStatement(sql);
         pstmt.setLong(1, memberId);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            member = getMemberInfo(rs);
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

      return member;
   }    
   
   public MemberInfo getExternalMember(long memberId) throws SQLException
   {
      MemberInfo member = null;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         String sql = MessageFormat.format(SELECT_MEMBER_TEMPLATE, new Object[]{MEMBER_ID + "=?"});
         pstmt = con.prepareStatement(sql);
         pstmt.setLong(1, memberId);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            member = getMemberInfo(rs);
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

      return member;
   }       
   
   /**
    * Searches members by English name, Chinese name, home phone, cell phone
    * or borrowed asset.
    * 
    * @param searchStr 
    * @param searchOption either Member or Asset
    * @return
    * @throws SQLException
    */
   public List searchMembers(String searchStr, String searchOption) throws SQLException
   {
      List list = new ArrayList();
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         String sql = "";
         if(searchOption.equals(Constants.OPTION_MEMBER))
         {
            sql = MessageFormat.format(SELECT_MEMBER_TEMPLATE, 
               new Object[]{NAME + " LIKE '%" + searchStr + "%' OR " 
                  + CHINESE_NAME + " LIKE '%" + searchStr + "%' OR "
                  + HOME_PHONE + " LIKE '%" + searchStr + "%' OR "
                  + CELL_PHONE + " LIKE '%" + searchStr + "%'"});
         }
         else if(searchOption.equals(Constants.OPTION_ASSET))
         {
            sql = MessageFormat.format(SELECT_MEMBER_TEMPLATE, 
                     new Object[]{SELECT_MEMBER_BY_CHECKED_OUT_ASSET});            
         }
         
         pstmt = con.prepareStatement(sql);
         if(searchOption.equals(Constants.OPTION_ASSET))
         {
            pstmt.setString(1, searchStr);
         }
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getMemberInfo(rs));
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
   
   public List getMembers() throws SQLException
   {
      List list = new ArrayList();

      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_ALL_MEMBERS);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getMemberInfo(rs));
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
         
   public int insert(MemberInfo member) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         if(member == null)
            return result;

         pstmt = con.prepareStatement(INSERT_MEMBER);
         pstmt.setLong(1, member.getMemberId());
         pstmt.setString(2, member.getName());
         pstmt.setString(3, member.getChineseName());
         pstmt.setString(4, member.getHomePhone());
         pstmt.setString(5, member.getCellPhone());
         pstmt.setString(6, member.getAddress());
         pstmt.setString(7, member.getEmail());
         pstmt.setInt(8, member.getMemberTypeId());

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
 
   public int update(MemberInfo member) throws SQLException
   {
      int result = -1;

      PreparedStatement pstmt = null;      
      try
      {
         if(member == null)
            return result;

         pstmt = con.prepareStatement(UPDATE_MEMBER);
         pstmt.setString(1, member.getName());
         pstmt.setString(2, member.getChineseName());
         pstmt.setString(3, member.getHomePhone());
         pstmt.setString(4, member.getCellPhone());
         pstmt.setString(5, member.getAddress());
         pstmt.setString(6, member.getEmail());
         pstmt.setInt(7, member.getMemberTypeId());
         pstmt.setLong(8, member.getMemberId());
         
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
      
   /**
    * Deletes a member.
    *
    * @param con a database connection
    * @param memberId member ID of member to be removed
    * @return the row count
    */
   public int delete(long memberId) throws SQLException
   { 
      int result = -1;
     
      try 
      {
         String query = "DELETE FROM " + TABLE_NAME +
                        " WHERE " +
                        MEMBER_ID + "=?";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, memberId); 
       
         result = pstmt.executeUpdate();   
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());  
      }
     
      return result;
   }   
   
   private MemberInfo getMemberInfo(ResultSet rs) throws SQLException
   {
      MemberInfo member = new MemberInfo();
      member.setMemberId(rs.getLong(MEMBER_ID));
      member.setName(rs.getString(NAME));
      member.setChineseName(rs.getString(CHINESE_NAME));
      member.setHomePhone(rs.getString(HOME_PHONE));
      member.setCellPhone(rs.getString(CELL_PHONE));   
      member.setAddress(rs.getString(ADDRESS));       
      member.setEmail(rs.getString(EMAIL));
      member.setMemberTypeId(rs.getInt(MEMBER_TYPE_ID));
    
      return member;
   }   
}
