package GsonTools

import Models.MsgModel

/**
 * Created by libosong on 2015/3/17.
 */

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jsy.project.TSWorkflow
import com.jsy.project.TSWorkflowPhase;

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

    /**
     * msgModel 生成 json
     * @param msgModel
     * @return
     */
    public static String getMsgModelJson(MsgModel msgModel){
        return getGsoInstance().toJson(msgModel);
    }

    /**
     * TSWorkflow list 生成 json
     * @param modelList
     * @return
     */
    public static String getTSWrolflowJson(List<TSWorkflow> modelList){
        return getGsoInstance().toJson(modelList);
    }

    /**
     * TSWorkflowPhase list 生成 json
     * @param modelList
     * @return
     */
    public static String getTSWorkflowPhaseJson(List<TSWorkflowPhase> modelList){
        return getGsoInstance().toJson(modelList);
    }
}
