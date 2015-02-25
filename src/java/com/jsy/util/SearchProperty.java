package com.jsy.util;

import grails.converters.JSON;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.codehaus.groovy.grails.web.json.JSONArray;
import java.util.Iterator;

/**
 * Created by oswaldl on 2/7/15.
 *
 *
 */
public class SearchProperty {
    JSONObject property;
    String key;
    Object value;
    int gapValue1;
    int gapValue2;
    String operate;
    public SearchProperty(JSONObject property) throws Exception{
        this.property=property;

        Iterator keysIterator = property.keys();
        if(property.size()!=2)
            throw new Exception("invalid search property, not 2 row define!");

        this.operate=(String)property.get("operate");

        while (keysIterator.hasNext()){
            String key = (String)keysIterator.next();
            if(!"operate".equals(key)){
                this.key = key;
                Object valueTemp = property.get(key);

                if(valueTemp instanceof String){
                    value = (String)valueTemp;
                }else if(valueTemp instanceof JSONArray){
                    JSONArray array = (JSONArray) valueTemp;
                    if(array.size()!= 2)
                        throw new Exception("gap array is not 2.");

                    gapValue1 = (Integer)array.get(0);
                    gapValue2 = (Integer)array.get(1);
                }else{
                    value = valueTemp;
                }
            }


        }
    }


    /**
     {"name":"张三","operate":"eq"},
     {"age":[3,4],"operate":"between"}

     to

     between("balance", 500, 1000)
     eq("branch", "London")
     like("holderFirstName", "Fred%")

     * @return
     */
    public String toString(){
        try {
            if ("between".equals(this.property.get("operate"))) {
                return "between(\""+this.key+"\","+this.gapValue1+","+this.gapValue2+")";
            }else if("like".equals(this.property.get("operate"))){
                return "like(\""+this.key+"\",\"%"+this.value+"%\")";
            }else if("eq".equals(this.property.get("operate"))){
                return "eq(\""+this.key+"\",\""+this.value+"\")";
            }else{
                return "";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public static void main(String args[]) throws Exception{
        String cir = "{\"name\":\"ab\",\"operate\":\"like\"}";
        SearchProperty p =new SearchProperty((JSONObject)JSON.parse(cir));
        System.out.println(p);

    }

}
