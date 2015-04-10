package com.jsy.archives

import com.jsy.bankConfig.BankAccount
import com.jsy.customerObject.Customer
import com.jsy.system.TypeConfig
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class CustomerArchivesResourceService {

    def create(CustomerArchives dto) {
        if (dto.bankAccount.size() > 0) {
            dto.bankAccount.each {
                it.bankName = it.bankOfDeposit
                it.purpose = TypeConfig.findByTypeAndMapValue(7, 4)
            }
        }
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = CustomerArchives.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CustomerArchives.class, id)
        }
        obj
    }

    def readAll() {
        CustomerArchives.findAll()
    }

    def update(CustomerArchives dto) {
        def obj = CustomerArchives.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CustomerArchives.class, dto.id)
        }
        try {
            def baks=obj.bankAccount
            for (int i=0;i<baks.size();i++){
                obj.removeFromBankAccount(baks[i])
            }
        } catch (Exception ex) {
            print(ex)
        }
        dto.bankAccount.each {
            it.bankName=it.bankOfDeposit
            it.purpose = TypeConfig.findByTypeAndMapValue(7, 4)
            //it.save(failOnError: true)
        }
        obj.properties = dto.properties

        if (obj.khh == null) obj.khh = ""
        if (obj.zch == null) obj.zch = ""
        obj.save(failOnError: true)
        obj
    }

    void delete(id) {
        def obj = CustomerArchives.get(id)
        if (obj) {
            obj.delete()
        }
    }
    /**
     *
     * @param cus
     * @return
     */
    def copyCustomer(Customer cus) {
        def customerArchive = new CustomerArchives()
        customerArchive.name = cus.name
        customerArchive.callAddress = cus.callAddress
        customerArchive.country = cus.country
        customerArchive.credentialsAddr = cus.credentialsAddr
        customerArchive.credentialsType = cus.credentialsType
        customerArchive.credentialsNumber = cus.credentialsNumber
        customerArchive.email = cus.email
        customerArchive.fddbr = cus.fddbr
        customerArchive.khh = cus.khh
        customerArchive.phone = cus.phone
        customerArchive.postalcode = cus.postalcode
        customerArchive.remark = cus.remark
        customerArchive.telephone = cus.telephone
        customerArchive.uploadFiles = cus.uploadFiles
        customerArchive.zch = (cus.zch || "")

        def bank = new BankAccount(bankName: cus.khh, bankOfDeposit: cus.khh, accountName: cus.name, account: cus.yhzh)
        bank.purpose = TypeConfig.findByTypeAndMapValue(7, 4)
        customerArchive.bankAccount = [bank]
        customerArchive.save(failOnError: true)
        return cus
    }
}
