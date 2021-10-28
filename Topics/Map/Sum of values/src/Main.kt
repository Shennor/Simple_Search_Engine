fun summator(map: Map<Int, Int>): Int {
    var sum = 0
    for (i in map.keys){
        if(i % 2 == 0) sum += map[i]!!
    }
    return sum
}