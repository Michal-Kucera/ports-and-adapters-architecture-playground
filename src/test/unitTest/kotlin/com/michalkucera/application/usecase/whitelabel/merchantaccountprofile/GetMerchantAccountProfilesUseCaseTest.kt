package com.michalkucera.application.usecase.whitelabel.merchantaccountprofile

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.michalkucera.application.port.input.whitelabel.merchantaccountprofile.GetMerchantAccountProfilesInputPort
import com.michalkucera.domain.core.valueobject.AcquirerId
import com.michalkucera.domain.core.valueobject.ProviderId
import com.michalkucera.domain.whitelabel.entity.MerchantAccountProfile
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.MerchantAccountProfileId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import com.michalkucera.framework.adapter.output.whitelabel.merchantaccountprofile.InMemoryMerchantAccountProfileDatabase
import com.michalkucera.framework.adapter.output.whitelabel.merchantaccountprofile.InMemoryMerchantAccountProfilePersistenceAdapter
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetMerchantAccountProfilesUseCaseTest {

    private val merchantAccountProfileDatabase = InMemoryMerchantAccountProfileDatabase()
    private val underTest: GetMerchantAccountProfilesUseCase = GetMerchantAccountProfilesInputPort(
        InMemoryMerchantAccountProfilePersistenceAdapter(merchantAccountProfileDatabase)
    )

    @Test
    fun `get all merchant account profiles`() {
        merchantAccountProfileDatabase += listOf(aMerchantAccountProfile1(), aMerchantAccountProfile2())

        val merchantAccountProfiles = underTest.getMerchantAccountProfiles()

        assertThat(merchantAccountProfiles).containsExactly(aMerchantAccountProfile1(), aMerchantAccountProfile2())
    }

    @Test
    fun `get nothing when there are no merchant account profiles`() {
        val merchantAccountProfiles = underTest.getMerchantAccountProfiles()

        assertThat(merchantAccountProfiles).isEmpty()
    }

    private fun aMerchantAccountProfile1() = MerchantAccountProfile(
        merchantAccountProfileId = MerchantAccountProfileId(UUID.fromString("2dfd0deb-df5c-4d77-8bca-7ee5bb3c482f")),
        whiteLabelId = WhiteLabelId(UUID.fromString("d5d280ea-1904-420c-9ddd-7b7f8230497e")),
        merchantAccountProfileName = "Merchant Account Profile 1",
        acquirerId = AcquirerId(UUID.fromString("efa32db5-8021-406a-adb0-5d4fa06afded")),
        providerId = ProviderId(UUID.fromString("24f74c52-abdc-449c-a10f-4a0ef4627d43")),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 7, 21, 10, 30, 5),
            lastModificationDateTime = LocalDateTime.of(2021, 8, 21, 10, 30, 5),
            deletionDateTime = null
        )
    )

    private fun aMerchantAccountProfile2() = MerchantAccountProfile(
        merchantAccountProfileId = MerchantAccountProfileId(UUID.fromString("cc93c695-8b00-4653-b354-fa7bd2f74aa9")),
        whiteLabelId = WhiteLabelId(UUID.fromString("d5d280ea-1904-420c-9ddd-7b7f8230497e")),
        merchantAccountProfileName = "Merchant Account Profile 2",
        acquirerId = AcquirerId(UUID.fromString("efa32db5-8021-406a-adb0-5d4fa06afded")),
        providerId = ProviderId(UUID.fromString("24f74c52-abdc-449c-a10f-4a0ef4627d43")),
        auditRecord = AuditRecord(
            creationDateTime = LocalDateTime.of(2021, 8, 21, 10, 30, 5),
            lastModificationDateTime = LocalDateTime.of(2021, 9, 21, 10, 30, 5),
            deletionDateTime = null
        )
    )
}
