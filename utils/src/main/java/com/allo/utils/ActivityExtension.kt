package com.allo.utils

import android.app.Activity



inline fun <reified T> Activity.intentData(key: String) = lazy {
    intent?.extras?.get(key) as? T
}

inline fun <reified T> Activity.intentData(key: String, default: T) = lazy {
    intent?.extras?.get(key) as? T ?: default
}

inline fun <reified T> Activity.safeIntentData(name: String) = lazy<T> {
    checkNotNull(intent?.extras?.get(name) as? T) { "No intent value for key \"$name\"" }
}