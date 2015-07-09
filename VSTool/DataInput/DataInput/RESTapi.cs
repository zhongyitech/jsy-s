using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Windows.Forms;
using System.Diagnostics;
using System.Threading;
namespace DataInput
{
    /// <summary>
    /// 对调用系统API的封闭类。
    /// </summary>
    static class RESTapi
    {
        ////static readonly string _apiHost = "http://localhost:8080/jsy";
        static readonly string _apiHost = "http://ilearnapi.azurewebsites.net/api/values";
        static HttpWebRequest _currentWebRequest = null;
        /// <summary>
        /// 获取一个可用的请求对象；
        /// </summary>
        /// <param name="api"></param>
        /// <param name="method"></param>
        /// <returns></returns>
        static HttpWebRequest createWebRequest(string api, string method = "GET")
        {
            var webRequest = (HttpWebRequest)WebRequest.Create(_apiHost + api);
            webRequest.Accept = "application/json";
            webRequest.Headers.Add("ContentType", "application/json;charset=UTF-8");
            webRequest.Headers.Add("Authorization", "123123123");
            webRequest.Method = method;
            webRequest.Timeout = 10 * 1000;
            webRequest.KeepAlive = true;
            return webRequest;
        }
        public static JToken Get(string apiName, string param)
        {
            JObject json = JsonConvert.DeserializeObject(param) as JObject;
            var uri = apiName;
            if (json != null && json.Count > 0)
            {
                uri += "?";
            }
            foreach (var item in json)
            {
                uri += string.Format("{0}={1}&", item.Key, item.Value);
            }
            var web = createWebRequest(apiName, "GET");
            var res = new System.IO.StreamReader(web.GetResponse().GetResponseStream(), Encoding.UTF8) { }.ReadToEnd();
            return (JToken)JsonConvert.DeserializeObject(res);
        }
        public static JObject Post(string apiName, string param, string data)
        {
            JObject json = JsonConvert.DeserializeObject(param) as JObject;
            if (json != null && json.Count > 0)
            {
                apiName += "?";
                foreach (var item in json)
                {
                    apiName += string.Format("{0}={1}&", item.Key, item.Value);
                }
            }
            var WebReq = createWebRequest(apiName, "POST");
            try
            {
                byte[] buffer = System.Text.Encoding.UTF8.GetBytes(data);
                WebReq.ContentType = "application/json; charset=UTF-8";
                System.IO.Stream PostData = WebReq.GetRequestStream();
                //Now we write, and afterwards, we close. Closing is always important!
                PostData.Write(buffer, 0, buffer.Length);
                PostData.Close();
                var res = new System.IO.StreamReader(WebReq.GetResponse().GetResponseStream(), Encoding.UTF8) { }.ReadToEnd();

                return (JObject)JsonConvert.DeserializeObject(res);
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
            }
        }

        public static JToken RequestAPI(string apiName, string param, string data, Method method = Method.get)
        {
            if (string.IsNullOrEmpty(apiName) ||
            string.IsNullOrEmpty(param)) throw new Exception("param 参数格式不合法！");

            if (param != null)
            {
                JObject json = JsonConvert.DeserializeObject(param) as JObject;
                if (json != null && json.Count > 0)
                {
                    apiName += "?";
                    foreach (var item in json)
                    {
                        apiName += string.Format("{0}={1}&", item.Key, item.Value);
                    }
                }
            }
            var WebReq = createWebRequest(apiName, method.ToString());
            try
            {
                WebReq.ContentType = "application/json; charset=UTF-8";
                if (data != null)
                {
                    byte[] buffer = System.Text.Encoding.UTF8.GetBytes(data);
                    System.IO.Stream PostData = WebReq.GetRequestStream();
                    //Now we write, and afterwards, we close. Closing is always important!
                    PostData.Write(buffer, 0, buffer.Length);
                    PostData.Close();
                }
                var res = new System.IO.StreamReader(WebReq.GetResponse().GetResponseStream(), Encoding.UTF8) { }.ReadToEnd();
                return (JToken)JsonConvert.DeserializeObject(res);
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
                //WebReq.Abort();
                WebReq = null;
            }
        }

        public static void Test()
        {
            var st = System.Diagnostics.Stopwatch.StartNew();
            for (int i = 0; i < 400; i++)
            {
                var res = st.Elapsed.ToString() + RequestAPI("test", "{'a':'11'}", null);
                st = Stopwatch.StartNew();
                Console.WriteLine(res);
            }
        }
    }

    enum Method
    {
        get, post, put, delete
    }
}