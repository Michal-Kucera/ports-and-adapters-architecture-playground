package com.michalkucera.application.port.output.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.ResellerCompany

interface PersistResellerCompanyOutputPort {
    fun persistResellerCompany(resellerCompany: ResellerCompany)
}
