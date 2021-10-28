package search

import java.io.File

enum class Strategy(toString : String){
    ALL("ALL"),
    ANY("ANY"),
    NONE("NONE")
}

class PeopleBase{
    private val data_lines = mutableListOf<String>()
    private val inverted_map = mutableMapOf<String, MutableList<Int>>()

    fun findByWord(word : String) : MutableSet<Int>{
        var indices = mutableSetOf<Int>()
        for(i in inverted_map.keys){
            if (i.lowercase() == word.lowercase()) {
                indices.addAll(inverted_map[i]!!)
            }
        }
        return indices
    }

    fun findAnyOf(words : List<String>) : MutableSet<Int>{
        var indices = mutableSetOf<Int>()
        for(w in words){
            indices.addAll(findByWord(w))
        }
        return indices
    }

    fun findAllOf(words : List<String>) : MutableSet<Int>{
        var indices = data_lines.indices.toMutableSet()
        for(w in words){
            indices = indices.intersect(findByWord(w)).toMutableSet()
        }
        return indices
    }

    fun findNoneOf(words : List<String>) : MutableSet<Int>{
        var wrong_indices = findAnyOf(words)
        var indices = mutableSetOf<Int>()
        for(i in data_lines.indices){
            if(!wrong_indices.contains(i)) indices.add(i)
        }
        return indices
    }

    fun addPerson(person : String){
        data_lines.add(person)
        val index = data_lines.indexOf(person)
        var words = person.split(" ")
        for(w in words){
            if (!inverted_map.containsKey(w)) inverted_map[w] = mutableListOf(index)
            else inverted_map[w]!!.add(index)
        }
    }

    fun printlnByIndices(indices : List<Int>){
        for(i in indices){
            println(data_lines[i])
        }
    }

    fun getIndices() : IntRange{
        return data_lines.indices
    }
}

class Menu{
    private val base = PeopleBase()

    fun addPeopleFromInput(){
        println("Enter the number of people:")
        val numPeople = readLine()!!.toInt()
        println("Enter all people:")
        for(i in 1..numPeople){
            val str = readLine()!!
            base.addPerson(str)
        }
    }

    fun addPeopleFromFile(file : File){
        val lines = file.readLines()
        for(i in lines){
            base.addPerson(i)
        }
    }

    override fun toString() : String{
        return """=== Menu ===
1. Find a person
2. Print all people
0. Exit"""
    }

    fun findPerson(){
        println("Select a matching strategy: ALL, ANY, NONE")
        var success = false
        var strategy = Strategy.ALL
        while(!success) {
            try {
                var i = readLine()!!.replace("\\s".toRegex(),"")
                strategy = Strategy.valueOf(i.uppercase())
                success = true
            } catch (e: Exception) {
                println("Typo in strategy identifier. Try again.")
                success = false
            }
        }
        println()
        println("Enter a name or email to search all suitable people.")
        val words = readLine()!!.split(" ")
        var resultIndices = when(strategy){
            Strategy.ALL -> base.findAllOf(words)
            Strategy.ANY -> base.findAnyOf(words)
            Strategy.NONE -> base.findNoneOf(words)
        }
        if (resultIndices.isNotEmpty()) {
            println()
            println("${resultIndices.size} persons found:")
            base.printlnByIndices(resultIndices.toList())
        }
        else println("No matching people found.")
    }

    fun printListOfPeople(){
        println("=== List of people ===")
        base.printlnByIndices(base.getIndices().toList())
    }

    fun exit(){
        println("Bye!")
    }
}

fun getValueIfInRange(range : IntRange) : Int?{
    var i = readLine()!!.toInt()
    while(i !in range){
        println("\nIncorrect option! Try again.\n")
        return null
    }
    return i
}

fun main(args : Array<String>) {
    val menu = Menu()
    if(args.size != 2) menu.addPeopleFromInput()
    else{
        if(args[0] == "--data"){
            try {
                val file = File(args[1])
                menu.addPeopleFromFile(file)
            }
            catch(e : Exception){
                println("Something wrong with file, chek it")
                return
            }
        }
    }
    println()
    var end = false
    while(!end) {
        var value: Int? = null
        while (value == null) {
            println(menu)
            value = getValueIfInRange(0..2)
        }
        println()
        when (value) {
            1 -> {
                menu.findPerson()
                println()
            }
            2 -> {
                menu.printListOfPeople()
                println()
            }
            0 -> end = true
        }
    }
    menu.exit()
}