package org.demiurg.calculator

import org.demiurg.calculator.Component.ReinforcedIronPlate

fun main() {
    show(ReinforcedIronPlate)
}

fun show(component: Component, number: Int? = null) {
    println(library.calculate(component, number).prettyString())
}