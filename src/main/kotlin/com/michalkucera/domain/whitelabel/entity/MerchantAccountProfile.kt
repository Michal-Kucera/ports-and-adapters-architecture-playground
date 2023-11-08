package com.michalkucera.domain.whitelabel.entity

import com.michalkucera.domain.core.entity.Acquirer
import com.michalkucera.domain.core.entity.Provider
import com.michalkucera.domain.core.valueobject.AcquirerId
import com.michalkucera.domain.core.valueobject.ProviderId
import com.michalkucera.domain.whitelabel.valueobject.AuditRecord
import com.michalkucera.domain.whitelabel.valueobject.MerchantAccountProfileId
import com.michalkucera.domain.whitelabel.valueobject.WhiteLabelId
import org.jmolecules.ddd.annotation.Association
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

@Entity
data class MerchantAccountProfile(
    @Identity val merchantAccountProfileId: MerchantAccountProfileId,
    val merchantAccountProfileName: String,
    @Association(aggregateType = WhiteLabel::class) val whiteLabelId: WhiteLabelId,
    @Association(aggregateType = Acquirer::class) val acquirerId: AcquirerId?,
    @Association(aggregateType = Provider::class) val providerId: ProviderId?,
    val auditRecord: AuditRecord
)
