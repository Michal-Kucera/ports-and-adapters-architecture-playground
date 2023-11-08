package com.michalkucera.application.port.input.whitelabel.company

import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.port.output.provider.LocalDateTimeProviderOutputPort
import com.michalkucera.application.port.output.whitelabel.company.PersistResellerCompanyOutputPort
import com.michalkucera.application.port.output.provider.UuidProviderOutputPort
import com.michalkucera.application.usecase.whitelabel.company.CreateResellerCompanyUseCase
import com.michalkucera.application.usecase.whitelabel.company.CreateResellerCompanyUseCase.Command
import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId

class CreateResellerCompanyInputPort(
    private val fetchWhiteLabelStrategyFactory: FetchWhiteLabelStrategyFactory,
    private val persistResellerCompanyOutputPort: PersistResellerCompanyOutputPort,
    private val uuidProviderOutputPort: UuidProviderOutputPort,
    private val localDateTimeProviderOutputPort: LocalDateTimeProviderOutputPort
) : CreateResellerCompanyUseCase {

    override fun createResellerCompany(command: Command): ResellerCompany {
        val whiteLabel = fetchWhiteLabel(command)
        val resellerCompany = whiteLabel.addNewResellerCompany(
            resellerCompanyId = ResellerCompanyId(uuidProviderOutputPort.getRandomUuid()),
            companyName = command.companyName,
            creationDateTime = localDateTimeProviderOutputPort.getCurrentLocalDateTime()
        )
        persistResellerCompanyOutputPort.persistResellerCompany(resellerCompany)
        return resellerCompany
    }

    private fun fetchWhiteLabel(
        command: Command
    ) = fetchWhiteLabelStrategyFactory.getStrategy(command).fetchWhiteLabel()
}
