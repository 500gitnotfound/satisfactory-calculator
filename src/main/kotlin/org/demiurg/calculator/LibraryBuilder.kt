package org.demiurg.calculator


fun recipes(init: RecipesLibraryBuilder.() -> Unit): RecipesLibrary {
    return RecipesLibraryBuilder().apply(init).build()
}

class RecipesLibraryBuilder {
    private val recipeMap = mutableMapOf<Component, MutableList<Recipe>>()
    private val chosenRecipes = mutableMapOf<Component, Int>()

    fun recipe(output: Component, time: Int, init: RecipeInputsBuilder.() -> Unit) {
        recipe(output, 1, time, init)
    }

    fun recipe(output: Component, number: Int, time: Int, init: RecipeInputsBuilder.() -> Unit) {
        val recipes = recipeMap.computeIfAbsent(output) { mutableListOf() }
        var usedRecipe = false
        val inputsBuilder = RecipeInputsBuilder { usedRecipe = true }.apply(init)
        val inputs = inputsBuilder.inputs
        recipes += Recipe(RecipeItem(output, number), time, inputs, inputsBuilder.assembler)

        if (usedRecipe) {
            chosenRecipes[output] = recipes.size - 1
        } else {
            chosenRecipes.putIfAbsent(output, 0)
        }
    }

    inner class RecipeInputsBuilder(private val useFunction: () -> Unit) {
        private val _inputs = mutableListOf<RecipeItem>()
        var assembler: AssemblerType? = null
            private set
        val inputs: List<RecipeItem> get() = _inputs

        operator fun Item.times(number: Int) {
            _inputs += RecipeItem(this, number)
        }

        fun useIt() {
            useFunction()
        }

        fun madeIn(assembler: AssemblerType) {
            this.assembler = assembler
        }
    }

    fun build(): RecipesLibrary = RecipesLibrary(recipeMap, chosenRecipes)
}