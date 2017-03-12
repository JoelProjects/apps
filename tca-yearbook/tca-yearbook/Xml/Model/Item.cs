/* $Id: Item.cs,v 1.2 2008/06/07 18:26:52 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace TCA.YearbookApp.Xml.Model
{
   public class Item
   {
      private FileInfo file;
      private string description;

      public FileInfo File
      {
         get
         {
            return file;
         }
         set
         {
            file = value;
         }
      }

      public string Description
      {
         get
         {
            return description;
         }
         set
         {
            description = value;
         }
      }
   }
}
