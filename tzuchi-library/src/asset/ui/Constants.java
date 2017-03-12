package asset.ui;

import java.awt.Color;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public interface Constants
{
   /**
    * Defines location of data source configuration.
    */
   public static String LIB_CONFIG = "lib.config";
   
   public static final int APP_WIDTH = 800;
   public static final int APP_HEIGHT = 600;   
   
   // checkout period
   public static final int CHECKOUT_PERIOD = 14;  // 14 days
   // incremental unit
   public static final int CHECKOUT_PERIOD_INT = 7;  // 7 days   
   
   // colors
   public static Color DEFAULT_BACKGROUND = Color.WHITE;
   public static Color DEFAULT_FOREGROUND = Color.BLACK;   
   
   public static Color COLOR_AVAILABLE = Color.BLACK;
   public static Color COLOR_CHECK_OUT = Color.LIGHT_GRAY;
   public static Color COLOR_OVER_DUE = Color.RED;
   public static Color COLOR_MISSING = Color.RED; 
   
   // search options
   public static String OPTION_MEMBER = "Member";
   public static String OPTION_ASSET = "Asset";  
   
   // description of search options
   public static String OPTION_MEMBER_DESC = "e.g., English or Chinese name, home or cell phone";
   public static String OPTION_ASSET_DESC = "e.g., asset barcode";
}
