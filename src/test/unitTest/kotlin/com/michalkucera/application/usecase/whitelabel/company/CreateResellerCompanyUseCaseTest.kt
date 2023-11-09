package com.michalkucera.application.usecase.whitelabel.company

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasRootCause
import assertk.assertions.isEqualTo
import com.michalkucera.application.port.input.whitelabel.company.CreateResellerCompanyInputPort
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelByIdStrategy.WhiteLabelNotFoundException
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.usecase.whitelabel.company.CreateResellerCompanyUseCase.Command.MultiWhiteLabelUserCommand
import com.michalkucera.application.usecase.whitelabel.company.CreateResellerCompanyUseCase.Command.SingleWhiteLabelUserCommand
import com.michalkucera.domain.whitelabel.entity.ResellerCompany
import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import com.michalkucera.framework.adapter.output.provider.fixed.FixedLocalDateTimeProviderOutputAdapter
import com.michalkucera.framework.adapter.output.provider.fixed.FixedUuidProviderOutputAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelByIdPersistenceAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryResellerCompanyDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryResellerCompanyPersistenceAdapter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class CreateResellerCompanyUseCaseTest {
    private val whiteLabelDatabase = InMemoryWhiteLabelDatabase()
    private val resellerCompanyDatabase = InMemoryResellerCompanyDatabase()
    private val underTest: CreateResellerCompanyUseCase

    init {
        val fetchWhiteLabelOutputPort = InMemoryWhiteLabelByIdPersistenceAdapter(whiteLabelDatabase)
        underTest = CreateResellerCompanyInputPort(
            FetchWhiteLabelStrategyFactory(fetchWhiteLabelOutputPort, fetchWhiteLabelOutputPort),
            InMemoryResellerCompanyPersistenceAdapter(resellerCompanyDatabase),
            FixedUuidProviderOutputAdapter(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
            FixedLocalDateTimeProviderOutputAdapter(LocalDateTime.of(2021, 7, 21, 10, 30, 5))
        )
    }

    @Nested
    inner class SingleWhiteLabelUser {
        @Test
        fun `create reseller company for white label`() {
            whiteLabelDatabase += aWhiteLabel()

            createResellerCompanyForSingleWhiteLabelUser()

            assertThat(resellerCompanyDatabase).contains(aResellerCompany())
        }

        @Test
        fun `return reseller company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()

            val resellerCompany = createResellerCompanyForSingleWhiteLabelUser()

            assertThat(resellerCompany).isEqualTo(aResellerCompany())
        }

        private fun createResellerCompanyForSingleWhiteLabelUser() = underTest.createResellerCompany(
            SingleWhiteLabelUserCommand(companyName = "Reseller company")
        )
    }

    @Nested
    inner class MultiWhiteLabelUser {
        @Test
        fun `create reseller company for white label`() {
            whiteLabelDatabase += aWhiteLabel()

            createResellerCompanyForMultiWhiteLabelUser()

            assertThat(resellerCompanyDatabase).contains(aResellerCompany())
        }

        @Test
        fun `show error when white label does not exist`() {
            assertFailure { createResellerCompanyForMultiWhiteLabelUser() }.hasRootCause(WhiteLabelNotFoundException)
        }

        @Test
        fun `return reseller company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()

            val resellerCompany = createResellerCompanyForMultiWhiteLabelUser()

            assertThat(resellerCompany).isEqualTo(aResellerCompany())
        }

        private fun createResellerCompanyForMultiWhiteLabelUser() = underTest.createResellerCompany(
            MultiWhiteLabelUserCommand(
                companyName = "Reseller company",
                whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf"))
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
}
