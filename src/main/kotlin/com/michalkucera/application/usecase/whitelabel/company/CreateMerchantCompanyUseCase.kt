package com.michalkucera.application.usecase.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.MerchantCompany
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

interface CreateMerchantCompanyUseCase {

    fun createMerchantCompany(command: Command): MerchantCompany

    open class Command(
        open val companyName: String,
        open val resellerCompanyId: ResellerCompanyId
    ) : CreateCompanyCommand() {

        data class SingleWhiteLabelUserCommand(
            override val companyName: String,
            override val resellerCompanyId: ResellerCompanyId
        ) : Command(companyName, resellerCompanyId)

        data class MultiWhiteLabelUserCommand(
            override val companyName: String,
            val whiteLabelId: WhiteLabelId,
            override val resellerCompanyId: ResellerCompanyId
        ) : Command(companyName, resellerCompanyId)
    }

    object ResellerCompanyNotFoundException : Exception("Reseller company not found")
}
