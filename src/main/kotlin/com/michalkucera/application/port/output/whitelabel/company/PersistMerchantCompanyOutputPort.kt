package com.michalkucera.application.port.output.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.MerchantCompany

interface PersistMerchantCompanyOutputPort {

    fun persistMerchantCompany(merchantCompany: MerchantCompany)
}
