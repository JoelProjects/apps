/* $Id: XmlReader.cs,v 1.9 2008/06/07 19:22:01 joelchou Exp $ */
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;
using TCA.YearbookApp.Xml.Model;

namespace TCA.YearbookApp.Xml
{
   public class XmlReader
   {
      private FileInfo xmlFile;
      private XmlDocument doc;
      private Yearbook yearbook;

      public XmlReader(FileInfo xmlFile)
      {
         this.xmlFile = xmlFile;
         doc = new XmlDocument();
         doc.Load(xmlFile.OpenRead());
      }

      public Yearbook process()
      {
         yearbook = new Yearbook();
         XmlNodeList nodes = doc.GetElementsByTagName(XmlElement.YEARBOOK);
         if(nodes.Count > 0)
         {
            nodes = nodes.Item(0).ChildNodes;
            foreach(XmlNode node in nodes)
            {
               String value = node.InnerText;
               switch(node.Name)
               {
                  case XmlElement.APP_TITLE:
                     yearbook.AppTitle = node.InnerText;
                     break;
                  case XmlElement.YEAR:
                     yearbook.Year = node.InnerText;
                     break;
                  case XmlElement.TITLE_IMAGE:
                     if (!Utility.isEmpty(value))
                        yearbook.TitleImage = new FileInfo(value);
                     break;
                  case XmlElement.URL:
                     if(!Utility.isEmpty(value))
                        yearbook.Url = new Uri(value);
                     break;
                  case XmlElement.SECTIONS:
                     yearbook.Sections = processSections(node);
                     break;
               }
            }

            // process XML file for each section
            IList<Section> sectionList = yearbook.Sections;
            foreach(Section section in sectionList)
            {
               FileInfo file = section.File;
               if(file != null && file.Exists)
                  processSectionFile(section);
            }
         }

         doc = null;

         return yearbook;
      }

      /// <summary>
      /// Processes XML element Sections.
      /// </summary>
      private IList<Section> processSections(XmlNode node)
      {
         IList<Section> sectionList = new List<Section>();
         // a list of section elements
         XmlNodeList nodes = node.ChildNodes;  
         if(nodes.Count > 0)
         {
            foreach(XmlNode sectionNode in nodes)
            {
               // elements under section
               XmlNodeList sectionChildren = sectionNode.ChildNodes;  
               Section section = new Section();
               foreach(XmlNode childNode in sectionChildren)
               {
                  String value = childNode.InnerText;
                  switch(childNode.Name)
                  {
                     case XmlElement.NAME:
                        section.Name = value;
                        break;
                     case XmlElement.ICON:
                        if(!Utility.isEmpty(value))
                           section.Icon = new FileInfo(value);
                        break;
                     case XmlElement.FILE:
                        if (!Utility.isEmpty(value))
                           section.File = new FileInfo(value);
                        break;
                     case XmlElement.TOOLTIP:
                        section.Tooltip = value;
                        break;
                  }
               }

               sectionList.Add(section);
            }
         }

         return sectionList;
      }

      /// <summary>
      /// Processes XML file for each section.
      /// </summary>
      private void processSectionFile(Section section)
      {
         XmlDocument sectionDoc = new XmlDocument();
         sectionDoc.Load(section.File.OpenRead());

         // items
         XmlNodeList nodes = sectionDoc.GetElementsByTagName(XmlElement.ITEMS);
         if(nodes.Count > 0)
         {
            // a list of item elements
            nodes = nodes.Item(0).ChildNodes; 
            if(nodes.Count > 0)
            {
               foreach(XmlNode node in nodes)
               {
                  Item item = new Item();
                  // children of item
                  XmlNodeList itemChildren = node.ChildNodes;
                  foreach(XmlNode childNode in itemChildren)
                  {
                     String value = childNode.InnerText;
                     switch(childNode.Name)
                     {
                        case XmlElement.FILE:
                           if(!Utility.isEmpty(value))
                              item.File = new FileInfo(value);
                           break;
                        case XmlElement.DESCRIPTION:
                           if(!Utility.isEmpty(value))
                              item.Description = value;                          
                           break;
                     }
                  }

                  section.Items.Add(item);
               }
            }

            // subsections
            nodes = sectionDoc.GetElementsByTagName(XmlElement.SECTIONS);
            if(nodes.Count > 0)
            {
               // get a list of section elements
               section.Sections = processSections(nodes.Item(0));
               // process XML file for each section
               IList<Section> subsectionList = section.Sections;
               foreach(Section subsection in subsectionList)
               {
                  FileInfo file = subsection.File;
                  if(file != null && file.Exists)
                     processSectionFile(subsection);
               }
            }
         }

         sectionDoc = null;
      }
   }
}
