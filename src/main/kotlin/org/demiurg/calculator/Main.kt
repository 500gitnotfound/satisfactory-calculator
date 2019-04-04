package org.demiurg.calculator

const val HELP = "help"
const val CALCULATE = "calculate"
const val CALC = "calc"
const val LIST = "list"
const val EXIT = "exit"

const val INDENT = "    "
const val GREETING = "> "
const val ERROR = "Error"
const val HELP_MESSAGE = "Type '$HELP' to see list of all commands"

fun main() {
    println("Hello. $HELP_MESSAGE")

    loop@while (true) {
        print(GREETING)
        val line = readLine()
        if (line.isNullOrBlank()) continue

        val args = line.split(" ")
        val command = args.first()
        when (command) {
            HELP -> {
                val message = """
                    Available commands:
                    $INDENT$LIST: show all available items
                    $INDENT$CALC <item> [number]: print production chain for `item`
                    $INDENT${INDENT}number (integer) is desired number of items per minute
                    $INDENT${INDENT}it is optional, if there is no number, default value will be chosen as
                    $INDENT${INDENT}number, that one constructor can built
                    $INDENT$CALCULATE <item> [number]: same as $CALC
                    $INDENT$HELP: show this help
                    $INDENT$EXIT: exit from application
                """.trimIndent()
                println(message)
            }

            LIST -> {
                val components = Component.values().joinToString("\n") { "$INDENT$it" }
                val message = "Available components:\n$components"
                println(message)
            }

            CALC, CALCULATE -> {
                try {
                    val componentName = args.getOrNull(1) ?: throw CLIException("Not enough arguments")
                    val component = try {
                        Component.valueOf(componentName)
                    } catch (e: IllegalArgumentException) {
                        throw CLIException("Unknown component: $componentName")
                    }
                    val number = args.getOrNull(2)?.let {
                        val number = try {
                            it.toInt()
                        } catch (e: NumberFormatException) {
                            throw CLIException("Not a number")
                        }
                        if (number <= 0) {
                            throw CLIException("Number must be positive")
                        }
                        number
                    }
                    val report = library.calculate(component, number)
                    println(report.prettyString())
                    println()
                } catch (e: Exception) {
                    val message = e.message?.let { "$ERROR: $it" } ?: ERROR
                    println(message)
                }
            }

            EXIT -> {
                break@loop
            }

            else -> {
                println("Unknown command: $command. $HELP_MESSAGE")
            }
        }
    }
}

class CLIException(override val message: String) : Exception(message)