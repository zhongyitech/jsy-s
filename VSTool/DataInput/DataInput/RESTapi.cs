using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;

namespace DataInput
{
    static class RESTapi
    {
        static HttpWebRequest createWebRequest(string api, string method = "GET")
        {
            var url = api;
            var webRequest = (HttpWebRequest)WebRequest.Create(url);
            webRequest.Accept = "application/json";
            webRequest.Headers.Add("ContentType", "application/json;charset=UTF-8");
            webRequest.Headers.Add("Authorization", "123123123");
            webRequest.Method = method;
            return webRequest;
        }
        dynamic Get(Uri url)
        {

        }
    }
}
