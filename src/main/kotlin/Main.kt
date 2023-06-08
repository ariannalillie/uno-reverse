fun main() {
    val player1 = Player("Player 1", mutableListOf())
    val player2 = Player("Player 2", mutableListOf(), isComputer = true)
    val players = listOf(player1, player2)

    val game = Game(players)
    player1.game = game
    player2.game = game

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

// Card class - card's color(red, green, blue, yellow) and value. wild, skip, reverse, draw 2
class Card(
    val color: CardColor,
    val type: CardType
) {
    override fun toString(): String {
        val reset = "\u001B[0m"
        val formattedText = "${color.textColor}${color.bgColor}${type.name}$reset"
        return formattedText
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
    var game: Game? = null
) {
    fun showHand() {
        println(hand)
    }

    fun showCurrentCard(index: Int): Card {
        return hand[index]
    }

    fun drawCard(card: Card) {
        hand.add(card)
    }

    fun cardsLeft(): Int {
        return hand.size
    }

    fun playCard(currentPlayer: Player, currentCard: Card, deck: MutableList<Card>) {
        var validCardPlayed = false

        // user will be asked to continue to draw cards/ type a valid response until they play a valid card
        while (!validCardPlayed) {
            println("Your cards are: ${currentPlayer.showHand()}")
            print("Enter the index of the card you would like to play, or type -1 to draw: ")
            val index: Int = readln().toInt()
            if (index == -1) {
                val newCard = deck.removeAt(0)
                drawCard(newCard)
            } else {
                println("The card you choose is: ${currentPlayer.showCurrentCard(index)}")
                val selectedCard = hand[index]

                if (selectedCard.color == currentCard.color || selectedCard.type == currentCard.type) {
                    hand.removeAt(index)
                    game?.updateCurrentCard(selectedCard)
                    game?.swapPlayer(selectedCard)
                    validCardPlayed = true
                } else {
                    println("This is not a valid move, please try again")
                }
            }
        }
    }

    fun computerMove(deck: MutableList<Card>) {
        // EASY: finds three playable cards and randomly chooses one
        val currentCardType = game?.currentCard?.type
        val currentCardColor = game?.currentCard?.color
        val playableCards = hand.filter { card -> card.type == currentCardType || card.color == currentCardColor }

        if (playableCards.isNotEmpty()) {
            val cardPicked = playableCards.random()
            game?.updateCurrentCard(cardPicked)
            val cardIndex = hand.indexOf(cardPicked)
            hand.removeAt(cardIndex)
            game?.swapPlayer(cardPicked)
            println("The computer played $cardPicked")
        } else {
            val newCard = deck.removeAt(0)
            drawCard(newCard)
        }
    }
}

// Game class - players, the current player, and the current card on the table.
class Game(
    private val players: List<Player>
) {
    var currentPlayer = players[0]
    var gameOver = false
    var currentCard: Card? = null
    // Used as lazy property delegate so that the deck will be created only when it is
    // accessed for the first time
    val deck: List<Card> by lazy { createDeck() }
    // Used as lazy property delegate so that the shuffledDeck will be created only when
    // it is accessed for the first time
    val shuffledDeck: MutableList<Card> by lazy { shuffleDeck(deck) }

    // startGame creates a deck, shuffles it and then deals 7 cards to each player
    fun setUpGame() {

        for (player in players) {
            var count = 0
            while (count < 7) {
                player.drawCard(shuffledDeck.removeAt(0))
                count++
            }
        }
        currentCard = shuffledDeck.removeAt(0)
    }

    fun playGame() {
        players.forEach { player -> player.game = this }

        while (!gameOver) {
//            if (!repeatedTurn) {
            println(" ")
            println("Current Player: ${currentPlayer.name}")
            println("The current card on the table is: ${currentCard.toString()}")
//            } else {
//                println("The computer drew a card")
//            }
            if (!currentPlayer.isComputer) {
                currentPlayer.playCard(currentPlayer, currentCard!!, shuffledDeck)
                // Computer loop logic
            } else {
                currentPlayer.computerMove(shuffledDeck)
            }


            // Swap current player
        }
    }

    fun swapPlayer(selectedCard: Card) {
        if (selectedCard.type == CardType.REVERSE || selectedCard.type == CardType.SKIP) {
            currentPlayer = currentPlayer
        } else {
            currentPlayer = if (currentPlayer === players[0]) players[1] else players[0]
        }
    }

    fun updateCurrentCard(playedCard: Card) {
        currentCard = playedCard
    }

    // checkForWin checks to see if either the player or the computer has an empty hand to determine if
    // anyone has won yet, and if so sets `gameOver` to true
    fun checkForWin() {
        if (players[0].cardsLeft() == 0) {
            gameOver = true
            println("Congrats! You win 🎉")
        } else if (players[1].cardsLeft() == 0) {
            gameOver = true
            println("The computer wins this round, better luck next time!")
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