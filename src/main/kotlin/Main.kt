fun main() {
    val player1 = Player("Player 1", mutableListOf())
    val player2 = Player("Player 2", mutableListOf(), isComputer = true)
    val players = listOf(player1, player2)

    val game = Game(players)
    game.startGame()
    // Testing that player one and player two have 7 different cards
    println(player1.showHand())
    println(player2.showHand())
}

enum class CardType {
    SKIP,
    DRAW2,
    REVERSE,
    NUM_0,
    NUM_1,
    NUM_2,
    NUM_3,
    NUM_4,
    NUM_5,
    NUM_6,
    NUM_7,
    NUM_8,
    NUM_9,
}

enum class CardColor {
    RED,
    YELLOW,
    GREEN,
    BLUE
}

// Card class - card's color(red, green, blue, yellow) and value. wild, skip, reverse, draw 2
class Card(
    val color: CardColor,
    val type: CardType
) {
    override fun toString(): String {
        return "${color.name}_${type.name}"
    }
}

// createDeck loops over the CardColor and CardType enum and creates a card of each type for each color
fun createDeck(): List<Card> {
    val deck = mutableListOf<Card>()

    for (color in CardColor.values()) {
        for (type in CardType.values()) {
            deck.add(Card(color, type))
        }
    }
    return deck
}

// shuffleDeck returns the list of cards in a random order
fun shuffleDeck(deck: List<Card>): MutableList<Card> {
    return deck.shuffled().toMutableList()
}

// Player class - player's name and hand (a list of cards).
class Player(
    val name: String,
    private val hand: MutableList<Card>,
    val isComputer: Boolean = false,
) {
    fun showHand() {
        println(hand)
    }

    fun drawCard(card: Card) {
        hand.add(card)
    }

    // need playCard function to take a card that the user inputs
    // confirm user has card
    // remove card from the users hand
    fun playCard(card: Card) {
        hand.remove(card)
    }

    fun cardsLeft(): Int {
        return hand.size
    }

}

// Game class - players, the current player, and the current card on the table.
class Game(
    private val players: List<Player>
) {
    // startGame creates a deck, shuffles it and then deals 7 cards to each player
    fun startGame() {
        val deck = createDeck()
        val shuffledDeck = shuffleDeck(deck)

        for (player in players) {
            var count: Int = 0
            while (count < 7) {
                player.drawCard(shuffledDeck.removeAt(0))
                count++
            }
        }
    }
}
