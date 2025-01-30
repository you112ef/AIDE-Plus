package com.richpathanimator

sealed class RepeatMode(val value: Int) {
    data object None : RepeatMode(-2)
    data object Restart : RepeatMode(1)
    data object Reverse : RepeatMode(2)
}
