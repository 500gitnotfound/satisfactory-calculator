package org.demiurg.calculator

import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.ceil

const val TIME_UNITS_IN_MINUTE = 60.0

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
}

data class RecipeInMinute(
    val output: Item,
    val number: Double,
    val inputs: List<RecipeInMinuteItem>
)

class RecipeInputsBuilder {
    private val _inputs = mutableListOf<RecipeItem>()
    val inputs: List<RecipeItem> get() = _inputs

    operator fun Item.times(number: Int) {
        _inputs += RecipeItem(this, number)
    }
}

enum class ManufacturerType {
    Constructor, Assembler, Manufacturer
}

sealed class AbstractReportPart {
    companion object {
        const val INDENT = "    "
    }

    abstract fun prettyString(): String
}

data class OreReportPart(
    val resource: Resource,
    val number: Double
) : AbstractReportPart() {
    override fun prettyString(): String = "Resource: $resource\n${INDENT}Number: $number"
}

data class ItemReportPart(
    val component: Component,
    val number: Double,
    val manufacturers: Int,
    val manufacturerType: ManufacturerType
) : AbstractReportPart() {
    override fun prettyString(): String = """
        Component: $component
        ${INDENT}Number: $number
        $INDENT$manufacturerType: $manufacturers
    """.trimIndent()
}

data class Report(val reportItems: List<AbstractReportPart>) {
    fun prettyString(): String = reportItems.joinToString("\n\n") { it.prettyString() }
}

class RecipesLibrary {
    private val recipeMap = mutableMapOf<Component, Recipe>()
    val recipes: List<Recipe> get() = recipeMap.values.toList()

    fun recipe(output: Component, time: Int, init: RecipeInputsBuilder.() -> Unit) {
        recipe(output, 1, time, init)
    }

    fun recipe(output: Component, number: Int, time: Int, init: RecipeInputsBuilder.() -> Unit) {
        recipeMap[output] = Recipe(RecipeItem(output, number), time, RecipeInputsBuilder().apply(init).inputs)
    }

    // number = items in minute
    fun calculateCount(component: Component, number: Int): Map<Item, Double> {
        val countsForOneMinute = calculateCountForOneMinute(component)
        val itemNumberInMinute = component.recipeInMinute.number
        val coefficient = number / itemNumberInMinute
        return countsForOneMinute.mapValues { (_, count) -> count * coefficient }
    }

    fun calculateCountForOneMinute(targetComponent: Component): Map<Item, Double> {
        val targetItemNumber = targetComponent.recipeInMinute.number
        val itemsCount = LinkedHashMap<Item, Double>()
        val stack = LinkedList<RecipeInMinuteItem>()
        stack.add(RecipeInMinuteItem(targetComponent, targetItemNumber))
        while (stack.isNotEmpty()) {
            val (item, number) = stack.pollFirst()
            itemsCount[item] = itemsCount.getOrDefault(item, 0.0) + number
            if (item is Component) {
                val recipe = item.recipeInMinute
                val coefficient = number / recipe.number
                recipe.inputs.forEach {
                    stack.add(RecipeInMinuteItem(it.item, it.number * coefficient))
                }
            }
        }
        return itemsCount
    }

    fun calculate(targetComponent: Component, targetNumber: Int? = null): Report {
        val countMap = if (targetNumber != null) {
            calculateCount(targetComponent, targetNumber)
        } else {
            calculateCountForOneMinute(targetComponent)
        }
        val parts = mutableListOf<AbstractReportPart>()
        for ((item, number) in countMap) {
            parts += when (item) {
                is Resource -> OreReportPart(item, number)
                is Component -> {
                    val manufacturers = ceil(number / item.recipeInMinute.number).toInt()
                    val manufacturerType = when (item.recipe.inputs.size) {
                        1 -> ManufacturerType.Constructor
                        2 -> ManufacturerType.Assembler
                        in 3..4 -> ManufacturerType.Manufacturer
                        else -> throw IllegalStateException()
                    }
                    ItemReportPart(item, number, manufacturers, manufacturerType)
                }
                else -> throw IllegalStateException("Unknown entity type: $item")
            }
        }
        return Report(parts)
    }

    val Component.recipeInMinute: RecipeInMinute get() = recipe.inMinute
    val Component.recipe: Recipe get() = get(this)

    operator fun get(component: Component): Recipe = recipeMap[component] ?: throw CalculatorException("There is no recipe for $component")
}

fun recipes(init: RecipesLibrary.() -> Unit): RecipesLibrary {
    return RecipesLibrary().apply(init)
}

class CalculatorException(override val message: String) : Exception(message)