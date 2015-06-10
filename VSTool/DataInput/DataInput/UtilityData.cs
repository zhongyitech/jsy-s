using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using System.Data.OleDb;

namespace DataInput
{
    public static class UtilityData
    {
        public static DataSet ExcelToDS(string Path)
        {
            try
            {
                string strConn = "Provider=Microsoft.Jet.OLEDB.4.0;" + "Data Source=" + Path + ";" + "Extended Properties=Excel 8.0;";
                if (System.IO.Path.GetExtension(Path) == ".xlsx")
                    strConn = "Provider=Microsoft.Ace.OleDb.12.0;" + "data source=" + Path + ";Extended Properties='Excel 12.0; HDR=Yes; IMEX=0'";
                OleDbConnection conn = new OleDbConnection(strConn);
                conn.Open();
                var tabDt = conn.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);
                OleDbDataAdapter myCommand = null;
                OleDbCommand oleCommmand = new OleDbCommand() { Connection = conn, CommandType = CommandType.Text };
                DataSet ds = new DataSet();
                myCommand = new OleDbDataAdapter(oleCommmand);
                foreach (DataRow dr in tabDt.Rows)
                {
                    var tableName = dr.Field<string>("TABLE_NAME");
                    if (tableName.IndexOf('_') > 0) break;
                    try
                    {                      
                        oleCommmand.CommandText = string.Format("select * from [{0}];", tableName);
                        myCommand.Fill(ds, tableName.Substring(0, tableName.Length - 1));
                    }
                    catch (Exception ex)
                    {
                        System.Windows.Forms.MessageBox.Show(String.Format("导入{0} 工作表时出错了，可能是表格式不正确(不能有合并的单元格，第一行必须为字段名）", tableName.Substring(0, tableName.Length - 1)));
                    }
                }
                oleCommmand.Dispose();
                conn.Close();
                return ds;
            }

            catch (System.InvalidOperationException ioex)
            {
                System.Windows.Forms.MessageBox.Show("电脑上没有安装Office软件，不能进行导入工作！\n" + ioex.Message, "打开Excel文件");
                throw;
            }
            catch (OleDbException oleEx)
            {
                System.Windows.Forms.MessageBox.Show("出错了：\n" + oleEx.Message, "打开Excel文件");
                throw;
            }
            catch (Exception ex)
            {
                throw new Exception("打开Excel文件出错了！" + ex.Message);
            }
            finally
            {
            }
        }

        public static DataTable QueryTable(string sql, string Path)
        {
            string strConn = "Provider=Microsoft.Jet.OLEDB.4.0;" + "Data Source=" + Path + ";" + "Extended Properties=Excel 8.0;";
            if (System.IO.Path.GetExtension(Path) == ".xlsx")
                strConn = "Provider=Microsoft.Ace.OleDb.12.0;" + "data source=" + Path + ";Extended Properties='Excel 12.0; HDR=Yes; IMEX=0'";
            OleDbConnection conn = new OleDbConnection(strConn);
            try
            {
                conn.Open();
                OleDbDataAdapter myCommand = null;
                myCommand = new OleDbDataAdapter(sql, conn);
                var dt = new DataTable();
                myCommand.Fill(dt);
                myCommand.Dispose();
                conn.Close();
                return dt;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
                conn.Close();
            }
        }
    }
}
