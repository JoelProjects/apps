/* $Id: Yearbook.cs,v 1.5 2008/06/06 06:17:57 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Collections;

namespace TCA.YearbookApp.Xml.Model
{
   public class Yearbook
   {
      private string appTitle;
      private string year;
      private FileInfo titleImage;
      private Uri url;
      private IList<Section> sections = new List<Section>();

      public string AppTitle
      {
         get
         {
            return appTitle;
         }
         set
         {
            appTitle = value;
         }
      }

      public string Year
      {
         get
         {
            return year;
         }
         set
         {
            year = value;
         }
      }

      public FileInfo TitleImage
      {
         get
         {
            return titleImage;
         }
         set
         {
            titleImage = value;
         }
      }

      public Uri Url
      {
         get
         {
            return url;
         }
         set
         {
            url = value;
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
