package com.michalkucera.application.port.input.whitelabel.company

import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.port.output.provider.LocalDateTimeProviderOutputPort
import com.michalkucera.application.port.output.provider.UuidProviderOutputPort
import com.michalkucera.application.port.output.whitelabel.company.PersistAggregatorCompanyOutputPort
import com.michalkucera.application.usecase.whitelabel.company.CreateAggregatorCompanyUseCase
import com.michalkucera.application.usecase.whitelabel.company.CreateAggregatorCompanyUseCase.Command
import com.michalkucera.domain.whitelabel.entity.AggregatorCompany
import com.michalkucera.domain.whitelabel.valueobject.AggregatorCompanyId

class CreateAggregatorCompanyInputPort(
    private val fetchWhiteLabelStrategyFactory: FetchWhiteLabelStrategyFactory,
    private val persistAggregatorCompanyOutputPort: PersistAggregatorCompanyOutputPort,
    private val uuidProviderOutputPort: UuidProviderOutputPort,
    private val localDateTimeProviderOutputPort: LocalDateTimeProviderOutputPort
) : CreateAggregatorCompanyUseCase {

    override fun createAggregatorCompany(command: Command): AggregatorCompany {
        val whiteLabel = fetchWhiteLabel(command)
        val aggregatorCompany = whiteLabel.addNewAggregatorCompany(
            aggregatorCompanyId = AggregatorCompanyId(uuidProviderOutputPort.getRandomUuid()),
            companyName = command.companyName,
            creationDateTime = localDateTimeProviderOutputPort.getCurrentLocalDateTime()
        )
        persistAggregatorCompanyOutputPort.persistAggregatorCompany(aggregatorCompany)
        return aggregatorCompany
    }

    private fun fetchWhiteLabel(
        command: Command
    ) = fetchWhiteLabelStrategyFactory.getStrategy(command).fetchWhiteLabel()
}
