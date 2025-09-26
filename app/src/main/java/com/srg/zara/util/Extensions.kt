package com.srg.zara.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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



