package com.michalkucera.application.port.input.whitelabel.company

import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.port.output.provider.LocalDateTimeProviderOutputPort
import com.michalkucera.application.port.output.provider.UuidProviderOutputPort
import com.michalkucera.application.port.output.whitelabel.company.FetchResellerCompanyOutputPort
import com.michalkucera.application.port.output.whitelabel.company.PersistMerchantCompanyOutputPort
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase.Command
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase.ResellerCompanyNotFoundException
import com.michalkucera.domain.whitelabel.entity.MerchantCompany
import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId

class CreateMerchantCompanyInputPort(
    private val fetchWhiteLabelStrategyFactory: FetchWhiteLabelStrategyFactory,
    private val fetchResellerCompanyOutputPort: FetchResellerCompanyOutputPort,
    private val persistMerchantCompanyOutputPort: PersistMerchantCompanyOutputPort,
    private val uuidProviderOutputPort: UuidProviderOutputPort,
    private val localDateTimeProviderOutputPort: LocalDateTimeProviderOutputPort
) : CreateMerchantCompanyUseCase {
    override fun createMerchantCompany(command: Command): MerchantCompany {
        val whiteLabel = fetchWhiteLabelWithResellerCompany(command)
        val merchantCompany = whiteLabel.addNewMerchantCompanyToExistingResellerCompany(
            resellerCompanyId = command.resellerCompanyId,
            merchantCompanyId = MerchantCompanyId(uuidProviderOutputPort.getRandomUuid()),
            companyName = command.companyName,
            creationDateTime = localDateTimeProviderOutputPort.getCurrentLocalDateTime()
        )
        persistMerchantCompanyOutputPort.persistMerchantCompany(merchantCompany)
        return merchantCompany
    }

    private fun fetchWhiteLabelWithResellerCompany(command: Command): WhiteLabel {
        val whiteLabel = fetchWhiteLabelStrategyFactory.getStrategy(command).fetchWhiteLabel()
        val resellerCompany = fetchResellerCompany(command.resellerCompanyId)
        whiteLabel.addExistingResellerCompany(resellerCompany)
        return whiteLabel
    }

    private fun fetchResellerCompany(resellerCompanyId: ResellerCompanyId) =
        fetchResellerCompanyOutputPort.fetchResellerCompanyById(resellerCompanyId)
            ?: throw ResellerCompanyNotFoundException
}
