
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * WorldOfSweets - In this game, users take turns drawing cards from a deck and
 * moving the appropriate number of spaces. The first player to reach the last
 * square on the board (Grandma's House) wins the game.
 */
public class WorldOfSweets {

    private static GameFrame gf;
    private static Game game;
    private static GameTimer gt;
    private static int[] playerLocations;
    private static Card lastCard;
    private static BoardPanel bp;
    private static List<Player> players;
    private static PlayerTracker pt;
    private static Space[] spaceArray;
    private static Listener listener = new Listener();
    private static ResizableImagePanel deckImage;
    private static JButton saveButton;
    private static JButton loadButton;
    private static JButton boomerangButton;
    private static int mode;
    private static boolean boomerang = false;
    private static int boomeranged;
    private static boolean winner = false;
    private static AtomicBoolean nextTurn = new AtomicBoolean(true);
    private static AtomicBoolean actionAllowed = new AtomicBoolean(true);
    private static AtomicBoolean pause = new AtomicBoolean(false);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        listener = new Listener();
        gf = new GameFrame(listener);
        gt = new GameTimer(gf);

        game = newGame();
        bp = gf.getBoard().getBoardPanel();
        players = bp.getPlayers();
        setPlayerTypes(game, players);
        // set up token colors
        gf.getBoard().getBoardPanel().setPlayerImages(game.colors, game.numberOfPlayers);
        pt = gf.pt;

        // set space coordinates for 55 space game
        spaceArray = gf.getGameBoard().spaces;
        setSpaceCoordinates(spaceArray);
        game.setSpaceArray(spaceArray);

        // players will all start out at position 0 by default
        playerLocations = new int[game.getNumberofPlayers()];

        initializeListeners();

        //change how fast the AI players move 
        Timer delayAI = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAImove();
                actionAllowed.set(true);
            }
        });
        delayAI.setRepeats(false);
        gf.setVisible(true);

        // simulate turns with normal and ai players 
        while (!winner) {
            if (nextTurn.get() && !pause.get()) {
                nextTurn.set(false);
                int playerNumber = game.getCurrentPlayer();
                Player curPlayer = players.get(playerNumber);
                String playerName = game.getCurrentPlayerName();
                if (curPlayer.getType() == 1) {
                    System.out.println(playerName + " is an AI");
                    actionAllowed.set(false);
                    delayAI.start();
                }
            }
        }
    }

    private static void initializeListeners() {

        BevelBorder raised = new BevelBorder(BevelBorder.RAISED);
        BevelBorder lowered = new BevelBorder(BevelBorder.LOWERED);

        JFileChooser fc = new JFileChooser("Load Saved Game");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("World of Sweets Save Files (*.wos)", "wos");
        File workingDirectory = new File(System.getProperty("user.dir"));
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(workingDirectory);

        deckImage = gf.getDeck();
        saveButton = gf.saveButton;
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pause.set(true);
                gt.stopTimer(); // pause the time when saving
                int returnVal = fc.showSaveDialog(gf);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    // This is where a real application would open the file.
                    // System.out.println("Saving: " + file.getName());

                    try {
                        saveGame(file.getName());
                        JOptionPane.showMessageDialog(gf, "Game saved successfully!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(gf, "Failed to save game. \n(File name must be at least 4 letters)");
                    }

                } else {
                    // System.out.println("Canceling...");
                }
                pause.set(false);
                gt.startTimer(); // resume time when dialog is closed
            }
        });

        loadButton = gf.loadButton;
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pause.set(true);
                gt.stopTimer(); // pause the time when trying to load a game
                int returnVal = fc.showOpenDialog(gf);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    // This is where a real application would open the file.
                    // System.out.println("Opening: " + file.getName());

                    try {
                        loadGame(file.getName());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(gf,
                                "Failed to load game. Please make sure the selected file is a World of Sweets save file and has not been tampered with!");
                    }

                } else {
                    // System.out.println("Canceling...");
                }
                pause.set(false);
                gt.startTimer(); // resume time when dialog is closed
            }
        });

        boomerangButton = gf.boomerangButton;
        boomerangButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (actionAllowed.get()) {
                    boomerangMove();
                }
            }
        });

        // Make deck clickable and trigger new draw
        deckImage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deckImage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (actionAllowed.get()) {
                    System.out.println("called from clicking deck ");
                    playerMove();
                }
            }

            public void mousePressed(MouseEvent e) {
                if (actionAllowed.get()) {
                    deckImage.setBorder(lowered);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (actionAllowed.get()) {
                    deckImage.setBorder(raised);
                }
            }
        });
    }

    /**
     * getAImove - If in strategic mode, the AI player will randomly choose
     * between boomerangs if any left and normal draw. If in normal mode, the AI
     * players by default draw a card.
     */
    private static void getAImove() {
        if (mode == 1) {
            Random rand = new Random();
            int value = rand.nextInt(2);
            if (value == 0) {
                playerMove();
            } else {
                boomerangMove();
            }
        } else {
            int playerNumber = game.getCurrentPlayer();
            Player curPlayer = players.get(playerNumber);
            String playerName = game.getCurrentPlayerName();
            playerMove();
        }

    }

    /**
     * boomerangMove - executes a boomerang move. If the player is an AI, this
     * move will execute automatically and randomly choose a player to boomerang
     * if the player has enough or prompt a normal draw if not. For a normal
     * player, a dialog will ask the user which player to boomerang or inform
     * the player that no boomerangs are left.
     */
    private static void boomerangMove() {
        if (listener.inPlay) {
            int playerNumber = game.getCurrentPlayer();
            Player temp = players.get(playerNumber);
            if (temp.getboomerang() > 0) {
                boomerang = true;
                temp.decrementboomerang();
                boomeranged = game.findPlayer(chooseBoomerangedPlayer());
                JLabel playerLabel = gf.getJLabel(game.getCurrentPlayer());
                gf.displayNewBoomerang(playerLabel, temp.getboomerang());
                listener.inPlay = false;
                if (temp.getType() == 1) {
                    playerMove();
                }
            } else {
                if (temp.getType() == 1) {
                    System.out.println("called from no boomerangs left");
                    playerMove();
                } else {
                    noBoomerangs();
                }
            }
        }
    }

    /**
     * playerMove - executes a normal draw and moves the tokens.
     */
    private static void playerMove() {
        if (!listener.tokenMoved && !winner) {

            int playerNumber;
            listener.cardDrawn = true;
            if (mode == 0 || (mode == 1 && !boomerang)) {
                playerNumber = game.getCurrentPlayer(); // get current player
            } else {
                playerNumber = boomeranged;
            }
            String playerName = game.getCurrentPlayerName();

            int currentLocation = playerLocations[playerNumber];

            Card c = game.draw(playerName, currentLocation, boomerang); // draw card
            if (boomerang) {
                System.out.println(game.getPlayerName(boomeranged) + " was boomeranged with " + c.toString());
            } else {
                System.out.println(playerName + " just drew a " + c.toString());
            }
            lastCard = c;
            String cardColor = c.color();
            String cardType = c.type();
            String path = gf.getPath(cardColor, cardType);
            ResizableImagePanel drawnCard = gf.getDrawnCard();
            gf.displayNewCard(path, drawnCard);

            Player curPlayer = players.get(playerNumber);
            int newLocation = currentLocation;

            if (mode == 0 || (mode == 1 && !boomerang)) {
                if (cardType.equals("mint")) {
                    newLocation = 10;
                } else if (cardType.equals("iceCream")) {
                    newLocation = 19;
                } else if (cardType.equals("cottonCandy")) {
                    newLocation = 28;
                } else if (cardType.equals("candyCane")) {
                    newLocation = 38;
                } else if (cardType.equals("lollipop")) {
                    newLocation = 47;
                } else if (cardType.equals("single")) {
                    for (int i = currentLocation + 1; i <= currentLocation + 6 && i < spaceArray.length; i++) {
                        if (spaceArray[i].getColor().equals(cardColor)
                                || spaceArray[i].getColor().equals("finish")) {
                            newLocation = i;
                            break;
                        }
                    }
                } else if (cardType.equals("double")) {
                    if ((currentLocation + 5) >= spaceArray.length) {
                        newLocation = spaceArray.length - 1;
                    } else {
                        for (int i = currentLocation + 6; i <= currentLocation + 11
                                && i < spaceArray.length; i++) {
                            if (spaceArray[i].getColor().equals(cardColor)
                                    || spaceArray[i].getColor().equals("finish")) {
                                newLocation = i;
                                break;
                            }
                        }
                    }
                }
            } else {
                if (cardType.equals("mint")) {
                    newLocation = 10;
                } else if (cardType.equals("iceCream")) {
                    newLocation = 19;
                } else if (cardType.equals("cottonCandy")) {
                    newLocation = 28;
                } else if (cardType.equals("candyCane")) {
                    newLocation = 38;
                } else if (cardType.equals("lollipop")) {
                    newLocation = 47;
                } else if (cardType.equals("single")) {
                    for (int i = currentLocation - 1; i >= currentLocation - 6 && i >= 0; i--) {
                        if (spaceArray[i].getColor().equals(cardColor)
                                || spaceArray[i].getColor().equals("start")) {
                            newLocation = i;
                            break;
                        }
                    }
                } else if (cardType.equals("double")) {
                    if ((currentLocation - 5) <= 0) {
                        newLocation = 0;
                    } else {
                        for (int i = currentLocation - 6; i <= currentLocation && i >= 0; i--) {
                            if (spaceArray[i].getColor().equals(cardColor)
                                    || spaceArray[i].getColor().equals("start")) {
                                newLocation = i;
                                break;
                            }
                        }
                    }
                }
            }
            boomerang = false;
            curPlayer.setX(spaceArray[newLocation].x_coordinate + 20 * playerNumber);
            curPlayer.setY(spaceArray[newLocation].y_coordinate);
            playerLocations[playerNumber] = newLocation;

            listener.tokenMoved = true;

            if (newLocation == spaceArray.length - 1) {
                winner = true;
                gt.stopTimer();
                pt.gameOver();
                gameOver(game, curPlayer);
            } else {
                game.nextTurn();
                pt.updateTracker();
                nextTurn.set(true);
            }

        }
        listener.tokenMoved = false;
        listener.inPlay = true;
    }

    /**
     * saveGame - allows the user to save the current game as a .wos file.
     *
     * @param filename game name
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     */
    private static void saveGame(String filename) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException {

        if (!filename.substring(filename.length() - 4, filename.length()).equals(".wos")) {
            filename = filename + ".wos";
        }
        int[] saveBoomerang = new int[game.getNumberofPlayers()];
        for (int i = 0; i < game.getNumberofPlayers(); i++) {
            saveBoomerang[i] = players.get(i).getboomerang();
        }

        KeyGenerator keyGen;
        keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, secretKey);

        Object[] saveState = new Object[6];
        saveState[0] = game;
        saveState[1] = playerLocations;
        saveState[2] = lastCard;
        saveState[3] = gt.getTime();
        saveState[4] = mode;
        saveState[5] = saveBoomerang;

        SealedObject so = new SealedObject(saveState, c);

        Object[] output = new Object[2];
        output[0] = so;
        output[1] = secretKey;

        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(output);
        oos.close();
    }

    /**
     * loadGame - allows user to reload an old game that was saved as a .wos
     * file
     *
     * @param filename game file name
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static void loadGame(String filename)
            throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Object[] input = (Object[]) ois.readObject();
        SealedObject so = (SealedObject) input[0];
        SecretKey secretKey = (SecretKey) input[1];

        Object[] saveState = (Object[]) so.getObject(secretKey);
        game = (Game) saveState[0];
        playerLocations = (int[]) saveState[1];
        lastCard = (Card) saveState[2];
        mode = (int) saveState[4];
        int[] saveBoomerang = (int[]) saveState[5];

        gf.dispose();

        listener = new Listener();
        gf = new GameFrame(listener);

        gf.startNewGame(game.getNumberofPlayers(), game, game.colors, mode);
        bp = gf.getBoard().getBoardPanel();
        bp.setPlayerImages(game.colors, game.numberOfPlayers);
        players = bp.getPlayers();
        setPlayerTypes(game, players);
        pt = gf.pt;
        spaceArray = gf.getGameBoard().spaces;
        setSpaceCoordinates(spaceArray);
        gt = new GameTimer(gf, (int) saveState[3]);

        ResizableImagePanel drawnCard = gf.getDrawnCard();

        // display the most recently drawn card
        if (lastCard != null) {
            String cardColor = lastCard.color();
            String cardType = lastCard.type();
            String path = gf.getPath(cardColor, cardType);
            gf.displayNewCard(path, drawnCard);
        } else {
            gf.displayNewCard("assets/cards/discard_pile.png", drawnCard);
        }

        // set players to the correct locations
        for (int i = 0; i < players.size(); i++) {
            Player curPlayer = players.get(i);
            curPlayer.setBoomerang(saveBoomerang[i]);
            int currentLocation = playerLocations[i];
            curPlayer.setX(spaceArray[currentLocation].x_coordinate + 20 * i);
            curPlayer.setY(spaceArray[currentLocation].y_coordinate);
        }

        if (mode == 1) {
            // display correct boomerang counts
            for (int i = 0; i < saveBoomerang.length; i++) {
                JLabel playerLabel = gf.getJLabel(i);
                Player temp = players.get(i);
                gf.displayNewBoomerang(playerLabel, temp.getboomerang());
            }
        }

        initializeListeners();
        gt.startTimer();
        gf.setVisible(true);

        ois.close();
    }

    /**
     * newGame - Sets up the game with 2-4 players upon startup Gets names of
     * players
     *
     * @throws IOException if invalid number of players
     */
    private static Game newGame() throws IOException {
        int numberOfPlayers = setNumberPlayers() + 2;
        JFrame frame = new JFrame();
        String[] names = new String[numberOfPlayers];
        String[] colors = new String[numberOfPlayers];
        int[] types = new int[numberOfPlayers];
        List<String> color_options_list = new ArrayList<String>();
        color_options_list.add("red");
        color_options_list.add("green");
        color_options_list.add("blue");
        color_options_list.add("yellow");
        color_options_list.add("pink");
        color_options_list.add("purple");
        color_options_list.add("white");
        color_options_list.add("brown");
        color_options_list.add("black");
        color_options_list.add("orange");

        // get player's names
        for (int i = 0; i < numberOfPlayers; i++) {
            String name = (String) JOptionPane.showInputDialog(frame, "What is your name?",
                    "Enter Player " + (i + 1) + "'s Name", JOptionPane.PLAIN_MESSAGE, null, null, "Player " + (i + 1));
            if (name == null || name.equals("")) {
                name = "Player " + (i + 1);
            }
            names[i] = name;

            //get if player AI
            String[] player_options = {"Normal player", "AI player"};
            int playerType = JOptionPane.showOptionDialog(frame, "Please select a player type",
                    "Select Player " + (i + 1) + "'s Type", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, player_options, null);
            if (playerType == -1) {
                throw new IllegalArgumentException("No type selected");
            }
            types[i] = playerType;

            // get player colors
            String[] color_options_array = new String[color_options_list.size()];
            color_options_array = color_options_list.toArray(color_options_array);
            int playerColor = JOptionPane.showOptionDialog(frame, "Please select a token color",
                    "Select Player " + (i + 1) + "'s Token", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, color_options_array, color_options_array[0]);
            if (playerColor == -1) {
                throw new IllegalArgumentException("No color selected");
            }
            String list_player_color = color_options_array[playerColor];
            colors[i] = list_player_color;
            color_options_list.remove(list_player_color);
        }
        setMode();
        Game game = new Game(numberOfPlayers, names, colors, types);
        gf.startNewGame(numberOfPlayers, game, colors, mode);
        gt.startTimer();
        return game;
    }

    /**
     * setNumberPlayers - ask for the number of players
     *
     * @return integer indicating the option chosen by the user, or
     * CLOSED_OPTION if the user closed the dialog
     */
    private static int setNumberPlayers() {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        Object[] numberOfPlayersOpt = {"2", "3", "4"};
        return JOptionPane.showOptionDialog(frame, "How many players?", "Enter a Number", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, numberOfPlayersOpt, null);
    }

    /**
     * setMode - get mode for game
     *
     */
    private static void setMode() {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        Object[] numberOfPlayersOpt = {"Classic", "Strategic"};
        mode = JOptionPane.showOptionDialog(frame, "Which game mode would you like to play?", "Choose mode:",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, numberOfPlayersOpt, null);
    }

    /**
     * noBoomerangs - tell user no more of this mode
     *
     */
    private static void noBoomerangs() {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        Object[] numberOfPlayersOpt = {"OK"};
        JOptionPane.showOptionDialog(frame, "No more boomerangs", "Sorry", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, numberOfPlayersOpt, null);
    }

    /**
     * chooseBoomerangedPlayer - get player to boomerang
     *
     * @return integer indicating the option chosen by the user
     */
    private static String chooseBoomerangedPlayer() {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        int numberOfPlayers = game.getNumberofPlayers();
        int numPlayersOpt = numberOfPlayers - 1;
        int playerIndex = 0;
        int currentPlayer = game.getCurrentPlayer();
        Object[] playerNames = new Object[numPlayersOpt];

        for (int i = 0; i < numberOfPlayers; i++) {
            if (i != currentPlayer) {
                playerNames[playerIndex] = game.getPlayerName(i);
                playerIndex++;
            }
        }

        int playa;
        if (players.get(currentPlayer).getType() == 0) {
            playa = JOptionPane.showOptionDialog(frame, "Which player would you like to boomerang?", "Choose Player:",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, playerNames, null);
        } else {
            Random rand = new Random();
            playa = rand.nextInt(numPlayersOpt);
            System.out.println(game.getPlayerName(currentPlayer) + " boomeranged " + playerNames[playa]);
            JOptionPane.showMessageDialog(gf, game.getPlayerName(currentPlayer) + " boomeranged " + playerNames[playa]);
        }
        return "" + playerNames[playa];
    }

    /**
     * setPlayerTypes - set the player's types for a given game
     *
     * @param game current game
     * @param gameplayers list of players
     */
    private static void setPlayerTypes(Game game, List<Player> gameplayers) {
        for (int i = 0; i < gameplayers.size(); i++) {
            int type = game.getPlayerType(i);
            Player p = gameplayers.get(i);
            p.setType(type);
        }
    }

    /**
     * gameOver - A popup dialog displaying the winner and closes the game
     *
     */
    private static void gameOver(Game game, Player winner) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        BufferedImage playerImg = winner.getImage();
        Image scaledPlayer = playerImg.getScaledInstance(playerImg.getWidth() / 10, playerImg.getHeight() / 10,
                Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledPlayer);
        JLabel label = new JLabel(game.getCurrentPlayerName() + " is the WINNER!", JLabel.CENTER);
        label.setFont(new Font("Tahoma", Font.PLAIN, 20));
        // Object[] options = {"Play again?","Nah"};
        Object[] options = {"Bye!"};
        int res = JOptionPane.showOptionDialog(frame, label, "Game Over", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
        System.exit(0);
    }

    /**
     * setSpaceCoordinates - define the coordinates of the board spaces
     *
     */
    private static void setSpaceCoordinates(Space[] spaceArray) {
        spaceArray[0].setX(180);
        spaceArray[0].setY(200);
        spaceArray[1].setX(340);
        spaceArray[1].setY(220);
        spaceArray[2].setX(470);
        spaceArray[2].setY(230);
        spaceArray[3].setX(610);
        spaceArray[3].setY(240);
        spaceArray[4].setX(670);
        spaceArray[4].setY(100);
        spaceArray[5].setX(810);
        spaceArray[5].setY(60);
        spaceArray[6].setX(970);
        spaceArray[6].setY(60);
        spaceArray[7].setX(1070);
        spaceArray[7].setY(140);
        spaceArray[8].setX(1080);
        spaceArray[8].setY(270);
        spaceArray[9].setX(1210);
        spaceArray[9].setY(290);
        spaceArray[10].setX(1340);
        spaceArray[10].setY(220);

        spaceArray[11].setX(1480);
        spaceArray[11].setY(140);
        spaceArray[12].setX(1630);
        spaceArray[12].setY(90);
        spaceArray[13].setX(1780);
        spaceArray[13].setY(120);
        spaceArray[14].setX(1860);
        spaceArray[14].setY(240);
        spaceArray[15].setX(1720);
        spaceArray[15].setY(330);
        spaceArray[16].setX(1650);
        spaceArray[16].setY(430);
        spaceArray[17].setX(1680);
        spaceArray[17].setY(580);
        spaceArray[18].setX(1820);
        spaceArray[18].setY(590);
        spaceArray[19].setX(1930);
        spaceArray[19].setY(680);
        spaceArray[20].setX(1900);
        spaceArray[20].setY(820);

        spaceArray[21].setX(1810);
        spaceArray[21].setY(930);
        spaceArray[22].setX(1650);
        spaceArray[22].setY(920);
        spaceArray[23].setX(1500);
        spaceArray[23].setY(900);
        spaceArray[24].setX(1500);
        spaceArray[24].setY(750);
        spaceArray[25].setX(1510);
        spaceArray[25].setY(580);
        spaceArray[26].setX(1380);
        spaceArray[26].setY(540);
        spaceArray[27].setX(1240);
        spaceArray[27].setY(520);
        spaceArray[28].setX(1110);
        spaceArray[28].setY(590);
        spaceArray[29].setX(1100);
        spaceArray[29].setY(750);
        spaceArray[30].setX(970);
        spaceArray[30].setY(800);

        spaceArray[31].setX(830);
        spaceArray[31].setY(720);
        spaceArray[32].setX(800);
        spaceArray[32].setY(570);
        spaceArray[33].setX(720);
        spaceArray[33].setY(480);
        spaceArray[34].setX(540);
        spaceArray[34].setY(490);
        spaceArray[35].setX(420);
        spaceArray[35].setY(580);
        spaceArray[36].setX(430);
        spaceArray[36].setY(730);
        spaceArray[37].setX(330);
        spaceArray[37].setY(830);
        spaceArray[38].setX(190);
        spaceArray[38].setY(790);
        spaceArray[39].setX(80);
        spaceArray[39].setY(860);
        spaceArray[40].setX(60);
        spaceArray[40].setY(1010);

        spaceArray[41].setX(60);
        spaceArray[41].setY(1180);
        spaceArray[42].setX(100);
        spaceArray[42].setY(1310);
        spaceArray[43].setX(250);
        spaceArray[43].setY(1290);
        spaceArray[44].setX(370);
        spaceArray[44].setY(1180);
        spaceArray[45].setX(500);
        spaceArray[45].setY(1090);
        spaceArray[46].setX(650);
        spaceArray[46].setY(1050);
        spaceArray[47].setX(770);
        spaceArray[47].setY(1130);
        spaceArray[48].setX(860);
        spaceArray[48].setY(1270);
        spaceArray[49].setX(1000);
        spaceArray[49].setY(1320);
        spaceArray[50].setX(1120);
        spaceArray[50].setY(1200);

        spaceArray[51].setX(1190);
        spaceArray[51].setY(1070);
        spaceArray[52].setX(1330);
        spaceArray[52].setY(1070);
        spaceArray[53].setX(1350);
        spaceArray[53].setY(1230);
        spaceArray[54].setX(1430);
        spaceArray[54].setY(1380);
        spaceArray[55].setX(1590);
        spaceArray[55].setY(1400);

        spaceArray[56].setX(1750);
        spaceArray[56].setY(1400);

    }
}
