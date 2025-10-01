package com.srg.zara.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.srg.zara.R
import kotlinx.coroutines.flow.onEach

/**
 * Subscribes to [EventFlow] streams, the action gets invoked each time a new value gets emitted to
 * the stream. An [EventFlow] can be acquired by using the extension function [receiveAsEventFlow]
 */
inline fun <reified T : Any, EF : EventFlow<T>> LifecycleOwner.on(
    eventFlow: EF,
    useViewLifeCycle: Boolean = true,
    crossinline body: (T) -> Unit
) {
    eventFlow.flow
        .onEach { body.invoke(it) }
        .observeInLifecycle(if (this is Fragment && useViewLifeCycle) viewLifecycleOwner else this)
}

fun TextView.setTextColorStatus(
    context: Context,
    string: CharSequence
) {
    val color = if (string == DEAD) {
        ContextCompat.getColor(context, R.color.red)
    } else if (string == ALIVE) {
        ContextCompat.getColor(context, R.color.green)
    } else {
        ContextCompat.getColor(context, R.color.teal_700)
    }
    setTextColor(color)
}

const val DEAD = "Dead"
const val ALIVE = "Alive"



