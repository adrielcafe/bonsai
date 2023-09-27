package cafe.adriel.bonsai.core.util

import platform.Foundation.NSUUID

internal actual val randomUUID: String
    get() = NSUUID().UUIDString()
