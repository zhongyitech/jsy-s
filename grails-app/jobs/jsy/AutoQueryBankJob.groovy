package jsy

import com.jsy.bankServices.BankProxyService
import com.jsy.project.ProjectResourceService

/**
 * ����ҵ����ص��Զ���������
 * 1,�������е�ת�˲�ѯ�ӿ�
 *    ��Payment(�Ҹ�����)��ȡ�Ѿ�֧��,����������ˮ���Զ���ƾ֤�ŵļ�¼,Ȼ��������еĽӿ�(4005)��ѯ�Ƿ��Ѿ�����,��������ؼ�¼��״̬
 *    a.�Ҹ���״̬����Ϊ�Ѹ�
 *    b.���(�������/ҵ�����)�ķ���ʱ�������
 * Created by lioa on 2015/5/14.
 */
class AutoQueryBankJob {
    static triggers = {
        cron name: 'bankQuery', cronExpression: "0/10 * * * * ?"
        //TODO:����ʵ����Ҫ������������ü��ʱ��
    }

    def execute() {
        /*  �������е�4004���׽ӿڣ���ѯ���ʻ��Ĵ�������������ݴ���������öҸ��������ɶҸ�����������ݵ�״̬ */
        try {
            new BankProxyService().TransferQueryTask()
        } catch (Exception ex) {
            println("ִ���Զ��������!")
        }
        //todo:����ִ��ƾ֤���ɴ���
        /*   ��������ˮ���еĻ�ȡ���ݣ����չ�������ƾ֤������������ˮ�Ĵ������  */

    }
}
