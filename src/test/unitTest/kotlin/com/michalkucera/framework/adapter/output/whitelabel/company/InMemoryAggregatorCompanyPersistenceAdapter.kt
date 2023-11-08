package com.michalkucera.framework.adapter.output.whitelabel.company

import com.michalkucera.application.port.output.whitelabel.company.PersistAggregatorCompanyOutputPort
import com.michalkucera.domain.whitelabel.entity.AggregatorCompany

class InMemoryAggregatorCompanyPersistenceAdapter(
    private val database: InMemoryAggregatorCompanyDatabase
) : PersistAggregatorCompanyOutputPort {

    override fun persistAggregatorCompany(aggregatorCompany: AggregatorCompany) {
        database.removeIf { it.aggregatorCompanyId == aggregatorCompany.aggregatorCompanyId }
        database += aggregatorCompany
    }
}
