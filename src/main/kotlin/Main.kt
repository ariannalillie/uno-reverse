fun main() {
    val deck = createDeck()
    val shuffledDeck = shuffleDeck(deck)

    // Currently printing the cards in the deck
    for (card in shuffledDeck) {
        println(card)
    }
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
fun shuffleDeck(deck: List<Card>): List<Card> {
    return deck.shuffled()
}

// Player class - player's name and hand (a list of cards).
class Player(
    val name: String,
    private val hand: MutableList<Card>,
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