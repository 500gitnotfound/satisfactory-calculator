package org.demiurg.calculator

import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.ceil

const val TIME_UNITS_IN_MINUTE = 60.0


class RecipesLibrary(
    private val recipeMap: Map<Component, MutableList<Recipe>>,
    private val chosenRecipes: Map<Component, Int>
) {
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

    operator fun get(component: Component): Recipe {
        val index = chosenRecipes[component] ?: throw CalculatorException("There is no recipe for $component")
        val recipes = recipeMap[component] ?: throw CalculatorException("There is no recipe for $component")
        return recipes[index]
    }
}



class CalculatorException(override val message: String) : Exception(message)