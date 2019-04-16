package org.demiurg.calculator

import kotlin.math.ceil

interface Item

enum class Resource : Item {
    IronOre, CopperOre, Limestone, Coal, CrudeOil, CateriumOre, RawQuartz, Sulfur
}

enum class Component : Item {
    IronIngot, IronPlate, IronRod, CopperIngot, Wire, Cable, Concrete, Screw,
    ReinforcedIronPlate, Rotor, ModularFrame, SteelIngot, SteelBeam, SteelPipe,
    EncasedIndustrialBeam, Stator, Motor, HeavyModularFrame, CateriumIngot,
    Quickwire, Plastic, Fuel, Rubber, CircuitBoard, Computer, AILimiter,
    Supercomputer, HighSpeedConnector
}

data class RecipeItem(
    val item: Item,
    val number: Int
)

data class RecipeInMinuteItem(
    val item: Item,
    val number: Double
)

data class Recipe(
    val output: RecipeItem,
    val time: Int,
    val inputs: List<RecipeItem>
) {
    val inMinute: RecipeInMinute by lazy {
        val coefficient = TIME_UNITS_IN_MINUTE / time
        val number = output.number * coefficient
        val inputs = inputs.map { RecipeInMinuteItem(it.item, it.number * coefficient) }
        RecipeInMinute(output.item, number, inputs)
    }

    fun prettyString(): String = StringBuilder().apply {
        appendln("${output.item}:")
        for (input in inputs) {
            append("  ")
            appendln(input.item)
        }
    }.toString()
}

data class RecipeInMinute(
    val output: Item,
    val number: Double,
    val inputs: List<RecipeInMinuteItem>
)

enum class ManufacturerType {
    Constructor, Assembler, Manufacturer
}

sealed class AbstractReportPart {
    companion object {
        const val INDENT = "    "
    }

    abstract fun prettyString(): String
}

fun Double.pretty(): String {
    return "%.0f".format(ceil(this))
}

data class OreReportPart(
    val resource: Resource,
    val number: Double
) : AbstractReportPart() {
    override fun prettyString(): String = "Resource: $resource\n${INDENT}Number: ${number.pretty()}"
}

data class ItemReportPart(
    val component: Component,
    val number: Double,
    val manufacturers: Int,
    val manufacturerType: ManufacturerType
) : AbstractReportPart() {
    override fun prettyString(): String = """
        Component: $component
        ${INDENT}Number: ${number.pretty()}
        $INDENT$manufacturerType: $manufacturers
    """.trimIndent()
}

data class Report(val reportItems: List<AbstractReportPart>) {
    fun prettyString(): String = reportItems.joinToString("\n\n") { it.prettyString() }
}

typealias Recipes = List<Recipe>

fun Recipes.prettyString(): String = joinToString("\n") { it.prettyString() }