package com.jsy.util;

import grails.converters.JSON;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;

import java.util.Iterator;

/**
 * Created by oswaldl on 2/7/15.
 *
 *
 */
public class OrderProperty {
    JSONObject property;
    String key;
    String value;
    public OrderProperty(JSONObject property) throws Exception{
        this.property=property;

        Iterator keysIterator = property.keys();
        if(property.size()!=1)
            throw new Exception("invalid search property, not 1 row define!");


        while (keysIterator.hasNext()){
            String key = (String)keysIterator.next();
            if(!"operate".equals(key)){
                this.key = key;
                Object valueTemp = property.get(key);

                if(valueTemp instanceof String){
                    value = (String)valueTemp;
                }
            }


        }
    }



    public static void main(String args[]) throws Exception{
        String cir = "{\"name\":\"ab\"}";
        OrderProperty p =new OrderProperty((JSONObject)JSON.parse(cir));
        System.out.println(p.key+","+p.value);

    }

}
