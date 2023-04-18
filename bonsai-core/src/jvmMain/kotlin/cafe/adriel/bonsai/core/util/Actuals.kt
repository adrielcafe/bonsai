package cafe.adriel.bonsai.core.util

import java.util.UUID

internal actual fun randomUUID(): String = UUID.randomUUID().toString()
