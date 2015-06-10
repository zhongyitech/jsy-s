using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Windows.Forms;
namespace DataInput
{
    static class RESTapi
    {
        static readonly string _apiHost = "http://localhost:8080/jsy";
        static HttpWebRequest createWebRequest(string api, string method = "GET")
        {
            var webRequest = (HttpWebRequest)WebRequest.Create(_apiHost + api);
            webRequest.Accept = "application/json";
            webRequest.Headers.Add("ContentType", "application/json;charset=UTF-8");
            webRequest.Headers.Add("Authorization", "123123123");
            webRequest.Method = method;
            return webRequest;
        }
        public static JObject Get(string apiName, string param)
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
            return (JObject)JsonConvert.DeserializeObject(res);
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
    }
}