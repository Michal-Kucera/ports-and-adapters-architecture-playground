package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.AuditRecordFactory
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.jmolecules.ddd.annotation.Association
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity
import java.time.LocalDateTime

@Entity
data class ResellerCompany(
    @Identity val resellerCompanyId: ResellerCompanyId,
    val companyName: String,
    @Association(aggregateType = WhiteLabel::class) val whiteLabelId: WhiteLabelId,
    val auditRecord: AuditRecord
) {

    private val merchantCompanies = mutableSetOf<MerchantCompany>()

    fun addNewMerchantCompany(
        merchantCompanyId: MerchantCompanyId,
        companyName: String,
        creationDateTime: LocalDateTime
    ): MerchantCompany {
        val merchantCompany = MerchantCompany(
            merchantCompanyId = merchantCompanyId,
            companyName = companyName,
            whiteLabelId = whiteLabelId,
            resellerCompanyId = resellerCompanyId,
            auditRecord = AuditRecordFactory.createdRecord(creationDateTime)
        )
        merchantCompanies += merchantCompany
        return merchantCompany
    }

    fun getMerchantCompanies() = merchantCompanies.toSet()
}
