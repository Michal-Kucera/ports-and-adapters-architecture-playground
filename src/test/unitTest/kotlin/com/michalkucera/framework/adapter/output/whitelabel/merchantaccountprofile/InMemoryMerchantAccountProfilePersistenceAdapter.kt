package com.michalkucera.framework.adapter.output.whitelabel.merchantaccountprofile

import com.michalkucera.application.port.output.whitelabel.merchantaccountprofile.FetchMerchantAccountProfilesOutputPort

class InMemoryMerchantAccountProfilePersistenceAdapter(
    private val database: InMemoryMerchantAccountProfileDatabase
) : FetchMerchantAccountProfilesOutputPort {
    override fun fetchMerchantAccountProfiles() = database.toList()
}
