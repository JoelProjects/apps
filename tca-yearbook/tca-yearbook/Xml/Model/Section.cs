/* $Id: Section.cs,v 1.5 2008/06/06 06:17:57 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace TCA.YearbookApp.Xml.Model
{
   public class Section
   {
      private String name;
      private FileInfo icon;
      private FileInfo file;
      private string tooltip;
      private IList<Item> items = new List<Item>();
      private IList<Section> sections = new List<Section>();

      public string Name
      {
         get
         {
            return name;
         }
         set
         {
            name = value;
         }
      }

      public FileInfo Icon
      {
         get
         {
            return icon;
         }
         set
         {
            icon = value;
         }
      }

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

      public string Tooltip
      {
         get
         {
            return tooltip;
         }
         set
         {
            tooltip = value;
         }
      }

      /// <summary>
      /// Contains a list of Item objects.
      /// </summary>
      public IList<Item> Items
      {
         get
         {
            return items;
         }
      }

      /// <summary>
      /// Contains a list of Section objects.
      /// </summary>
      public IList<Section> Sections
      {
         get
         {
            return sections;
         }
         set
         {
            sections = value;
         }
      }
   }
}
