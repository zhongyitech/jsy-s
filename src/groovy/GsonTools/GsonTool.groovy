package GsonTools

import Models.MsgModel
import Models.ProjectModelPhaseRole

/**
 * Created by libosong on 2015/3/17.
 */

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jsy.project.TSWorkflow
import com.jsy.project.TSWorkflowPhase

import java.lang.reflect.Type;

class GsonTool {
    public static Gson m_instance = null;

    /**
     * 生成解析实例
     * @return
     */
    public static Gson getGsoInstance(){
        if(null == m_instance){
            m_instance = new Gson();
        }
        return m_instance;
    }

    /**
     * json 生成MsgModel
     * @param json    MsgModel json
     * @return
     */
    public static MsgModel getMsgModel(String json){
        MsgModel model = getGsoInstance().fromJson(json,new TypeToken<MsgModel>(){}.getType());
        return model;
    }

    public static String getProjectModelPhaseRolesJson(ProjectModelPhaseRole phaseRole){
        String json = getGsoInstance().toJson(phaseRole);
        return json;
    }
}
