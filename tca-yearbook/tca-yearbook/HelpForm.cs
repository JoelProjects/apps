using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace TCA.YearbookApp
{
   public partial class HelpForm : Form
   {
      public HelpForm()
      {
         InitializeComponent();
      }

      private void chineseLinkLabel_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
      {
         Utility.RunApp("Chinese IME.pdf");
      }

      private void pdfLinkLabel_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
      {
         Utility.RunApp("http://www.adobe.com/products/acrobat/readstep2.html");
      }

      private void qtLinkLabel_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
      {
         Utility.RunApp("http://www.apple.com/quicktime/download/");
      }
   }
}