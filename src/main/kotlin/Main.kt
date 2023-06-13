fun main() {
    val players = listOf(
        HumanPlayer(),
        ComputerPlayer()
    )

    val game = Game(players)
    game.setUpGame()
    game.playGame()
    game.checkForWin()
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

enum class CardColor(val textColor: String, val bgColor: String) {
    RED("\u001B[30m", "\u001B[41m"),
    YELLOW("\u001B[30m", "\u001B[43m"),
    GREEN("\u001B[30m", "\u001B[42m"),
    BLUE("\u001B[30m", "\u001B[44m"),
}

class Card(val color: CardColor, val type: CardType) {
    override fun toString(): String {
        val reset = "\u001B[0m"
        val formattedText = "${color.textColor}${color.bgColor}${type.name}$reset"
        return formattedText
    }
}

fun createDeck(): List<Card> {
    return CardColor.values().flatMap { color ->
        CardType.values().map { type ->
            Card(color, type)
        }
    }
}

fun shuffleDeck(deck: List<Card>): MutableList<Card> {
    return deck.shuffled().toMutableList()
}

interface Player {
    fun showHand()
    fun showCurrentCard(index: Int): Card
    fun drawCard(card: Card)
    fun cardsLeft(): Int
    fun playCard(currentCard: Card, deck: MutableList<Card>): Card?
}

class HumanPlayer : Player {
    private val hand = mutableListOf<Card>()

    override fun showHand() {
        println(hand)
    }

    override fun showCurrentCard(index: Int): Card {
        return hand[index]
    }

    override fun drawCard(card: Card) {
        hand += card
    }

    override fun cardsLeft(): Int {
        return hand.size
    }

    override fun playCard(currentCard: Card, deck: MutableList<Card>): Card? {
        var validCardPlayed = false
        var playedCard: Card? = null

        while (!validCardPlayed) {
            println("Your cards are: $hand")
            print("Enter the index of the card you would like to play, or type -1 to draw: ")
            val index: Int = readLine()?.toIntOrNull() ?: -2

            if (index == -1) {
                val newCard = deck.removeAt(0)
                drawCard(newCard)
            } else {
                println("The card you choose is: ${showCurrentCard(index)}")
                val selectedCard = showCurrentCard(index)

                if (selectedCard.color == currentCard.color || selectedCard.type == currentCard.type) {
                    hand.removeAt(index)  // Remove the card from the hand
                    playedCard = selectedCard
                    validCardPlayed = true
                } else {
                    println("This is not a valid move, please try again")
                }
            }
        }
        return playedCard
    }
}

class ComputerPlayer : Player {
    private val hand = mutableListOf<Card>()

    override fun showHand() {
        println(hand)
    }

    override fun showCurrentCard(index: Int): Card {
        return hand[index]
    }

    override fun drawCard(card: Card) {
        hand += card
    }

    override fun cardsLeft(): Int {
        return hand.size
    }

    override fun playCard(currentCard: Card, deck: MutableList<Card>): Card? {
        val playableCards = hand.filter { card -> card.type == currentCard.type || card.color == currentCard.color }

        if (playableCards.isNotEmpty()) {
            val cardPicked = playableCards.random()
            hand.remove(cardPicked)  // Remove the card from the hand
            println("The computer played $cardPicked")
            return cardPicked
        } else {
            while (true) {
                val newCard = deck.removeAt(0)
                drawCard(newCard)
                if (newCard.type == currentCard.type || newCard.color == currentCard.color) {
                    println("The computer drew $newCard")
                    return newCard
                }
            }
        }
    }
}

class Game(private val players: List<Player>) {
    private var currentPlayerIndex = 0
    private var gameOver = false
    private var currentCard: Card? = null
    private val deck: List<Card> by lazy { createDeck() }
    private val shuffledDeck: MutableList<Card> by lazy { shuffleDeck(deck) }

    fun setUpGame() {
        for (player in players) {
            repeat(7) {
                player.drawCard(shuffledDeck.removeAt(0))
            }
        }
        currentCard = shuffledDeck.removeAt(0)
    }

    fun playGame() {
        while (!gameOver) {
            val currentPlayer = players[currentPlayerIndex]
            println(" ")
            println("Current Player: ${currentPlayer.javaClass.simpleName}")
            println("The current card on the table is: ${currentCard.toString()}")

            val playedCard = currentPlayer.playCard(currentCard!!, shuffledDeck)
            if (playedCard != null) {
                currentCard = playedCard
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        }
    }

    fun checkForWin() {
        players.forEachIndexed { index, player ->
            if (player.cardsLeft() == 0) {
                gameOver = true
                if (index == 0) {
                    println("Congrats! You win 🎉")
                } else {
                    println("The computer wins this round, better luck next time!")
                }
                return
            }
        }
    }
}


// NICE TO HAVES:
// add ASCII art or colored output to display the current card ✅
// Allow user to pick card in a more user-friendly way (not by index)
// Clear console periodically (maybe after user chooses to draw a card) to make terminal info more readable

//TODOS:
// Computer draw card function
// Add logic for special cases (reverse, draw2, draw4, skip)
// Implement wild card
// Create a list for the cards put down in case deck runs out

//BUGS:
// Errors out of users input in NaN