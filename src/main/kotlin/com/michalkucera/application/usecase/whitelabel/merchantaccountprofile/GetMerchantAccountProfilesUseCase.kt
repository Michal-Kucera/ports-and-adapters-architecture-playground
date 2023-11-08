package com.michalkucera.application.usecase.whitelabel.merchantaccountprofile

import com.michalkucera.domain.whitelabel.entity.MerchantAccountProfile

interface GetMerchantAccountProfilesUseCase {

    fun getMerchantAccountProfiles(): List<MerchantAccountProfile>
}
