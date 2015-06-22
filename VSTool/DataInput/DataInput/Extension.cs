using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using System.Data.OleDb;

namespace DataInput
{
    public static class Extension
    {
        public static DataSet ExcelToDS(string Path)
        {
            string strConn = "Provider=Microsoft.Jet.OLEDB.4.0;" + "Data Source=" + Path + ";" + "Extended Properties=Excel 8.0;";
            OleDbConnection conn = new OleDbConnection(strConn);
            conn.Open();
            string strExcel = "";
            OleDbDataAdapter myCommand = null;
            DataSet ds = null;
            strExcel = "select * from [sheet1$]";
            myCommand = new OleDbDataAdapter(strExcel, strConn);
            ds = new DataSet();
            myCommand.Fill(ds, "table1");
            return ds;
        }

        /// <summary>
        /// 将中文逗号替换为英文半角的
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public static string ReplaceDo(this string str)
        {
            if (str == null) return "";
            return str.Replace('，', ',');
        }
        public static string ToJson(this object obj)
        {

            if (obj == null) return null;
            return Newtonsoft.Json.JsonConvert.SerializeObject(obj);
        }
    }


}
