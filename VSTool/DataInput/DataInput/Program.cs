﻿using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using System.Windows.Forms;
using Newtonsoft.Json.Linq;

namespace DataInput
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            //JObject json = (JObject)JsonConvert.DeserializeObject("{\"rest_result\":[{\"tzje\":null,\"rtzje\":234.00,\"fundName\":\"合并用的基金\",\"id\":4}],\"rest_status\":\"suc\"}");
            //var s = json.ToString();
            //var ss = RESTapi.Get(new Uri("http://localhost:8080/jsy/api/report/fundTzje"), "{id:1}");
            //ss = RESTapi.Post(new Uri("http://localhost:8080/jsy/api/role"), "{id:1}", "{name:textname}");

            //var locks = ss.ToString();

            //var url = "http://hq.sinajs.cn/list=sh601003,sh601001";
            //var wb = System.Net.HttpWebRequest.Create(url);

            //var sowp = new System.Diagnostics.Stopwatch();
            //sowp.Start();
            //var web = new System.Net.WebClient();
            //for (int i = 0; i < 5; i++)
            //{
            //    Console.WriteLine("{0}", sowp.Elapsed);
            //    //Console.WriteLine("{0}", web.DownloadString(url));
            //    web.DownloadString(url);
            //}

            Application.ThreadException += Application_ThreadException;
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }

        static void Application_ThreadException(object sender, System.Threading.ThreadExceptionEventArgs e)
        {
            MessageBox.Show(e.Exception.Message, "应用程序出错了！");
        }
    }
}
