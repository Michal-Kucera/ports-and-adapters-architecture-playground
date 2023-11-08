package com.michalkucera.application.port.output.whitelabel.merchantaccountprofile

import com.michalkucera.domain.whitelabel.entity.MerchantAccountProfile

interface FetchMerchantAccountProfilesOutputPort {

    fun fetchMerchantAccountProfiles(): List<MerchantAccountProfile>
}
