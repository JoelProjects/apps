namespace TCA.YearbookApp
{
   partial class MainForm
   {
      /// <summary>
      /// Required designer variable.
      /// </summary>
      private System.ComponentModel.IContainer components = null;

      /// <summary>
      /// Clean up any resources being used.
      /// </summary>
      /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
      protected override void Dispose(bool disposing)
      {
         if (disposing && (components != null))
         {
            components.Dispose();
         }
         base.Dispose(disposing);
      }

      #region Windows Form Designer generated code

      /// <summary>
      /// Required method for Designer support - do not modify
      /// the contents of this method with the code editor.
      /// </summary>
      private void InitializeComponent()
      {
         this.components = new System.ComponentModel.Container();
         System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
         this.imageList = new System.Windows.Forms.ImageList(this.components);
         this.yearLabel = new System.Windows.Forms.Label();
         this.toolTip = new System.Windows.Forms.ToolTip(this.components);
         this.helpPic = new System.Windows.Forms.PictureBox();
         this.titleImage = new System.Windows.Forms.PictureBox();
         this.logoPic = new System.Windows.Forms.PictureBox();
         this.splitContainer1 = new System.Windows.Forms.SplitContainer();
         this.treeView = new System.Windows.Forms.TreeView();
         this.counterLbl = new System.Windows.Forms.Label();
         this.descriptionLbl = new System.Windows.Forms.Label();
         this.previousBtn = new System.Windows.Forms.Button();
         this.nextBtn = new System.Windows.Forms.Button();
         this.nameLabel = new System.Windows.Forms.Label();
         this.fileLink = new System.Windows.Forms.LinkLabel();
         this.pictureBox = new System.Windows.Forms.PictureBox();
         this.axWindowsMediaPlayer1 = new AxWMPLib.AxWindowsMediaPlayer();
         ((System.ComponentModel.ISupportInitialize)(this.helpPic)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.titleImage)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.logoPic)).BeginInit();
         this.splitContainer1.Panel1.SuspendLayout();
         this.splitContainer1.Panel2.SuspendLayout();
         this.splitContainer1.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.pictureBox)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.axWindowsMediaPlayer1)).BeginInit();
         this.SuspendLayout();
         // 
         // imageList
         // 
         this.imageList.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList.ImageStream")));
         this.imageList.TransparentColor = System.Drawing.Color.Transparent;
         this.imageList.Images.SetKeyName(0, "closing.gif");
         this.imageList.Images.SetKeyName(1, "opening.gif");
         // 
         // yearLabel
         // 
         this.yearLabel.AutoSize = true;
         this.yearLabel.Font = new System.Drawing.Font("Times New Roman", 24F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.yearLabel.ForeColor = System.Drawing.SystemColors.HotTrack;
         this.yearLabel.Location = new System.Drawing.Point(44, 12);
         this.yearLabel.Name = "yearLabel";
         this.yearLabel.Size = new System.Drawing.Size(0, 36);
         this.yearLabel.TabIndex = 4;
         // 
         // helpPic
         // 
         this.helpPic.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
         this.helpPic.Cursor = System.Windows.Forms.Cursors.Help;
         this.helpPic.Image = global::TCA.YearbookApp.Properties.Resources.help;
         this.helpPic.Location = new System.Drawing.Point(14, 622);
         this.helpPic.Name = "helpPic";
         this.helpPic.Size = new System.Drawing.Size(44, 41);
         this.helpPic.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
         this.helpPic.TabIndex = 3;
         this.helpPic.TabStop = false;
         this.toolTip.SetToolTip(this.helpPic, "輔助");
         this.helpPic.Click += new System.EventHandler(this.helpPic_Click);
         // 
         // titleImage
         // 
         this.titleImage.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
         this.titleImage.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
         this.titleImage.Cursor = System.Windows.Forms.Cursors.Hand;
         this.titleImage.Location = new System.Drawing.Point(254, 9);
         this.titleImage.Name = "titleImage";
         this.titleImage.Size = new System.Drawing.Size(531, 55);
         this.titleImage.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
         this.titleImage.TabIndex = 2;
         this.titleImage.TabStop = false;
         this.toolTip.SetToolTip(this.titleImage, "到學校網站");
         this.titleImage.Click += new System.EventHandler(this.titlePic_Click);
         // 
         // logoPic
         // 
         this.logoPic.Cursor = System.Windows.Forms.Cursors.Hand;
         this.logoPic.Image = ((System.Drawing.Image)(resources.GetObject("logoPic.Image")));
         this.logoPic.Location = new System.Drawing.Point(143, 7);
         this.logoPic.Name = "logoPic";
         this.logoPic.Size = new System.Drawing.Size(85, 55);
         this.logoPic.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
         this.logoPic.TabIndex = 1;
         this.logoPic.TabStop = false;
         this.toolTip.SetToolTip(this.logoPic, "到慈濟網站");
         this.logoPic.Click += new System.EventHandler(this.logoPic_Click);
         // 
         // splitContainer1
         // 
         this.splitContainer1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                     | System.Windows.Forms.AnchorStyles.Left)
                     | System.Windows.Forms.AnchorStyles.Right)));
         this.splitContainer1.BackgroundImage = global::TCA.YearbookApp.Properties.Resources.main_back;
         this.splitContainer1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.splitContainer1.Location = new System.Drawing.Point(2, 67);
         this.splitContainer1.Name = "splitContainer1";
         // 
         // splitContainer1.Panel1
         // 
         this.splitContainer1.Panel1.BackColor = System.Drawing.Color.Transparent;
         this.splitContainer1.Panel1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
         this.splitContainer1.Panel1.Controls.Add(this.treeView);
         this.splitContainer1.Panel1.RightToLeft = System.Windows.Forms.RightToLeft.No;
         // 
         // splitContainer1.Panel2
         // 
         this.splitContainer1.Panel2.BackColor = System.Drawing.Color.Transparent;
         this.splitContainer1.Panel2.BackgroundImage = global::TCA.YearbookApp.Properties.Resources.main_back;
         this.splitContainer1.Panel2.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
         this.splitContainer1.Panel2.Controls.Add(this.counterLbl);
         this.splitContainer1.Panel2.Controls.Add(this.descriptionLbl);
         this.splitContainer1.Panel2.Controls.Add(this.previousBtn);
         this.splitContainer1.Panel2.Controls.Add(this.nextBtn);
         this.splitContainer1.Panel2.Controls.Add(this.nameLabel);
         this.splitContainer1.Panel2.Controls.Add(this.fileLink);
         this.splitContainer1.Panel2.Controls.Add(this.pictureBox);
         this.splitContainer1.Panel2.ForeColor = System.Drawing.Color.Blue;
         this.splitContainer1.Panel2.RightToLeft = System.Windows.Forms.RightToLeft.No;
         this.splitContainer1.Size = new System.Drawing.Size(843, 549);
         this.splitContainer1.SplitterDistance = 255;
         this.splitContainer1.TabIndex = 0;
         // 
         // treeView
         // 
         this.treeView.BackColor = System.Drawing.SystemColors.InactiveBorder;
         this.treeView.BorderStyle = System.Windows.Forms.BorderStyle.None;
         this.treeView.Cursor = System.Windows.Forms.Cursors.Hand;
         this.treeView.Dock = System.Windows.Forms.DockStyle.Fill;
         this.treeView.Font = new System.Drawing.Font("DFKai-SB", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.treeView.ForeColor = System.Drawing.Color.MidnightBlue;
         this.treeView.HideSelection = false;
         this.treeView.HotTracking = true;
         this.treeView.ImageIndex = 0;
         this.treeView.ImageList = this.imageList;
         this.treeView.ItemHeight = 30;
         this.treeView.LineColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(102)))), ((int)(((byte)(204)))));
         this.treeView.Location = new System.Drawing.Point(0, 0);
         this.treeView.Margin = new System.Windows.Forms.Padding(5);
         this.treeView.Name = "treeView";
         this.treeView.SelectedImageIndex = 1;
         this.treeView.ShowNodeToolTips = true;
         this.treeView.Size = new System.Drawing.Size(251, 545);
         this.treeView.TabIndex = 0;
         this.treeView.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.treeView_AfterSelect);
         // 
         // counterLbl
         // 
         this.counterLbl.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
         this.counterLbl.AutoSize = true;
         this.counterLbl.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.counterLbl.ForeColor = System.Drawing.Color.Black;
         this.counterLbl.Location = new System.Drawing.Point(272, 511);
         this.counterLbl.Name = "counterLbl";
         this.counterLbl.Size = new System.Drawing.Size(0, 13);
         this.counterLbl.TabIndex = 9;
         this.counterLbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
         // 
         // descriptionLbl
         // 
         this.descriptionLbl.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                     | System.Windows.Forms.AnchorStyles.Right)));
         this.descriptionLbl.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.descriptionLbl.ForeColor = System.Drawing.Color.Navy;
         this.descriptionLbl.Location = new System.Drawing.Point(90, 412);
         this.descriptionLbl.Name = "descriptionLbl";
         this.descriptionLbl.Size = new System.Drawing.Size(400, 60);
         this.descriptionLbl.TabIndex = 8;
         this.descriptionLbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
         this.descriptionLbl.UseCompatibleTextRendering = true;
         // 
         // previousBtn
         // 
         this.previousBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
         this.previousBtn.BackColor = System.Drawing.Color.Transparent;
         this.previousBtn.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
         this.previousBtn.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.previousBtn.ForeColor = System.Drawing.Color.DarkBlue;
         this.previousBtn.Location = new System.Drawing.Point(218, 506);
         this.previousBtn.Name = "previousBtn";
         this.previousBtn.Size = new System.Drawing.Size(37, 23);
         this.previousBtn.TabIndex = 7;
         this.previousBtn.Text = "<<";
         this.toolTip.SetToolTip(this.previousBtn, "前一頁");
         this.previousBtn.UseVisualStyleBackColor = false;
         this.previousBtn.Click += new System.EventHandler(this.previousBtn_Click);
         // 
         // nextBtn
         // 
         this.nextBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.nextBtn.BackColor = System.Drawing.Color.Transparent;
         this.nextBtn.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
         this.nextBtn.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.nextBtn.ForeColor = System.Drawing.Color.DarkBlue;
         this.nextBtn.Location = new System.Drawing.Point(320, 506);
         this.nextBtn.Name = "nextBtn";
         this.nextBtn.Size = new System.Drawing.Size(37, 23);
         this.nextBtn.TabIndex = 6;
         this.nextBtn.Text = ">>";
         this.toolTip.SetToolTip(this.nextBtn, "下一頁");
         this.nextBtn.UseVisualStyleBackColor = false;
         this.nextBtn.Click += new System.EventHandler(this.nextBtn_Click);
         // 
         // nameLabel
         // 
         this.nameLabel.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
         this.nameLabel.AutoSize = true;
         this.nameLabel.Font = new System.Drawing.Font("PMingLiU", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(136)));
         this.nameLabel.ForeColor = System.Drawing.Color.Yellow;
         this.nameLabel.Location = new System.Drawing.Point(244, 471);
         this.nameLabel.Name = "nameLabel";
         this.nameLabel.Size = new System.Drawing.Size(0, 16);
         this.nameLabel.TabIndex = 2;
         // 
         // fileLink
         // 
         this.fileLink.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
         this.fileLink.AutoSize = true;
         this.fileLink.Font = new System.Drawing.Font("PMingLiU", 12F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(136)));
         this.fileLink.Location = new System.Drawing.Point(246, 501);
         this.fileLink.Name = "fileLink";
         this.fileLink.Size = new System.Drawing.Size(0, 16);
         this.fileLink.TabIndex = 1;
         this.fileLink.UseWaitCursor = true;
         // 
         // pictureBox
         // 
         this.pictureBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                     | System.Windows.Forms.AnchorStyles.Left)
                     | System.Windows.Forms.AnchorStyles.Right)));
         this.pictureBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.pictureBox.Cursor = System.Windows.Forms.Cursors.Hand;
         this.pictureBox.ErrorImage = null;
         this.pictureBox.Location = new System.Drawing.Point(46, 42);
         this.pictureBox.Name = "pictureBox";
         this.pictureBox.Size = new System.Drawing.Size(491, 342);
         this.pictureBox.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
         this.pictureBox.TabIndex = 0;
         this.pictureBox.TabStop = false;
         this.toolTip.SetToolTip(this.pictureBox, "請按下滑鼠鍵打開文件");
         this.pictureBox.Click += new System.EventHandler(this.pictureBox_Click);
         // 
         // axWindowsMediaPlayer1
         // 
         this.axWindowsMediaPlayer1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.axWindowsMediaPlayer1.Enabled = true;
         this.axWindowsMediaPlayer1.Location = new System.Drawing.Point(675, 625);
         this.axWindowsMediaPlayer1.Name = "axWindowsMediaPlayer1";
         this.axWindowsMediaPlayer1.OcxState = ((System.Windows.Forms.AxHost.State)(resources.GetObject("axWindowsMediaPlayer1.OcxState")));
         this.axWindowsMediaPlayer1.Size = new System.Drawing.Size(160, 35);
         this.axWindowsMediaPlayer1.TabIndex = 5;
         this.axWindowsMediaPlayer1.UseWaitCursor = true;
         // 
         // MainForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
         this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
         this.ClientSize = new System.Drawing.Size(845, 667);
         this.Controls.Add(this.axWindowsMediaPlayer1);
         this.Controls.Add(this.yearLabel);
         this.Controls.Add(this.helpPic);
         this.Controls.Add(this.titleImage);
         this.Controls.Add(this.logoPic);
         this.Controls.Add(this.splitContainer1);
         this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
         this.Name = "MainForm";
         this.Padding = new System.Windows.Forms.Padding(2);
         this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
         this.Load += new System.EventHandler(this.Form1_Load);
         ((System.ComponentModel.ISupportInitialize)(this.helpPic)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.titleImage)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.logoPic)).EndInit();
         this.splitContainer1.Panel1.ResumeLayout(false);
         this.splitContainer1.Panel2.ResumeLayout(false);
         this.splitContainer1.Panel2.PerformLayout();
         this.splitContainer1.ResumeLayout(false);
         ((System.ComponentModel.ISupportInitialize)(this.pictureBox)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.axWindowsMediaPlayer1)).EndInit();
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.SplitContainer splitContainer1;
      private System.Windows.Forms.PictureBox logoPic;
      private System.Windows.Forms.PictureBox titleImage;
      private System.Windows.Forms.PictureBox helpPic;
      private System.Windows.Forms.Label yearLabel;
      private System.Windows.Forms.PictureBox pictureBox;
      private System.Windows.Forms.Label nameLabel;
      private System.Windows.Forms.LinkLabel fileLink;
      private AxWMPLib.AxWindowsMediaPlayer axWindowsMediaPlayer1;
      private System.Windows.Forms.Button nextBtn;
      private System.Windows.Forms.Button previousBtn;
      private System.Windows.Forms.Label descriptionLbl;
      private System.Windows.Forms.ToolTip toolTip;
      private System.Windows.Forms.TreeView treeView;
      private System.Windows.Forms.ImageList imageList;
      private System.Windows.Forms.Label counterLbl;
   }
}

