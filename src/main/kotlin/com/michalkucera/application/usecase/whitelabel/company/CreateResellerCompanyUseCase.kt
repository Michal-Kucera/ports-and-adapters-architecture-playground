package com.michalkucera.application.usecase.whitelabel.company

import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

interface CreateResellerCompanyUseCase {

    fun createResellerCompany(command: Command): ResellerCompany

    sealed class Command(open val companyName: String) : CreateCompanyCommand() {

        data class SingleWhiteLabelUserCommand(override val companyName: String) : Command(companyName)

        data class MultiWhiteLabelUserCommand(
            override val companyName: String,
            val whiteLabelId: WhiteLabelId
        ) : Command(companyName)
    }
}
