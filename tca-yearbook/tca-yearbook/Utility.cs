/* $Id: Utility.cs,v 1.7 2008/07/20 03:31:12 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.Windows.Forms;

namespace TCA.YearbookApp
{
   class Utility
   {
      public static void RunApp(String name)
      {
         Cursor oldCursor = Cursor.Current;
         Cursor.Current = Cursors.WaitCursor;
         try
         {
            Process proc = new Process();
            proc.StartInfo.FileName = name;
            proc.StartInfo.UseShellExecute = true;
            proc.Start();
         }
         catch(Exception e)
         {
            MessageBox.Show(e.Message + ": " + name, "Run Application", MessageBoxButtons.OK, 
               MessageBoxIcon.Error);
         }
         finally
         {
            Cursor.Current = oldCursor;
         }
      }

      public static Boolean isEmpty(String value)
      {
         return (value == null || value.Trim().Length == 0);
      }
   }
}
