package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.core.entity.Acquirer
import com.michalkucera.domain.core.entity.Provider
import com.michalkucera.domain.core.valueobject.AcquirerId
import com.michalkucera.domain.core.valueobject.ProviderId
import com.michalkucera.domain.whitelabel.valueobject.AggregatorCompanyId
import com.michalkucera.domain.whitelabel.valueobject.AuditRecordFactory
import com.michalkucera.domain.whitelabel.valueobject.MerchantAccountProfileId
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.jmolecules.ddd.annotation.AggregateRoot
import org.jmolecules.ddd.annotation.Identity
import java.time.LocalDateTime

@AggregateRoot
class WhiteLabel(
    @Identity val whiteLabelId: WhiteLabelId
) {
    private val resellerCompanies = mutableSetOf<ResellerCompany>()
    private val aggregatorCompanies = mutableSetOf<AggregatorCompany>()
    private val merchantAccountProfiles = mutableSetOf<MerchantAccountProfile>()
    private val acquirers = mutableSetOf<Acquirer>()
    private val providers = mutableSetOf<Provider>()

    fun addNewResellerCompany(
        resellerCompanyId: ResellerCompanyId,
        companyName: String,
        creationDateTime: LocalDateTime
    ): ResellerCompany {
        val resellerCompany = ResellerCompany(
            resellerCompanyId = resellerCompanyId,
            companyName = companyName,
            whiteLabelId = whiteLabelId,
            auditRecord = AuditRecordFactory.createdRecord(creationDateTime)
        )
        resellerCompanies += resellerCompany
        return resellerCompany
    }

    fun addExistingResellerCompany(resellerCompany: ResellerCompany) {
        if (resellerCompany.whiteLabelId != whiteLabelId) {
            throw IllegalArgumentException(
                "Reseller company with ID: [${resellerCompany.resellerCompanyId}] does not belong to a white label with ID: [$whiteLabelId]"
            )
        }

        resellerCompanies += resellerCompany
    }

    fun addNewAggregatorCompany(
        aggregatorCompanyId: AggregatorCompanyId,
        companyName: String,
        creationDateTime: LocalDateTime
    ): AggregatorCompany {
        val aggregatorCompany = AggregatorCompany(
            aggregatorCompanyId = aggregatorCompanyId,
            companyName = companyName,
            whiteLabelId = whiteLabelId,
            auditRecord = AuditRecordFactory.createdRecord(creationDateTime)
        )
        aggregatorCompanies += aggregatorCompany
        return aggregatorCompany
    }

    fun addNewMerchantCompanyToExistingResellerCompany(
        resellerCompanyId: ResellerCompanyId,
        merchantCompanyId: MerchantCompanyId,
        companyName: String,
        creationDateTime: LocalDateTime
    ): MerchantCompany {
        val resellerCompany = resellerCompanies.firstOrNull { it.resellerCompanyId == resellerCompanyId }
            ?: throw IllegalArgumentException("Reseller company with ID: [$resellerCompanyId] not found in white label")
        return resellerCompany.addNewMerchantCompany(merchantCompanyId, companyName, creationDateTime)
    }

    fun addNewMerchantAccountProfile(
        merchantAccountProfileId: MerchantAccountProfileId,
        merchantAccountProfileName: String,
        acquirerId: AcquirerId?,
        providerId: ProviderId?,
        creationDateTime: LocalDateTime
    ): MerchantAccountProfile {
        if (acquirerId != null && acquirers.none { it.acquirerId == acquirerId }) {
            throw IllegalArgumentException("Acquirer with ID: [$acquirerId] not found in white label")
        }
        if (providerId != null && providers.none { it.providerId == providerId }) {
            throw IllegalArgumentException("Provider with ID: [$providerId] not found in white label")
        }
        val merchantAccountProfile = MerchantAccountProfile(
            merchantAccountProfileId = merchantAccountProfileId,
            merchantAccountProfileName = merchantAccountProfileName,
            whiteLabelId = whiteLabelId,
            acquirerId = acquirerId,
            providerId = providerId,
            auditRecord = AuditRecordFactory.createdRecord(creationDateTime)
        )
        merchantAccountProfiles += merchantAccountProfile
        return merchantAccountProfile
    }

    fun getResellerCompanies() = resellerCompanies.toSet()

    fun addExistingAcquirer(acquirer: Acquirer) {
        acquirers += acquirer
    }

    fun addExistingProvider(provider: Provider) {
        providers += provider
    }

    fun getAggregatorCompanies() = aggregatorCompanies.toSet()

    fun getMerchantAccountProfiles() = merchantAccountProfiles.toSet()

    fun getAcquirers() = acquirers.toSet()

    fun getProviders() = providers.toSet()
}
