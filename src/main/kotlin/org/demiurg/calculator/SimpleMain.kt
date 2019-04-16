package org.demiurg.calculator

import org.demiurg.calculator.Component.*

fun main() {
    show(Computer, 11)
    recipes(Computer)
}

fun show(component: Component, number: Int? = null) {
    println(library.calculate(component, number).prettyString())
}

fun recipes(component: Component) {
    println(library.printRecipes(component).prettyString())
}

/*
import org.demiurg.calculator.show
import org.demiurg.calculator.recipes
import org.demiurg.calculator.Component.*
 */