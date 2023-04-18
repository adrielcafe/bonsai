package cafe.adriel.bonsai.core.util

import platform.Foundation.NSUUID

internal actual fun randomUUID(): String = NSUUID().UUIDString()
