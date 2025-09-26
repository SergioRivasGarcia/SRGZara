package com.srg.zara.util

import kotlinx.coroutines.flow.Flow

/**
 * An inline class that takes a [Flow]. Used to reduce the scope of [Flow] single shot event
 * handling extension functions.
 */
class EventFlow<T>(val flow: Flow<T>)
