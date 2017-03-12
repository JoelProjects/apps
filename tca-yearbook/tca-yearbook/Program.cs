/* $Id: Program.cs,v 1.5 2008/06/12 06:15:04 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.IO;
using TCA.YearbookApp.Xml;
using TCA.YearbookApp.Xml.Model;

namespace TCA.YearbookApp
{
   static class Program
   {
      /// <summary>
      /// The main entry point for the application.
      /// </summary>
      [STAThread]
      static void Main()
      {
         try
         {
            XmlReader reader = new XmlReader(new FileInfo("yearbook.xml"));
            Yearbook yearbook = reader.process();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm(yearbook));
         }
         catch(Exception e)
         {
            MessageBox.Show(e.Message, "Yearbook", MessageBoxButtons.OK, MessageBoxIcon.Error);
         }
      }
   }
}