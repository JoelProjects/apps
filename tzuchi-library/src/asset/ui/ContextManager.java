package asset.ui;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import members.utils.Utility;

import asset.util.DatabaseAccess;

import asset.datamodel.AssetCategory;
import asset.datamodel.AssetCategoryManager;
import asset.datamodel.AssetStatus;
import asset.datamodel.AssetStatusManager;
import asset.datamodel.AssetType;
import asset.datamodel.AssetTypeManager;
import asset.datamodel.MemberType;
import asset.datamodel.MemberTypeManager;

/**
 * @author Cheng-Hung Chou
 * @since 3/13/2005
 */
public class ContextManager
{
   private static DatabaseAccess dbAccess;
   private static DatabaseAccess userDbAccess;
   private static Vector assetCategoryList;
   private static Map assetCategoryMap = new HashMap();  // (ID, name)
   private static Vector assetStatusList;
   private static Map assetStatusMap = new HashMap();  // (ID, AssetStatus) 
   private static Map assetStatusIdMap = new HashMap();  // (status, ID)
   private static Vector assetTypeList;
   private static Map assetTypeMap = new HashMap();
   private static Vector memberTypeList;
   private static Map memberTypeMap = new HashMap();   
   
   private static String splashTitle = null;
   private static String frameTitle = null;   
   private static String imagePrefix = null;
   private static int outWeeks = 2; 
   private static boolean isAuthRequired = false;
   
   public ContextManager() throws SQLException
   {
      Connection con = null;
      try
      {
         // load JDBC driver
         dbAccess = new DatabaseAccess("config", LibraryManager.configFile);
         con = dbAccess.getConnection();
         
         // load settings
         InputStream inFile = new File(LibraryManager.configFile).toURL().openStream();
         Properties prop = new Properties();
         prop.load(inFile);
         if(prop != null)
         {
            splashTitle = prop.getProperty("config.title.splash");
            frameTitle = prop.getProperty("config.title.frame");
            imagePrefix = prop.getProperty("config.image.prefix");
            String value = prop.getProperty("config.out.weeks");
            if(!Utility.isEmpty(value))
               outWeeks = Integer.parseInt(value);
            value = prop.getProperty("config.auth.required");
            if(!Utility.isEmpty(value))
               isAuthRequired = Boolean.parseBoolean(value);
            inFile.close(); 
         }
         else
         {
            throw new Exception("* Can not find property file");
         }         
         
         // get available asset categories
         AssetCategoryManager assetCatMgr = new AssetCategoryManager(con);
         assetCategoryList = assetCatMgr.getAssetCategoryList();
         for(int i = 0; i < assetCategoryList.size(); i++)
         {
            AssetCategory category = (AssetCategory)assetCategoryList.get(i);
            assetCategoryMap.put(new Integer(category.getId()), category);
         }
         
         // get available asset statuses
         AssetStatusManager assetStatusMgr = new AssetStatusManager(con);
         assetStatusList = assetStatusMgr.getAssetStatusList();
         for(int i = 0; i < assetStatusList.size(); i++)
         {
            AssetStatus status = (AssetStatus)assetStatusList.get(i);
            assetStatusMap.put(new Integer(status.getId()), status);
            assetStatusIdMap.put(status.getName(), new Integer(status.getId()));
         }         
      
         // get available asset types
         AssetTypeManager assetTypeMgr = new AssetTypeManager(con);
         assetTypeList = assetTypeMgr.getAssetTypeList(); 
         for(int i = 0; i < assetTypeList.size(); i++)
         {
            AssetType type = (AssetType)assetTypeList.get(i);
            assetTypeMap.put(new Integer(type.getId()), type);
         }        
         
         // get available member types
         MemberTypeManager memberTypeMgr = new MemberTypeManager(con);
         memberTypeList = memberTypeMgr.getMemberTypeList(); 
         for(int i = 0; i < memberTypeList.size(); i++)
         {
            MemberType type = (MemberType)memberTypeList.get(i);
            memberTypeMap.put(new Integer(type.getId()), type);
         }              
      }
      catch(Exception e)
      {
         System.out.println(e);
         throw new SQLException(e.getMessage());
      }
      finally
      {
         try
         {
            if(con != null)
               con.close();
         }
         catch(Exception e){}
      }
   }
   
   public static Vector getAssetCategoryList()
   {
      return assetCategoryList;
   }
   
   public static AssetCategory getAssetCategory(int id)
   {
      return (AssetCategory)assetCategoryMap.get(new Integer(id));
   }
   
   public static Vector getAssetStatusList()
   {
      return assetStatusList;
   }
   
   public static AssetStatus getAssetStatus(int id)
   {
      return (AssetStatus)assetStatusMap.get(new Integer(id));
   }  
   
   public static int getAssetStatusId(String status)
   {
      Integer idObj = ((Integer)assetStatusIdMap.get(status));
      
      if(idObj != null)
         return idObj.intValue();
      else
         return -1;
   }      
   
   public static Vector getAssetTypeList()
   {
      return assetTypeList;
   }
   
   public static AssetType getAssetType(int id)
   {
      return (AssetType)assetTypeMap.get(new Integer(id));
   } 
   
   public static Vector getMemberTypeList()
   {
      return memberTypeList;
   }
   
   public static MemberType getMemberType(int id)
   {
      return (MemberType)memberTypeMap.get(new Integer(id));
   }      
   
   public static Connection getConnection() throws SQLException
   {
      return dbAccess.getConnection();
   }
   
   public static Connection getUserDBConnection() throws SQLException
   {
      return userDbAccess.getConnection();
   }

   public static String getFrameTitle()
   {
      return frameTitle;
   }

   public static String getImagePrefix()
   {
      return imagePrefix;
   }

   public static String getSplashTitle()
   {
      return splashTitle;
   }  
   
   public static int getOutWeeks()
   {
      return outWeeks;
   }   
   
   public static boolean isAuthRequired()
   {
      return isAuthRequired;
   }
}
