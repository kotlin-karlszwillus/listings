package kmb.com.kotlinsnippets.extensions

import java.util.*

fun Array<*>.randomString() = this.get(Random().nextInt(this.size)).toString()