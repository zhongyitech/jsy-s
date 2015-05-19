package jsy

import com.jsy.bankServices.BankProxyService
import com.jsy.project.ProjectResourceService

/**
 * ����ҵ����ص��Զ���������
 * 1,�������е�ת�˲�ѯ�ӿ�
 *    ��Payment(�Ҹ�����)��ȡ�Ѿ�֧��,����������ˮ���Զ���ƾ֤�ŵļ�¼,Ȼ��������еĽӿ�(4005)��ѯ�Ƿ��Ѿ�����,��������ؼ�¼��״̬
 *    a.�Ҹ���״̬����Ϊ�Ѹ�
 *    b.���(�������/ҵ�����)�ķ���ʱ�������
 *    c.
 * Created by lioa on 2015/5/14.
 */
class AutoQueryBankJob {
//    BankProxyService bankProxyService
    ProjectResourceService projectResourceService
    static triggers = {
        cron name: 'bankQuery', cronExpression: "0/10 * * * * ?"
    }
    def execute() {
        //��ѯ���н�����Ϣ
        try {
           new BankProxyService().TransferQueryTask()
        } catch (Exception ex) {
            println("ִ���Զ��������!")
        }

        //todo:����ִ��ƾ֤���ɴ���

    }
}
