fun main() {
    val deck = createDeck()
    for (card in deck) {
        println(card.toString())
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

// Deck class - a list property to store the cards.
// Player class - player's name and hand (a list of cards).
// Game class - players, the current player, and the current card on the table.