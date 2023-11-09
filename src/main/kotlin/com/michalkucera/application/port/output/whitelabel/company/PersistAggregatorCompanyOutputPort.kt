package com.michalkucera.application.port.output.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.AggregatorCompany

interface PersistAggregatorCompanyOutputPort {
    fun persistAggregatorCompany(aggregatorCompany: AggregatorCompany)
}
