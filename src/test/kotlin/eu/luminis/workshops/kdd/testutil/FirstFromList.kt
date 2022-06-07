package eu.luminis.workshops.kdd.testutil

import org.assertj.core.api.Assertions

internal inline fun <reified T> List<*>.firstAndOnlyItemAs(): T {
    Assertions.assertThat(this.size).isEqualTo(1)
    return this[0] as T
}