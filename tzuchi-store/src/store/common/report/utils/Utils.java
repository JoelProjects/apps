/* $Id: Utils.java,v 1.1 2008/02/29 13:01:19 jlin Exp $
 * Created on Feb 22, 2008
 * 
 * $Log: Utils.java,v $
 * Revision 1.1  2008/02/29 13:01:19  jlin
 * Add Report framework
 * Implement ExcelReport and TableModelReport
 *
 */
package store.common.report.utils;

import java.io.File;

/**
 * A collection of utilities
 *
 * @author Jeff Lin
 * @version  $date:$
 */
public class Utils
{
	public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";
    public final static String XLS = "xls";

    
    /*
     * Get the extension of a file.
     */  
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
}
