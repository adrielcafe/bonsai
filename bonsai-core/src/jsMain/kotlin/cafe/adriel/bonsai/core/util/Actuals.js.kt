package cafe.adriel.bonsai.core.util

import com.benasher44.uuid.uuid4

internal actual fun randomUUID(): String = uuid4().toString()
