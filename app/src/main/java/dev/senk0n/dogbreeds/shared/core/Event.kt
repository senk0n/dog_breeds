package dev.senk0n.dogbreeds.shared.core

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Event<T>(v: T) {
    var valueOrNull: T? = v
        get() = field?.also { field = null }
        private set
}

fun <T> Delegates.event(v: T): ReadOnlyProperty<Any?, T?> =
    object : ReadOnlyProperty<Any?, T?> {
        var field: T? = v
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            field?.also { field = null }
    }
