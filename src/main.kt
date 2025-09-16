import kotlin.random.Random

fun main() {
    var n: Int? = null
    while (n == null) {
        println("Введите размер массива")
        n = readln().toIntOrNull() ?: continue
        if (n < 1) {
            println("Число должно быть положительным")
            n = null
        }
    }
    var isRandom: Boolean? = null
    while (isRandom == null) {
        println("Ввести массив самому (1) или заполнить случайными числами (2)")
        isRandom = readln().toIntOrNull()?.let { if (it == 1) false else if (it == 2) true else null } ?: continue
    }


    var array = Array(n) {
        if (isRandom) {
            return@Array Random.nextInt(n * 2)
        }
        while (true) {
            println("Введите $it-й элемент массива")
            val n = readln().toIntOrNull() ?: continue
            return@Array n
        }
        -1 // WTF compiler
    }
    print("Неотсортированный массив: ")
    println(array.joinToString(", "))
    val sorted = array.shakeSort()
    print("Отсортированный массив: ")

    println(sorted.array.joinToString(", "))
    println("Количество сравнений: ${sorted.compares}")
    println("Количество перестановок: ${sorted.swaps}")
    println("Что вы хотите сделать?")

}


data class SortedArray(val array: Array<Int>, val swaps: Int, val compares: Int)

/**
 * Шейкерная сортировка
 */
fun Array<Int>.shakeSort(): SortedArray {
    val array = this.copyOf()
    var swaps = 0
    var compares = 0

    var left = 0
    var right = size - 1
    while (left < right) {
        for (i in left..<right) {
            compares++
            if (array[i] > array[i + 1]) {
                val temp = array[i + 1]
                array[i + 1] = array[i]
                array[i] = temp
                swaps++
            }
        }
        right--
        for (i in right downTo left + 1){
            compares++
            if (array[i] < array[i - 1]){
                val temp = array[i - 1]
                array[i - 1] = array[i]
                array[i] = temp
                swaps++
            }
        }
        left++
    }
    return SortedArray(array, swaps, compares)
}