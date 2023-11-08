package com.michalkucera.application.port.input.whitelabel.merchantaccountprofile

import com.michalkucera.application.port.output.whitelabel.merchantaccountprofile.FetchMerchantAccountProfilesOutputPort
import com.michalkucera.application.usecase.whitelabel.merchantaccountprofile.GetMerchantAccountProfilesUseCase

class GetMerchantAccountProfilesInputPort(
    private val fetchMerchantAccountProfilesOutputPort: FetchMerchantAccountProfilesOutputPort
) : GetMerchantAccountProfilesUseCase {

    override fun getMerchantAccountProfiles() = fetchMerchantAccountProfilesOutputPort.fetchMerchantAccountProfiles()
}
