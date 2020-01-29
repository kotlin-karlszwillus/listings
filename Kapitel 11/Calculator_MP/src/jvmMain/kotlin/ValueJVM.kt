package com.openpromos.jni.nrexpressionlib.value

import java.net.URLEncoder

actual fun encodeURIComponent(str: String): String = URLEncoder.encode(str, "UTF-8")
