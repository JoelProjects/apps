/* $Id: Messages.java,v 1.3 2008/04/07 05:29:22 joelchou Exp $ */
package store.utils;

import members.utils.Utility;

public interface Messages
{
   public static final String SUPPORTED_DATE_FORMATS = "可使用的日期格式為: " + 
      Utility.DEFAULT_DATE_PATTERN + ", " + Utility.US_DATE_PATTERN;
}
