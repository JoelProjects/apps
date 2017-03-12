package asset.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Cheng-Hung Chou
 * @since 9/14/2005
 */
public class MemberHistoryManager
{
   public static final String TABLE_NAME = "member_history";
   
   public static final String MEMBER_ID = "member_id";
   public static final String ASSET_ID = "asset_id";
   public static final String CHECKOUT_DATE = "checkout_date";
   public static final String CHECKIN_DATE = "checkin_date";
   public static final String DUE_DATE = "due_date";
   public static final String IS_CHECKED_IN = "is_checked_in";
   
   // checked out but not dued for a member
   private static final String SELECT_CHECKOUT_ASSETS = 
      "SELECT " + 
      "a." + AssetManager.NAME + "," +     
      "a." + AssetManager.ASSET_CODE + "," +  
      "a." + AssetManager.ASSET_BARCODE + "," + 
      "a." + AssetManager.ASSET_TYPE_ID + "," + 
      "m." + ASSET_ID + "," + 
      "m." + CHECKOUT_DATE + "," + 
      "m." + CHECKIN_DATE + "," + 
      "m." + DUE_DATE +  "," +
      "m." + IS_CHECKED_IN + 
      " FROM " + TABLE_NAME + " m LEFT JOIN " + AssetManager.TABLE_NAME + " a ON " +
      "a." + AssetManager.ASSET_ID + "=" + "m." + ASSET_ID +
      " WHERE " + MEMBER_ID + "=? AND " + IS_CHECKED_IN + "=0";  
   
   // checked out but not dued based on barcode
   private static final String SELECT_CHECKOUT_ASSET_BY_BARCODE = 
      "SELECT " + 
      "a." + AssetManager.NAME + "," +     
      "a." + AssetManager.ASSET_CODE + "," +  
      "a." + AssetManager.ASSET_BARCODE + "," + 
      "a." + AssetManager.ASSET_TYPE_ID + "," + 
      "m." + ASSET_ID + "," + 
      "m." + CHECKOUT_DATE + "," + 
      "m." + CHECKIN_DATE + "," + 
      "m." + DUE_DATE +  "," +
      "m." + IS_CHECKED_IN + 
      " FROM " + TABLE_NAME + " m LEFT JOIN " + AssetManager.TABLE_NAME + " a ON " +
      "a." + AssetManager.ASSET_ID + "=" + "m." + ASSET_ID +
      " WHERE a." + AssetManager.ASSET_BARCODE + "=? AND "+ MEMBER_ID + "=? AND " + IS_CHECKED_IN + "=0";     
   
   private static final String INSERT_MEMBER_HISTORY = 
      "INSERT INTO " + TABLE_NAME + "(" + 
      MEMBER_ID + "," + 
      ASSET_ID + "," + 
      CHECKOUT_DATE + "," +
      DUE_DATE +
      ") VALUES (?,?,?,?)";   
   
   private static final String UPDATE_CHECKIN_DATE = 
      "UPDATE " + TABLE_NAME + " SET " +  
      CHECKIN_DATE + "=?," + 
      IS_CHECKED_IN + "=?" + 
      " WHERE " + 
      MEMBER_ID + "=? AND " +
      ASSET_ID + "=? AND " +
      IS_CHECKED_IN + "=?";
   
   private static final String UPDATE_DUE_DATE = 
      "UPDATE " + TABLE_NAME + " SET " +  
      DUE_DATE + "=?" + 
      " WHERE " + 
      MEMBER_ID + "=? AND " +
      ASSET_ID + "=? AND " +
      IS_CHECKED_IN + "=0";   
   
   private static final String GET_OVERDUE_ASSETS = 
      "SELECT " + ASSET_ID + " FROM " + TABLE_NAME +
      " WHERE is_checked_in=0 AND " + DUE_DATE + "<?";
   
   private Connection con;
   
   public MemberHistoryManager(Connection con)
   {
      this.con = con;
   }
   
   /**
    * Gets assets been checked out but not yet dued for a member.
    * 
    * @param memberId
    * @return
    * @throws SQLException
    */
   public List getCheckOutAssets(long memberId) throws SQLException
   {
      List list = new ArrayList();
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_CHECKOUT_ASSETS);
         pstmt.setLong(1, memberId);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            list.add(getMemberHistory(rs));
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
   
