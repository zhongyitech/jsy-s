using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace DataInput
{
    public partial class Form1 : Form
    {

        DataSet _data = null;
        public Form1()
        {
            InitializeComponent();
            tbox_exceFile.Text = @"C:\Users\世忠\Desktop\JSY数据导入模板.xls";
        }
        private void Form1_Load(object sender, EventArgs e)
        {
            if (tbox_exceFile.Text == "") return;
            _data = UtilityData.ExcelToDS(tbox_exceFile.Text.Trim());
            listBox1.Items.Clear();
            foreach (DataTable tb in _data.Tables)
            {
                listBox1.Items.Add(tb.TableName);
            }
            listBox1.SelectedIndex = 0;
        }
        private void btn_select_excelfile_Click(object sender, EventArgs e)
        {
            openFileDialog1.ShowDialog();
            tbox_exceFile.Text = openFileDialog1.FileName;
            // new F_tips().Show();
            _data = UtilityData.ExcelToDS(tbox_exceFile.Text.Trim());
            listBox1.Items.Clear();
            foreach (DataTable tb in _data.Tables)
            {
                listBox1.Items.Add(tb.TableName);
            }
            listBox1.SelectedIndex = 0;
        }
        void _setView(string tableName)
        {
            dataGridView1.DataSource = _data.Tables[tableName];
        }

        private void listBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            _setView((sender as ListBox).SelectedItem.ToString());
        }

        private void button1_Click(object sender, EventArgs e)
        {
            var sql = richTextBox1.Text;
            var dt = UtilityData.QueryTable(sql, tbox_exceFile.Text);
            dataGridView1.DataSource = dt;
        }

        private void button4_Click(object sender, EventArgs e)
        {
            var dt = getTableForName("职位");
            var data = new List<String>();
            foreach (DataRow item in dt.Rows)
            {
                data.Add(Newtonsoft.Json.JsonConvert.SerializeObject(new { name = item["名称"], description = item["描述"] }));
            }
            foreach (var d in data)
            {
                //RESTapi.Post("/api/role", "", d);
            }
        }
        /// <summary>
        /// 处理基金资料
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn_fund_Click(object sender, EventArgs e)
        {
            var dt = getTableForName("基金资料");
            foreach (DataRow dr in dt.Rows)
            {
                var tcfw = dr.Field<string>("收益率范围").ReplaceDo();
                Console.WriteLine("{0}",dr.ItemArray.ToJson());
            }
        }        

        /* 数据源相关方法 */
        DataTable getTableForName(string dtName)
        {
            return _data.Tables.Contains(dtName) ? _data.Tables[dtName] : null;            
        }

        private void button2_Click(object sender, EventArgs e)
        {
            RESTapi.Test();
        }                
        //
    }
}
