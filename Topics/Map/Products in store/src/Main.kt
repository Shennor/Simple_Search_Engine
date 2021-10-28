fun bill(priceList: Map<String, Int>, shoppingList: MutableList<String>): Int{
    var sum = 0
    for(i in shoppingList){
        if(priceList.containsKey(i)) sum += priceList[i]!!
    }
    return sum
}