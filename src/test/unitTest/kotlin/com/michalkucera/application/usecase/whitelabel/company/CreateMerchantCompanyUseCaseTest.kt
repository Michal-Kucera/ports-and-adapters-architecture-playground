package com.michalkucera.application.usecase.whitelabel.company

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasRootCause
import assertk.assertions.isEqualTo
import com.michalkucera.application.port.input.whitelabel.company.CreateMerchantCompanyInputPort
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelByIdStrategy.WhiteLabelNotFoundException
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase.Command.MultiWhiteLabelUserCommand
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase.ResellerCompanyNotFoundException
import com.michalkucera.application.usecase.whitelabel.company.CreateMerchantCompanyUseCase.Command.SingleWhiteLabelUserCommand
import com.michalkucera.domain.whitelabel.entity.MerchantCompany
import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryMerchantCompanyDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryMerchantCompanyPersistenceAdapter
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryResellerCompanyDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryResellerCompanyPersistenceAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelByIdPersistenceAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelDatabase
import com.michalkucera.framework.adapter.output.provider.fixed.FixedLocalDateTimeProviderOutputAdapter
import com.michalkucera.framework.adapter.output.provider.fixed.FixedUuidProviderOutputAdapter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class CreateMerchantCompanyUseCaseTest {

    private val whiteLabelDatabase = InMemoryWhiteLabelDatabase()
    private val resellerCompanyDatabase = InMemoryResellerCompanyDatabase()
    private val merchantCompanyDatabase = InMemoryMerchantCompanyDatabase()
    private val underTest: CreateMerchantCompanyUseCase

    init {
        val fetchWhiteLabelOutputPort = InMemoryWhiteLabelByIdPersistenceAdapter(whiteLabelDatabase)
        underTest = CreateMerchantCompanyInputPort(
            FetchWhiteLabelStrategyFactory(fetchWhiteLabelOutputPort, fetchWhiteLabelOutputPort),
            InMemoryResellerCompanyPersistenceAdapter(resellerCompanyDatabase),
            InMemoryMerchantCompanyPersistenceAdapter(merchantCompanyDatabase),
            FixedUuidProviderOutputAdapter(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
            FixedLocalDateTimeProviderOutputAdapter(LocalDateTime.of(2021, 7, 21, 10, 30, 5))
        )
    }

    @Nested
    inner class SingleWhiteLabelUser {

        @Test
        fun `create merchant company for default white label and reseller company`() {
            whiteLabelDatabase += aWhiteLabel()
            resellerCompanyDatabase += aResellerCompany()

            createMerchantCompanyForSingleWhiteLabelUser()

            assertThat(resellerCompanyDatabase).contains(aResellerCompany())
        }

        @Test
        fun `show error when reseller company does not exist`() {
            whiteLabelDatabase += aWhiteLabel()

            assertFailure { createMerchantCompanyForSingleWhiteLabelUser() }
                .hasRootCause(ResellerCompanyNotFoundException)
        }

        private fun createMerchantCompanyForSingleWhiteLabelUser() = underTest.createMerchantCompany(
            SingleWhiteLabelUserCommand(
                companyName = "Merchant company",
                resellerCompanyId = ResellerCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290"))
            )
        )

        @Test
        fun `return merchant company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()
            resellerCompanyDatabase += aResellerCompany()

            val merchantCompany = createMerchantCompanyForSingleWhiteLabelUser()

            assertThat(merchantCompany).isEqualTo(aMerchantCompany())
        }
    }

    @Nested
    inner class MultiWhiteLabelUser {

        @Test
        fun `create merchant company for white label and reseller company`() {
            whiteLabelDatabase += aWhiteLabel()
            resellerCompanyDatabase += aResellerCompany()

            createMerchantCompanyForMultiWhiteLabelUser()

            assertThat(resellerCompanyDatabase).contains(aResellerCompany())
        }

        @Test
        fun `show error when white label does not exist`() {
            resellerCompanyDatabase += aResellerCompany()

            assertFailure { createMerchantCompanyForMultiWhiteLabelUser() }.hasRootCause(WhiteLabelNotFoundException)
        }

        @Test
        fun `show error when reseller company does not exist`() {
            whiteLabelDatabase += aWhiteLabel()

            assertFailure { createMerchantCompanyForMultiWhiteLabelUser() }
                .hasRootCause(ResellerCompanyNotFoundException)
        }

        @Test
        fun `return merchant company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()
            resellerCompanyDatabase += aResellerCompany()

            val merchantCompany = createMerchantCompanyForMultiWhiteLabelUser()

            assertThat(merchantCompany).isEqualTo(aMerchantCompany())
        }

        private fun createMerchantCompanyForMultiWhiteLabelUser() = underTest.createMerchantCompany(
            MultiWhiteLabelUserCommand(
                companyName = "Merchant company",
                whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf")),
                resellerCompanyId = ResellerCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290"))
            )
        )
    }

    private fun aWhiteLabel() = WhiteLabel(
        whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf"))
    )

    private fun aResellerCompany() = ResellerCompany(
        resellerCompanyId = ResellerCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
        companyName = "Reseller company",
        whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf")),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            lastModificationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            deletionDateTime = null
        )
    )

    private fun aMerchantCompany() = MerchantCompany(
        merchantCompanyId = MerchantCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
        companyName = "Merchant company",
        whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf")),
        resellerCompanyId = ResellerCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            lastModificationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            deletionDateTime = null
        )
    )
}
