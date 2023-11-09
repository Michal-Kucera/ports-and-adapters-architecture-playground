package com.michalkucera.framework.adapter.output.whitelabel.company

import com.michalkucera.application.port.output.whitelabel.company.FetchResellerCompanyOutputPort
import com.michalkucera.application.port.output.whitelabel.company.PersistResellerCompanyOutputPort
import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId

class InMemoryResellerCompanyPersistenceAdapter(
    private val database: InMemoryResellerCompanyDatabase
) : PersistResellerCompanyOutputPort,
    FetchResellerCompanyOutputPort {
    override fun persistResellerCompany(resellerCompany: ResellerCompany) {
        database.removeIf { it.resellerCompanyId == resellerCompany.resellerCompanyId }
        database += resellerCompany
    }

    override fun fetchResellerCompanyById(resellerCompanyId: ResellerCompanyId) =
        database.firstOrNull { it.resellerCompanyId == resellerCompanyId }
}
