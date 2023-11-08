package com.michalkucera.framework.adapter.output.whitelabel.company

import com.michalkucera.application.port.output.whitelabel.company.PersistMerchantCompanyOutputPort
import com.michalkucera.domain.whitelabel.entity.MerchantCompany

class InMemoryMerchantCompanyPersistenceAdapter(
    private val database: InMemoryMerchantCompanyDatabase
) : PersistMerchantCompanyOutputPort {

    override fun persistMerchantCompany(merchantCompany: MerchantCompany) {
        database.removeIf { it.merchantCompanyId == merchantCompany.merchantCompanyId }
        database += merchantCompany
    }
}
