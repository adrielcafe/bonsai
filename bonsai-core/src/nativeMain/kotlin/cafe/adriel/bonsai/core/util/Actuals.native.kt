package cafe.adriel.bonsai.core.util

import com.benasher44.uuid.uuid4
import platform.Foundation.NSUUID

internal actual fun randomUUID(): String = NSUUID().UUIDString()
