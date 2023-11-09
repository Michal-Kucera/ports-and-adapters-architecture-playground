package com.michalkucera.application.usecase.whitelabel.company

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasRootCause
import assertk.assertions.isEqualTo
import com.michalkucera.application.port.input.whitelabel.company.CreateAggregatorCompanyInputPort
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelByIdStrategy.WhiteLabelNotFoundException
import com.michalkucera.application.port.input.whitelabel.fetchstrategy.FetchWhiteLabelStrategyFactory
import com.michalkucera.application.usecase.whitelabel.company.CreateAggregatorCompanyUseCase.Command.MultiWhiteLabelUserCommand
import com.michalkucera.application.usecase.whitelabel.company.CreateAggregatorCompanyUseCase.Command.SingleWhiteLabelUserCommand
import com.michalkucera.domain.whitelabel.entity.AggregatorCompany
import com.michalkucera.domain.whitelabel.entity.WhiteLabel
import com.michalkucera.domain.whitelabel.valueobject.AggregatorCompanyId
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import com.michalkucera.framework.adapter.output.provider.fixed.FixedLocalDateTimeProviderOutputAdapter
import com.michalkucera.framework.adapter.output.provider.fixed.FixedUuidProviderOutputAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelByIdPersistenceAdapter
import com.michalkucera.framework.adapter.output.whitelabel.InMemoryWhiteLabelDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryAggregatorCompanyDatabase
import com.michalkucera.framework.adapter.output.whitelabel.company.InMemoryAggregatorCompanyPersistenceAdapter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class CreateAggregatorCompanyUseCaseTest {
    private val whiteLabelDatabase = InMemoryWhiteLabelDatabase()
    private val aggregatorCompanyDatabase = InMemoryAggregatorCompanyDatabase()
    private val underTest: CreateAggregatorCompanyUseCase

    init {
        val fetchWhiteLabelOutputPort = InMemoryWhiteLabelByIdPersistenceAdapter(whiteLabelDatabase)
        underTest = CreateAggregatorCompanyInputPort(
            FetchWhiteLabelStrategyFactory(fetchWhiteLabelOutputPort, fetchWhiteLabelOutputPort),
            InMemoryAggregatorCompanyPersistenceAdapter(aggregatorCompanyDatabase),
            FixedUuidProviderOutputAdapter(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
            FixedLocalDateTimeProviderOutputAdapter(LocalDateTime.of(2021, 7, 21, 10, 30, 5))
        )
    }

    @Nested
    inner class SingleWhiteLabelUser {
        @Test
        fun `create aggregator company for white label`() {
            whiteLabelDatabase += aWhiteLabel()

            createAggregatorCompanyForSingleWhiteLabelUser()

            assertThat(aggregatorCompanyDatabase).contains(anAggregatorCompany())
        }

        @Test
        fun `return aggregator company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()

            val aggregatorCompany = createAggregatorCompanyForSingleWhiteLabelUser()

            assertThat(aggregatorCompany).isEqualTo(anAggregatorCompany())
        }

        private fun createAggregatorCompanyForSingleWhiteLabelUser() = underTest.createAggregatorCompany(
            SingleWhiteLabelUserCommand(companyName = "Aggregator company")
        )
    }

    @Nested
    inner class MultiWhiteLabelUser {
        @Test
        fun `create aggregator company for white label`() {
            whiteLabelDatabase += aWhiteLabel()

            createAggregatorCompanyForMultiWhiteLabelUser()

            assertThat(aggregatorCompanyDatabase).contains(anAggregatorCompany())
        }

        @Test
        fun `show error when white label does not exist`() {
            assertFailure { createAggregatorCompanyForMultiWhiteLabelUser() }.hasRootCause(WhiteLabelNotFoundException)
        }

        @Test
        fun `return aggregator company data after creation`() {
            whiteLabelDatabase += aWhiteLabel()

            val aggregatorCompany = createAggregatorCompanyForMultiWhiteLabelUser()

            assertThat(aggregatorCompany).isEqualTo(anAggregatorCompany())
        }

        private fun createAggregatorCompanyForMultiWhiteLabelUser() = underTest.createAggregatorCompany(
            MultiWhiteLabelUserCommand(
                companyName = "Aggregator company",
                whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf"))
            )
        )
    }

    private fun aWhiteLabel() = WhiteLabel(
        whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf"))
    )

    private fun anAggregatorCompany() = AggregatorCompany(
        aggregatorCompanyId = AggregatorCompanyId(UUID.fromString("6595b89b-0568-49e5-b330-aa8536b34290")),
        companyName = "Aggregator company",
        whiteLabelId = WhiteLabelId(UUID.fromString("8098387f-db73-4866-997c-18642c970daf")),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            lastModificationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            deletionDateTime = null
        )
    )
}
