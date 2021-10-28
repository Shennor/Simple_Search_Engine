class Player(val id: Int, val name: String) {
    companion object {
        var role = "playable character"
        fun getInfo() = "$role"
    }
}

fun getPlayerInfo(player: Player) = "${player.id}, ${player.name}, ${Player.Companion.getInfo()}"