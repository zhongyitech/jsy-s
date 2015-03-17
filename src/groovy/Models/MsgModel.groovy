package Models

/**
 * Created by libosong on 2015/3/17.
 */
class MsgModel {
    private String rest_status;
    private String rest_result;

    private static MsgModel m_instance = null;

    public MsgModel(String status, String result){
        rest_status = status;
        rest_result = result;
    }
    /**
     * 生成对象
     * @return
     */
    public static MsgModel getInstance(String status, String result){
        if(null == m_instance){
            m_instance = new MsgModel(status,result);
        }
        return m_instance;
    }

    /**
     * 生成成功体
     * @param msg 成功体
     * @return
     */
    public static  MsgModel getSuccessMsg(String msg){
        return getInstance("200",msg);
    }

    /**
     * 生成失败体
     * @param msg 失败体
     * @return
     */
    public static MsgModel getErrorMsg(String msg){
        return getInstance("500",msg);
    }

    public boolean isSuccess(){
        if(rest_status.equals("200")){
            return true
        }else{
            return false;
        }
    }
}
