using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using TCA.YearbookApp.Xml.Model;
using System.IO;
using System.Resources;

namespace TCA.YearbookApp
{
   public partial class MainForm : Form
   {
      private Yearbook yearbook;
      private IList<Item> currentItems = null;
      private int currentIdx = 0;

      public MainForm(Yearbook yearbook)
      {
         this.yearbook = yearbook;

         InitializeComponent();
         this.Text = yearbook.AppTitle;
         this.yearLabel.Text = yearbook.Year;
         this.titleImage.ImageLocation = yearbook.TitleImage.ToString();

         IList<Section> sections = yearbook.Sections;
         int parentIdx = 0;
         foreach(Section section in sections)
         {
            TreeNode node = new TreeNode(section.Name);
            node.ToolTipText = section.Tooltip;
            IList<Section> subsections = section.Sections;
            if(subsections.Count > 0)
            {
               int childIdx = 0;
               foreach(Section subsection in subsections)
               {
                  TreeNode subnode = new TreeNode(subsection.Name);
                  subnode.ToolTipText = subsection.Tooltip;
                  subnode.Tag = new int[] { parentIdx, childIdx, -1};
                  node.Nodes.Add(subnode);
                  getSubTree(subnode, subsection);
                  childIdx++;
               }
            }
            node.Tag = new int[] { parentIdx, -1, -1};
            this.treeView.Nodes.Add(node);

            parentIdx++;
         }
      }

      private void getSubTree(TreeNode node, Section section)
      {
         IList<Section> subsections = section.Sections;
         if(subsections.Count > 0)
         {
            int childIdx = 0;
            foreach(Section subsection in subsections)
            {
               TreeNode subnode = new TreeNode(subsection.Name);
               subnode.ToolTipText = subsection.Tooltip;
               int[] tag = (int[])node.Tag;
               subnode.Tag = new int[] { tag[0], tag[1], childIdx };
               node.Nodes.Add(subnode);
               childIdx++;
            }
         }
      }

      private void Form1_Load(object sender, EventArgs e)
      {

      }

      private void helpPic_Click(object sender, EventArgs e)
      {
         HelpForm form = new HelpForm();
         form.Show();
      }

      private void titlePic_Click(object sender, EventArgs e)
      {
         if(yearbook.Url != null)
            Utility.RunApp(yearbook.Url.ToString());
      }

      private void logoPic_Click(object sender, EventArgs e)
      {
         Utility.RunApp("http://www.tzuchi.org");
      }

      private void treeView_AfterSelect(object sender, TreeViewEventArgs e)
      {
         // get selected section
         TreeNode node = ((TreeView)sender).SelectedNode;
         int[] tag = (int[])node.Tag;
         int level1 = tag[0];
         int level2 = tag[1];
         int level3 = tag[2];
         Section section = yearbook.Sections[level1];
         if(level2 >= 0 && level3 >= 0)
            currentItems = section.Sections[level2].Sections[level3].Items;
         else if(level2 >= 0)
            currentItems = section.Sections[level2].Items;
         else
            currentItems = section.Items;

         // display selected item
         currentIdx = 0;
         showItem();
      }

      private void previousBtn_Click(object sender, EventArgs e)
      {
         if(currentIdx > 0)
         {
            currentIdx--;
            showItem();
         }
      }

      private void nextBtn_Click(object sender, EventArgs e)
      {
         if(currentIdx < currentItems.Count - 1)
         {
            currentIdx++;
            showItem();
         }
      }

      private void showItem()
      {
         if(currentItems.Count > 0)
         {
            string description = currentItems[currentIdx].Description;
            if(Utility.isEmpty(description))
               description = "";
            this.descriptionLbl.Text = description;

            FileInfo file = currentItems[currentIdx].File;
            if(file != null)
            {
               string extension = file.Extension.ToLower();
               if(extension.Equals(".gif") || 
                  extension.Equals(".jpg") || 
                  extension.Equals(".png"))
                  this.pictureBox.ImageLocation = file.ToString();
               else 
                  this.pictureBox.Image = Properties.Resources.play;
            }

            this.counterLbl.Text = (currentIdx + 1) + "/" + currentItems.Count;
         }
         else
         {
            this.descriptionLbl.Text = "";
            this.pictureBox.ImageLocation = null;
            this.counterLbl.Text = "";
         }
      }

      private void pictureBox_Click(object sender, EventArgs e)
      {
         FileInfo file = currentItems[currentIdx].File;
         if(file != null)
            Utility.RunApp(file.FullName);
      }
   }
}