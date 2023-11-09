package com.michalkucera.application.port.output.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId

interface FetchResellerCompanyOutputPort {
    fun fetchResellerCompanyById(resellerCompanyId: ResellerCompanyId): ResellerCompany?
}
