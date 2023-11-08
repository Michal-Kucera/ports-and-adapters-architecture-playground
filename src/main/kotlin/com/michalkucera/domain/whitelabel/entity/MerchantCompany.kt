package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.MerchantCompanyId
import com.michalkucera.domain.whitelabel.valueobject.ResellerCompanyId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.jmolecules.ddd.annotation.Association
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

@Entity
data class MerchantCompany(
    @Identity val merchantCompanyId: MerchantCompanyId,
    val companyName: String,
    @Association(aggregateType = WhiteLabel::class) val whiteLabelId: WhiteLabelId,
    @Association(aggregateType = ResellerCompany::class) val resellerCompanyId: ResellerCompanyId,
    val auditRecord: AuditRecord
)
