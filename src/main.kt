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
    while (true) {
        print("Неотсортированный массив: ")
        println(array.joinToString(", "))
        val sorted = array.shakeSort()
        print("Отсортированный массив: ")

        println(sorted.array.joinToString(", "))
        println("Количество сравнений: ${sorted.compares}")
        println("Количество перестановок: ${sorted.swaps}")
        println("Что вы хотите сделать?")
        println("1. Добавить элемент")
        println("2. Удалить элемент")
        println("3. Найти элемент по его позиции")
        println("4. Выйти")

        val variant = readln().toIntOrNull()
        when (variant) {
            1 -> array = array.interactiveAdd()
            2 -> array = array.interactiveRemove()
            3 -> sorted.interactiveFindByPosition()
            4 -> break
        }
    }
}


private fun Array<*>.inputIndex(query: String, min: Int = 0, max: Int = size): Int {
    var i = -1

    while (i !in min..max) {
        println(query)
        i = readln().toIntOrNull() ?: continue
    }
    return i
}

private fun Array<Int>.interactiveAdd(): Array<Int> {
    val i = inputIndex("Введите индекс (от 0 до $size) куда вы хотите добавить новый элемент")
    var num: Int? = null
    while (num == null) {
        println("Введите значение элемента")
        num = readln().toIntOrNull() ?: continue
    }
    return this.sliceArray(0..<i) + num + this.sliceArray(i..<size)

}

private fun Array<Int>.interactiveRemove(): Array<Int> {
    val i = inputIndex("Введите индекс элемента (от 0 до $size), который вы хотите удалить")
    return this.sliceArray(0..<i) + this.sliceArray(i + 1..<size)
}

private fun SortedArray.interactiveFindByPosition() {
    val i = array.inputIndex("Введите число по порядку (от 1 до ${array.size})", 1, array.size)
    println("Число по порядку $i: ${findByPosition(i)}")
}

/**
 * Класс возвращающий отсортированный массив
 *
 * @param array отсортированный массив
 * @param swaps количество перестановок
 * @param compares количество сравнений
 */
data class SortedArray(val array: Array<Int>, val swaps: Int, val compares: Int) {

    /**
     * Поиск k-го по порядку числа
     */
    fun findByPosition(k: Int): Int {
        return array[k - 1]
    }
}

/**
 * Шейкерная сортировка
 */
fun Array<Int>.shakeSort(): SortedArray {
    val array = this.copyOf()

    // Количество перестановок и сравнений
    var swaps = 0
    var compares = 0

    // Левая и правая граница
    var left = 0
    var right = size - 1
    do {
        var isSwapped = false // Есть ли перестановки
        var swappedIndex = left

        // Слева направо
        for (i in left..<right) {
            compares++
            if (array[i] > array[i + 1]) {
                val temp = array[i + 1]
                array[i + 1] = array[i]
                array[i] = temp
                swaps++
                swappedIndex = i
                isSwapped = true
            }
        }
        if (!isSwapped) break // Если список уже отсортирован, то в нём нет перестановок

        right = swappedIndex

        // Справа налево
        for (i in right downTo left + 1) {
            compares++
            if (array[i] < array[i - 1]) {
                val temp = array[i - 1]
                array[i - 1] = array[i]
                array[i] = temp
                swaps++
                swappedIndex = i
                isSwapped = true
            }
        }
        left = swappedIndex
    } while (isSwapped)
    return SortedArray(array, swaps, compares)
}