package cafe.adriel.bonsai.core.util

import java.util.UUID

internal actual val randomUUID: String
    get() = UUID.randomUUID().toString()
