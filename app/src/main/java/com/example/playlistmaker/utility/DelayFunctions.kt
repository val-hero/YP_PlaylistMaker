package com.example.playlistmaker.utility

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam)
            debounceJob?.cancel()

        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

fun loopWithDelay(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    loopCondition: () -> Boolean,
    action: () -> Unit
): () -> Unit {
    return {
        coroutineScope.launch {
            while (loopCondition()) {
                action()
                delay(delayMillis)
            }
        }
    }
}