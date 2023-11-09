package com.michalkucera.domain.whitelabel.entity

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.michalkucera.domain.core.entity.Acquirer
import com.michalkucera.domain.core.entity.Provider
import com.michalkucera.domain.core.valueobject.AcquirerId
import com.michalkucera.domain.core.valueobject.ProviderId
import com.michalkucera.domain.whitelabel.valueobject.AggregatorCompanyId
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.MerchantAccountProfileId
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class WhiteLabelTest {
    @Nested
    inner class ResellerCompany {
        @Test
        fun `create a new reseller company`() {
            val whiteLabel = aWhiteLabel()

            val resellerCompany = whiteLabel.addNewResellerCompany(
                resellerCompanyId = aResellerCompanyId(),
                companyName = "PayXpert reseller",
                creationDateTime = LocalDateTime.of(2021, 4, 14, 10, 30)
            )

            assertThat(resellerCompany).isEqualTo(aResellerCompany())
            assertThat(whiteLabel.getResellerCompanies()).containsOnly(aResellerCompany())
        }

        @Test
        fun `shows error when adding existing reseller company to white label that is not the owner of the company`() {
            val whiteLabel = aWhiteLabel()

            assertFailure {
                whiteLabel.addExistingResellerCompany(
                    aResellerCompany(
                        WhiteLabelId(UUID.fromString("872736e6-c4e7-4feb-846d-292a0deeaec2"))
                    )
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(
                    "Reseller company with ID: [${aResellerCompanyId()}] does not belong to a white label with ID: [${aWhiteLabelId()}]"
                )
        }

        @Test
        fun `add existing reseller company to white label`() {
            val whiteLabel = aWhiteLabel()

            whiteLabel.addExistingResellerCompany(aResellerCompany())

            assertThat(whiteLabel.getResellerCompanies()).containsOnly(aResellerCompany())
        }

        @Nested
        inner class MerchantCompany {
            @Test
            fun `add a new merchant company to reseller company`() {
                val whiteLabel = aWhiteLabel()
                whiteLabel.addExistingResellerCompany(aResellerCompany())

                val merchantCompany = whiteLabel.addNewMerchantCompanyToExistingResellerCompany(
                    aResellerCompanyId(),
                    aMerchantCompanyId(),
                    "PayXpert merchant",
                    LocalDateTime.of(2021, 5, 12, 10, 30)
                )

                assertThat(merchantCompany).isEqualTo(aMerchantCompany())
                assertThat(whiteLabel.getResellerCompanies().first().getMerchantCompanies()).containsOnly(
                    aMerchantCompany()
                )
            }

            @Test
            fun `show error when adding a new merchant company to reseller company but reseller company not found in white label`() {
                val whiteLabel = aWhiteLabel()

                assertFailure {
                    whiteLabel.addNewMerchantCompanyToExistingResellerCompany(
                        aResellerCompanyId(),
                        aMerchantCompanyId(),
                        "PayXpert merchant",
                        LocalDateTime.of(2021, 5, 12, 10, 30)
                    )
                }.isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Reseller company with ID: [${aResellerCompanyId()}] not found in white label")
            }
        }
    }

    @Nested
    inner class AggregatorCompany {
        @Test
        fun `create a new aggregator company`() {
            val whiteLabel = aWhiteLabel()

            val aggregatorCompany = whiteLabel.addNewAggregatorCompany(
                aggregatorCompanyId = anAggregatorCompanyId(),
                companyName = "PayXpert aggregator",
                creationDateTime = LocalDateTime.of(2021, 4, 14, 12, 30)
            )

            assertThat(aggregatorCompany).isEqualTo(anAggregatorCompany())
            assertThat(whiteLabel.getAggregatorCompanies()).containsOnly(anAggregatorCompany())
        }
    }

    @Nested
    inner class MerchantAccountProfile {
        @Test
        fun `add a new merchant account profile to white label`() {
            val whiteLabel = aWhiteLabel()

            val merchantAccountProfile = whiteLabel.addNewMerchantAccountProfile(
                merchantAccountProfileId = aMerchantAccountProfileId(),
                merchantAccountProfileName = "PayXpert merchant account profile",
                acquirerId = null,
                providerId = null,
                creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45)
            )

            assertThat(merchantAccountProfile).isEqualTo(aMerchantAccountProfile(acquirerId = null, providerId = null))
            assertThat(whiteLabel.getMerchantAccountProfiles()).containsOnly(
                aMerchantAccountProfile(
                    acquirerId = null,
                    providerId = null
                )
            )
        }

        @Test
        fun `add a new merchant account profile with acquirer to white label`() {
            val whiteLabel = aWhiteLabel()
            whiteLabel.addExistingAcquirer(anAcquirer())

            val merchantAccountProfile = whiteLabel.addNewMerchantAccountProfile(
                merchantAccountProfileId = aMerchantAccountProfileId(),
                merchantAccountProfileName = "PayXpert merchant account profile",
                acquirerId = anAcquirerId(),
                providerId = null,
                creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45)
            )

            assertThat(merchantAccountProfile).isEqualTo(aMerchantAccountProfile(providerId = null))
            assertThat(whiteLabel.getMerchantAccountProfiles()).containsOnly(aMerchantAccountProfile(providerId = null))
        }

        @Test
        fun `show error when creating a new merchant account profile with acquirer but acquirer not found in white label`() {
            val whiteLabel = aWhiteLabel()

            assertFailure {
                whiteLabel.addNewMerchantAccountProfile(
                    merchantAccountProfileId = aMerchantAccountProfileId(),
                    merchantAccountProfileName = "PayXpert merchant account profile",
                    acquirerId = anAcquirerId(),
                    providerId = null,
                    creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45)
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Acquirer with ID: [${anAcquirerId()}] not found in white label")
        }

        @Test
        fun `add a new merchant account profile with provider to white label`() {
            val whiteLabel = aWhiteLabel()
            whiteLabel.addExistingProvider(aProvider())

            val merchantAccountProfile = whiteLabel.addNewMerchantAccountProfile(
                merchantAccountProfileId = aMerchantAccountProfileId(),
                merchantAccountProfileName = "PayXpert merchant account profile",
                acquirerId = null,
                providerId = aProviderId(),
                creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45)
            )

            assertThat(merchantAccountProfile).isEqualTo(aMerchantAccountProfile(acquirerId = null))
            assertThat(whiteLabel.getMerchantAccountProfiles()).containsOnly(aMerchantAccountProfile(acquirerId = null))
        }

        @Test
        fun `show error when creating a new merchant account profile with provider but provider not found in white label`() {
            val whiteLabel = aWhiteLabel()

            assertFailure {
                whiteLabel.addNewMerchantAccountProfile(
                    merchantAccountProfileId = aMerchantAccountProfileId(),
                    merchantAccountProfileName = "PayXpert merchant account profile",
                    acquirerId = null,
                    providerId = aProviderId(),
                    creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45)
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Provider with ID: [${aProviderId()}] not found in white label")
        }
    }

    @Nested
    inner class Acquirer {
        @Test
        fun `add existing acquirer to white label`() {
            val whiteLabel = aWhiteLabel()

            whiteLabel.addExistingAcquirer(anAcquirer())

            assertThat(whiteLabel.getAcquirers()).containsOnly(anAcquirer())
        }
    }

    @Nested
    inner class Provider {
        @Test
        fun `add existing provider to white label`() {
            val whiteLabel = aWhiteLabel()

            whiteLabel.addExistingProvider(aProvider())

            assertThat(whiteLabel.getProviders()).containsOnly(aProvider())
        }
    }

    private fun aWhiteLabel() = WhiteLabel(aWhiteLabelId())

    private fun aWhiteLabelId() = WhiteLabelId(UUID.fromString("550ecc3b-aad8-42e9-86ca-edfd2d244edc"))

    private fun aResellerCompany(whiteLabelId: WhiteLabelId = aWhiteLabelId()) = ResellerCompany(
        resellerCompanyId = aResellerCompanyId(),
        companyName = "PayXpert reseller",
        whiteLabelId = whiteLabelId,
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 4, 14, 10, 30),
            lastModificationDateTime = LocalDateTime.of(2021, 4, 14, 10, 30),
            deletionDateTime = null
        )
    )

    private fun aResellerCompanyId() = ResellerCompanyId(UUID.fromString("872736e6-c4e7-4feb-846d-292a0deeaec2"))

    private fun anAggregatorCompany() = AggregatorCompany(
        aggregatorCompanyId = anAggregatorCompanyId(),
        companyName = "PayXpert aggregator",
        whiteLabelId = aWhiteLabelId(),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 4, 14, 12, 30),
            lastModificationDateTime = LocalDateTime.of(2021, 4, 14, 12, 30),
            deletionDateTime = null
        )
    )

    private fun anAggregatorCompanyId() = AggregatorCompanyId(UUID.fromString("872736e6-c4e7-4feb-846d-292a0deeaec2"))

    private fun aMerchantCompany() = MerchantCompany(
        merchantCompanyId = aMerchantCompanyId(),
        companyName = "PayXpert merchant",
        resellerCompanyId = aResellerCompanyId(),
        whiteLabelId = aWhiteLabelId(),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 5, 12, 10, 30),
            lastModificationDateTime = LocalDateTime.of(2021, 5, 12, 10, 30),
            deletionDateTime = null
        )
    )

    private fun aMerchantCompanyId() = MerchantCompanyId(UUID.fromString("a848c0d5-0223-488b-a74d-0a518a04500b"))

    private fun aMerchantAccountProfile(
        acquirerId: AcquirerId? = anAcquirerId(),
        providerId: ProviderId? = aProviderId()
    ) = MerchantAccountProfile(
        merchantAccountProfileId = aMerchantAccountProfileId(),
        merchantAccountProfileName = "PayXpert merchant account profile",
        whiteLabelId = aWhiteLabelId(),
        acquirerId = acquirerId,
        providerId = providerId,
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45),
            lastModificationDateTime = LocalDateTime.of(2021, 11, 21, 10, 45),
            deletionDateTime = null
        )
    )

    private fun aMerchantAccountProfileId() = MerchantAccountProfileId(
        UUID.fromString("f09e3e27-9cba-4d6b-9b45-0fdc23afb440")
    )

    private fun anAcquirer() = Acquirer(acquirerId = anAcquirerId())

    private fun anAcquirerId() = AcquirerId(UUID.fromString("e2d3e2ec-5c81-41c3-8008-99b75b4bc3c5"))

    private fun aProvider() = Provider(providerId = aProviderId())

    private fun aProviderId() = ProviderId(UUID.fromString("785dca99-48d5-4fee-bb33-1654f50e23c2"))
}