   /**
    * Gets asset been checked out but not yet dued based on barcode.
    * 
    * @param barcode
    * @return
    * @throws SQLException
    */
   public MemberHistory getCheckOutAsset(String barcode, long memberId) throws SQLException
   {
      MemberHistory history = null;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(SELECT_CHECKOUT_ASSET_BY_BARCODE);
         pstmt.setString(1, barcode);
         pstmt.setLong(2, memberId);
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            history = getMemberHistory(rs);
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

      return history;
   }    
   
   /**
    * Gets assets been checked out but not yet dued for a member.
    * 
    * @param memberId
    * @return
    * @throws SQLException
    */
   public List getOverDueAssetIds(Date today) throws SQLException
   {
      List ids = new ArrayList();
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try
      {
         pstmt = con.prepareStatement(GET_OVERDUE_ASSETS);
         pstmt.setDate(1, new java.sql.Date(today.getTime()));
         rs = pstmt.executeQuery();

         while(rs.next())
         {
            ids.add(new Integer(rs.getInt(1))); 
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

      return ids;
   }    
   
   public int checkOutAssets(long memberId, List ids, List renewIds, 
      Date dueDate) throws SQLException
   {
      int result = 0;

      PreparedStatement pstmt = null;  
      PreparedStatement renewPstmt = null; 
      try
      {
         if(ids == null || ids.size() == 0)
            return result;

         Date now = new Date();
         pstmt = con.prepareStatement(INSERT_MEMBER_HISTORY);
         if(renewIds != null && renewIds.size() > 0)
            renewPstmt = con.prepareStatement(UPDATE_DUE_DATE);         
         AssetManager assetMgr = new AssetManager(con);
         for(int i = 0; i < ids.size(); i++)
         {
            int assetId = ((Integer)ids.get(i)).intValue();
            if(!renewIds.contains(new Integer(assetId)))
            {
               // insert history
               pstmt.setLong(1, memberId);
               pstmt.setInt(2, assetId);
               pstmt.setTimestamp(3, new Timestamp(now.getTime()));
               pstmt.setDate(4, new java.sql.Date(dueDate.getTime()));
               pstmt.executeUpdate();
            
               // update asset
               assetMgr.updateStatus(assetId, AssetStatus.CHECK_OUT, now);
            }
            else
            {
               // for renewing, only update due date
               renewPstmt.setDate(1, new java.sql.Date(dueDate.getTime()));
               renewPstmt.setLong(2, memberId);
               renewPstmt.setInt(3, assetId);
               renewPstmt.executeUpdate();
            }
            
            result++;
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
         if(renewPstmt != null)
            renewPstmt.close();         
      }

      return result;
   }   
   
   public int checkInAssets(long memberId, List ids, Date checkinDate) throws SQLException
   {
      int result = 0;

      PreparedStatement pstmt = null;      
      try
      {
         if(ids == null || ids.size() == 0)
            return result;

         pstmt = con.prepareStatement(UPDATE_CHECKIN_DATE);
         AssetManager assetMgr = new AssetManager(con);
         for(int i = 0; i < ids.size(); i++)
         {
            int assetId = ((Integer)ids.get(i)).intValue();
            // update history
            pstmt.setTimestamp(1, new Timestamp(checkinDate.getTime()));
            pstmt.setInt(2, 1); // to indicate being checked in
            pstmt.setLong(3, memberId);
            pstmt.setInt(4, assetId);
            pstmt.setInt(5, 0);  // for an asset, only one of historical data can be in checkout status
            pstmt.executeUpdate();
            
            // update asset status
            assetMgr.updateStatus(assetId, AssetStatus.AVAILABLE, checkinDate);
            
            result++;
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
   
   private MemberHistory getMemberHistory(ResultSet rs) throws SQLException
   {
      MemberHistory history = new MemberHistory();
      history.getAsset().setAssetId(rs.getInt(ASSET_ID));
      history.getAsset().setName(rs.getString(AssetManager.NAME));
      history.getAsset().setAssetCode(rs.getString(AssetManager.ASSET_CODE));
      history.getAsset().setAssetBarcode(rs.getString(AssetManager.ASSET_BARCODE));
      history.getAsset().setAssetTypeId(rs.getInt(AssetManager.ASSET_TYPE_ID));
      history.setCheckoutDate(rs.getTimestamp(CHECKOUT_DATE));
      history.setCheckinDate(rs.getTimestamp(CHECKIN_DATE));
      history.setDueDate(rs.getDate(DUE_DATE));
      history.setCheckedIn(rs.getInt(IS_CHECKED_IN) > 0 ? true : false);
      
      return history;
   }      
}
