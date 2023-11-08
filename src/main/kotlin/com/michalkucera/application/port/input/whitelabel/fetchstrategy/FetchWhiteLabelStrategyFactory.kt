package com.michalkucera.application.port.input.whitelabel.fetchstrategy

import com.michalkucera.application.port.output.whitelabel.FetchDefaultWhiteLabelOutputPort
import com.michalkucera.application.port.output.whitelabel.FetchWhiteLabelByIdOutputPort
import com.michalkucera.application.usecase.whitelabel.company.CreateAggregatorCompanyUseCase
import com.michalkucera.application.usecase.whitelabel.company.CreateCompanyCommand
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase
import com.michalkucera.application.usecase.whitelabel.company.CreateResellerCompanyUseCase
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId

class FetchWhiteLabelStrategyFactory(
    private val fetchWhiteLabelByIdOutputPort: FetchWhiteLabelByIdOutputPort,
    fetchDefaultWhiteLabelOutputPort: FetchDefaultWhiteLabelOutputPort
) {

    private val fetchDefaultWhiteLabelStrategy = FetchDefaultWhiteLabelStrategy(fetchDefaultWhiteLabelOutputPort)


    fun <T : CreateCompanyCommand> getStrategy(command: T) = when (command) {
        is CreateMerchantCompanyUseCase.Command.SingleWhiteLabelUserCommand,
        is CreateAggregatorCompanyUseCase.Command.SingleWhiteLabelUserCommand,
        is CreateResellerCompanyUseCase.Command.SingleWhiteLabelUserCommand -> fetchDefaultWhiteLabelStrategy

        is CreateMerchantCompanyUseCase.Command.MultiWhiteLabelUserCommand -> createFetchWhiteLabelByIdStrategy(command.whiteLabelId)
        is CreateAggregatorCompanyUseCase.Command.MultiWhiteLabelUserCommand -> createFetchWhiteLabelByIdStrategy(
            command.whiteLabelId
        )

        is CreateResellerCompanyUseCase.Command.MultiWhiteLabelUserCommand -> createFetchWhiteLabelByIdStrategy(command.whiteLabelId)

        else -> throw UnsupportedOperationException("Command not supported")
    }

    private fun createFetchWhiteLabelByIdStrategy(
        whiteLabelId: WhiteLabelId
    ) = FetchWhiteLabelByIdStrategy(fetchWhiteLabelByIdOutputPort, whiteLabelId)
}
