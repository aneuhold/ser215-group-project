import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class BattleShipFrame extends JFrame {

  private final String BATTLESHIP_LOGO_FILE = "Battleship-Logo.png";
  private final int FRAME_HEIGHT = 550;
  private final int FRAME_WIDTH = 800;

  private JPanel logoPanel;
  private JPanel gameOptionsPanel;
  private JPanel gameStatusPanel;
  private JPanel centerPanel;
  private JPanel gridsPanel;
  private JPanel playerGridPanel;
  private JPanel opponentGridPanel;
  private JPanel gameReadoutPanel;

  private JTextArea console;
  private JTextArea status;

  private JLabel battleShipLogoLabel;

  private int gameStatus = 0; //0: ships not placed, 1 : Your turn, 2: opp. turn, ect.
  private int[][] gameBoard = new int[10][10];
  private int[][] opponentBoard = new int[10][10];
  private int[][] playerBoard = new int[10][10];
  private boolean DEBUG = true;
  private int currentShip = 1; // increment every time user places a ship
  private String currentOrientation = "Vert"; // change with spacebar?

  /**
   * Builds all components for the Battleship frame and makes it visible.
   */
  public void loadBattleshipFrame() {
    // Logo Panel
    loadBattleShipLogo();
    this.add(logoPanel, BorderLayout.NORTH);

    // Game Status
    loadGameStatus();
    this.add(gameStatusPanel, BorderLayout.EAST);

    // Game Options
    loadGameOptions();
    this.add(gameOptionsPanel, BorderLayout.WEST);

    // Center panel to hold grids and readout
    centerPanel = new JPanel();
    centerPanel.setLayout(new GridLayout(2, 1));
    this.add(centerPanel, BorderLayout.CENTER);
    gridsPanel = new JPanel();
    gridsPanel.setLayout(new GridLayout(1, 2));
    centerPanel.add(gridsPanel);

    // Player Grid Panel
    loadPlayerGrid();
    gridsPanel.add(playerGridPanel);

    // Opponent Grid Panel
    loadOpponentGrid();
    placeOpponentShips();
    gridsPanel.add(opponentGridPanel);

    // Game Readout
    loadGameReadout();
    centerPanel.add(gameReadoutPanel);

    // Play Grid
    // Frame settings
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    this.setResizable(true);
    this.setVisible(true);
  }
  private void placePlayerShips() {
        /*
    num     shipType        size
    1     Carrier         5
    2     Battleship      4
    3     Cruiser         3
    4     Submarine     3
    5     Destroyer     2 */

    // Creating Player ship Objects
    ShipPiece playerBattleship = new ShipPiece(ShipType.BATTLESHIP);
    ShipPiece playerCruiser = new ShipPiece(ShipType.CRUISER);
    ShipPiece playerSubmarine = new ShipPiece(ShipType.SUBMARINE);
    ShipPiece playerDestroyer = new ShipPiece(ShipType.DESTROYER);
    ShipPiece playerCarrier = new ShipPiece(ShipType.CARRIER);

    placePlayerShip(playerBoard, playerCarrier.getSize(), 1);
    placePlayerShip(playerBoard, playerBattleship.getSize(), 2);
    placePlayerShip(playerBoard, playerCruiser.getSize(), 3);
    placePlayerShip(playerBoard, playerSubmarine.getSize(), 4);
    placePlayerShip(playerBoard, playerDestroyer.getSize(), 5);
  }

  private void placeOpponentShips() {
        /*
    num     shipType        size
    1     Carrier         5
    2     Battleship      4
    3     Cruiser         3
    4     Submarine     3
    5     Destroyer     2 */

    // Creating AI ship Objects
    ShipPiece aiBattleship = new ShipPiece(ShipType.BATTLESHIP);
    ShipPiece aiCruiser = new ShipPiece(ShipType.CRUISER);
    ShipPiece aiSubmarine = new ShipPiece(ShipType.SUBMARINE);
    ShipPiece aiDestroyer = new ShipPiece(ShipType.DESTROYER);
    ShipPiece aiCarrier = new ShipPiece(ShipType.CARRIER);

    placeShip(opponentBoard, aiCarrier.getSize(), 1);
    placeShip(opponentBoard, aiBattleship.getSize(), 2);
    placeShip(opponentBoard, aiCruiser.getSize(), 3);
    placeShip(opponentBoard, aiSubmarine.getSize(), 4);
    placeShip(opponentBoard, aiDestroyer.getSize(), 5);
  }

  private void changeOrientation() {}
  private void placePlayerShip(int[][] onBoard, int shipSize, int shipNum) {
    if (DEBUG) {
      System.out.println("Looking to place a ship of size" + shipSize);
    }
    shipNum = shipNum;
    int shipPiece = 10 * shipNum;
    boolean clear;
    int chooseRow;
    int chooseColumn;
    String orientation;
    String[] choice = {"Vert", "Horz"};
    do {
      Random gen = new Random();
      chooseColumn = 0;//gen.nextInt(10 - shipSize) + 1;
      chooseRow = 0;//gen.nextInt(10 - shipSize) + 1;
      orientation = currentOrientation;//choice[gen.nextInt(choice.length)];
      // Horizontal placement
      clear = clearPath(onBoard, shipSize, orientation, chooseColumn, chooseRow);
      if (DEBUG) {
        String msg = String.format("check for ships at pos: [%s, %s], orientation %s, test was %s",
                chooseColumn, chooseRow, orientation, clear);
        System.out.println(msg);
      }
    } while (!clear);

    if (orientation.equals("Horz")) {
      for (int itr = 0; itr < shipSize; itr++) {
        // tens place Carrier for now
        onBoard[chooseColumn][chooseRow + itr] = shipNum + itr;
      }
    } else if (orientation.equals("Vert")) {
      // vertical implementation
      for (int itr = 0; itr < shipSize; itr++) {
        onBoard[chooseColumn + itr][chooseRow] = shipNum + itr;
      }
    }
  }

  private void placeShip(int[][] onBoard, int shipSize, int shipNum) {
    if (DEBUG) {
      System.out.println("Looking to place a ship of size" + shipSize);
    }
    shipNum = 10 * shipNum;
    boolean clear;
    int chooseRow;
    int chooseColumn;
    String orientation;
    String[] choice = {"Vert", "Horz"};
    do {
      Random gen = new Random();
      chooseColumn = gen.nextInt(10 - shipSize) + 1;
      chooseRow = gen.nextInt(10 - shipSize) + 1;
      orientation = choice[gen.nextInt(choice.length)];
      // Horizontal placement
      clear = clearPath(onBoard, shipSize, orientation, chooseColumn, chooseRow);
      if (DEBUG) {
        String msg = String.format("check for ships at pos: [%s, %s], orientation %s, test was %s",
                chooseColumn, chooseRow, orientation, clear);
        System.out.println(msg);
      }
    } while (!clear);

    if (orientation.equals("Horz")) {
      for (int itr = 0; itr < shipSize; itr++) {
        // tens place Carrier for now
        onBoard[chooseColumn][chooseRow + itr] = shipNum + itr;
      }
    } else if (orientation.equals("Vert")) {
      // vertical implementation
      for (int itr = 0; itr < shipSize; itr++) {
        onBoard[chooseColumn + itr][chooseRow] = shipNum + itr;
      }
    }
  }

  private boolean clearPath(int[][] onBoard, int shipSize, String orientation, int chooseColumn, int chooseRow) {
    boolean ans = false;
    String errMsg = "Ship too long for that position\n";
    int total = 0;
    if (orientation.equals("Horz")) {
      for (int itr = 0; itr < shipSize; itr++) {
        try {
          total += onBoard[chooseColumn][chooseRow + itr];
        } catch (ArrayIndexOutOfBoundsException e) { printTo(console, errMsg);}
      }
    } else if (orientation.equals("Vert")) {
      for (int itr = 0; itr < shipSize; itr++) {
        try {
          total += onBoard[chooseColumn + itr][chooseRow];
        } catch (ArrayIndexOutOfBoundsException e) {
          printTo(console, errMsg);
          total += 9;
        }
      }
    }
    ans = total <= 0;
    return ans;
  }

  /**
   * Loads the logo from the built-in battleship logo file path.
   */
  private void loadBattleShipLogo() {
    logoPanel = new JPanel();
    BufferedImage battleShipImg = null;
    try {
      battleShipImg = ImageIO.read(getClass().getResource(BATTLESHIP_LOGO_FILE));
      battleShipLogoLabel = new JLabel(new ImageIcon(battleShipImg));
      logoPanel.add(battleShipLogoLabel);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadGameStatus() {
    gameStatusPanel = new JPanel();

    // Game Status code
    gameStatusPanel.add(new JLabel("Game Status"));
    gameStatusPanel.setBorder(new TitledBorder(new EtchedBorder()));
    status = new JTextArea("status");

    // scrollbar for the status pane
    JScrollPane statusPane = new JScrollPane(status);
    gameStatusPanel.add(statusPane);
  }

  /**
   * Creates JButtons: newGame and options
   */
  private void loadGameOptions() {
    int buttonWidth = 100;
    int buttonHeight = 20;
    gameOptionsPanel = new JPanel();
    gameOptionsPanel.setLayout(new BorderLayout());
    gameOptionsPanel.add(new JLabel("Game Options"));
    // New Game
    JButton newGame = new JButton("New Game");
    newGame.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    newGame.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
    newGame.addActionListener(e -> {
      // newGame Actions:
      printTo(console, "New Game pressed\n");
      dispose();
      BattleShipTestClass.newGame();
      // reset boards:
      for (int eachCol = 0; eachCol < opponentBoard[1].length; eachCol++) {
        for (int eachRow = 0; eachRow < opponentBoard[1].length; eachRow++) {
          opponentBoard[eachCol][eachRow] = 0;
        }
      }
    });
    gameOptionsPanel.add(newGame, BorderLayout.BEFORE_FIRST_LINE);
    // Options
    JButton options = new JButton("Options");
    options.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    options.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
    options.addActionListener(e -> {
      // Options Actions:
      printTo(console, "Options pressed\n");
    });
    gameOptionsPanel.add(options, BorderLayout.SOUTH);
    gameOptionsPanel.setBorder(new TitledBorder(new EtchedBorder()));
  }

  private void loadGameReadout() {
    gameReadoutPanel = new JPanel();

    // Scrollbar for the console
    console = new JTextArea("Console\n", 6, 40);
    JScrollPane consolePane = new JScrollPane(console);
    gameReadoutPanel.setLayout(new BorderLayout());
    gameReadoutPanel.add(consolePane, BorderLayout.CENTER);
    gameReadoutPanel.setBorder(new TitledBorder(new EtchedBorder()));
  }
  private void updatePlayerGrid(){
    String orientation = "Vert";

    Component[] btns = playerGridPanel.getComponents();
    if(DEBUG){ System.out.println("updatePlayerGrid() called "); }
    int currentSize = 1;
    String currentShipName = "";
    String imgPath = "";
    switch (currentShip){
      case 1 :
        currentSize = ShipType.CARRIER.size;
        currentShipName = ShipType.CARRIER.name;
        break;
      case 2 :
        currentSize = ShipType.BATTLESHIP.size;
        currentShipName = ShipType.BATTLESHIP.name;
        break;
      case 3 :
        currentSize = ShipType.CRUISER.size;
        currentShipName = ShipType.CRUISER.name;
        break;
      case 4 :
        currentSize = ShipType.SUBMARINE.size;
        currentShipName = ShipType.SUBMARINE.name;
        break;
      case 5 :
        currentSize = ShipType.DESTROYER.size;
        currentShipName = ShipType.DESTROYER.name;
        break;
    }
    for(int i=0; i < btns.length; i++) {
      if (btns[i] instanceof JButton) {

        JButton btn = (JButton) btns[i];
        int thisColumn = getBtnColumn(btn.getText());
        int thisRow = getBtnRow(btn.getText());
        int shipPiece = (playerBoard[thisColumn][thisRow] + 1) % 10;
        if(playerBoard[thisColumn][thisRow] > 0 && shipPiece <= currentSize) { // This line right?
          if (DEBUG) {
            System.out.println(
                    "Looking at" + btn.getText() + "[" + thisColumn + "," + thisRow + "]");
          }
          imgPath = "img/" + orientation + currentShipName + shipPiece + ".png";

          if (DEBUG) {
            System.out.println("attempting to place imgPath = " + imgPath);
          }
          try {
            BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
            btn.setIcon(new ImageIcon(img));
          } catch (Exception ex) {
            System.out.println("ImageIcon Swap Error:\t" + ex);
            // Might be out of ships increment gameStatus
            gameStatus++;
          }
        }else {btn.setBackground(Color.BLUE);}
        //square.setEnabled(false);
      }
    }

  }

  private void loadPlayerGrid() {
    playerGridPanel = new JPanel();
    GridLayout layout = new GridLayout(0, 10);
    layout.setHgap(0);
    layout.setVgap(0);
    int squareSize = 30;
    playerGridPanel.setLayout(layout);
    // Player Grid code
    int column, row;
    for (column = 0; column < 10; column++) {
      for (row = 0; row < 10; row++) {
        String boardPOS = String.format("%s%s", asChar(column), row + 1);
        JButton square = new JButton(boardPOS);
        square.setPreferredSize(new Dimension(squareSize, squareSize));
        square.setMaximumSize(new Dimension(squareSize, squareSize));
        if (playerBoard[column][row] > 0) { }
        int thisColumn = column;
        int thisRow = row;
        square.addActionListener(e -> {

          String msg = boardPOS + " Pressed\n";
          if (DEBUG){System.out.println(e.toString());}
          printTo(console, msg);
          int currentSize = 1;
          String currentShipName = "";
          String imgPath = "";
          switch (currentShip){
            case 1 :
              currentSize = ShipType.CARRIER.size;
              currentShipName = ShipType.CARRIER.name;
              break;
            case 2 :
              currentSize = ShipType.BATTLESHIP.size;
              currentShipName = ShipType.BATTLESHIP.name;
              break;
            case 3 :
              currentSize = ShipType.CRUISER.size;
              currentShipName = ShipType.CRUISER.name;
              break;
            case 4 :
              currentSize = ShipType.SUBMARINE.size;
              currentShipName = ShipType.SUBMARINE.name;
              break;
            case 5 :
              currentSize = ShipType.DESTROYER.size;
              currentShipName = ShipType.DESTROYER.name;
              break;
          }

          if (clearPath(playerBoard,currentSize,currentOrientation, thisColumn, thisRow)) {
            String orientation =currentOrientation;
            msg = String.format("%s placed at %s!\n", currentShipName, boardPOS);
            for (int i=1; i <=currentSize; i++){
              if (orientation.equals("Horz")) {
                playerBoard[getBtnColumn(square.getText())][getBtnRow(square.getText())+i] = currentShip * 10 + i;
              }else if (orientation.equals("Vert")) {
                playerBoard[getBtnColumn(square.getText())+i][getBtnRow(square.getText())] = currentShip * 10 + i;
              }

            }
            if(DEBUG) { System.out.println("playerBoard = "+Arrays.deepToString(playerBoard));}

            int shipPiece = playerBoard[thisColumn][thisRow]+1;
            imgPath = "img/"+orientation+currentShipName+shipPiece+".png";
            if (DEBUG){System.out.println("attempting to place imgPath = "+imgPath);}
            try {
              BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
              square.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
              System.out.println("ImageIcon Swap Error:\t"+ex);
            }
            square.setBackground(Color.BLACK);
            //square.setEnabled(false);
            updatePlayerGrid();
            currentShip++;
            getNextShip();

          } else if (playerBoard[thisColumn][thisRow] > 0) {
            msg = String.format("%s not available!\n", boardPOS);
            //square.setEnabled(false);
          }
        });/*
        System.out.println(square.getText()+ " = arrayValue[" +
                getBtnColumn( square.getText())+"," + getBtnRow(square.getText())+"]");*/
        playerGridPanel.add(square);

      }
    }
    playerGridPanel.setBorder(new TitledBorder(new EtchedBorder()));

  }

  private void getNextShip() {
    int currentSize = 1;
    String currentShipName = "";
    switch (currentShip){
      case 1 :
        currentSize = ShipType.CARRIER.size;
        currentShipName = ShipType.CARRIER.name;
        break;
      case 2 :
        currentSize = ShipType.BATTLESHIP.size;
        currentShipName = ShipType.BATTLESHIP.name;
        break;
      case 3 :
        currentSize = ShipType.CRUISER.size;
        currentShipName = ShipType.CRUISER.name;
        break;
      case 4 :
        currentSize = ShipType.SUBMARINE.size;
        currentShipName = ShipType.SUBMARINE.name;
        break;
      case 5 :
        currentSize = ShipType.DESTROYER.size;
        currentShipName = ShipType.DESTROYER.name;
        break;
    }
    if(currentShip <= 5)
    {
      String msg = "Where would you like to place your "+ currentShipName+" at?\n";
      printTo(console,msg);
    } else if (currentShip > 5) {printTo(console,"All ships have been placed already!");}
  }

  private void loadOpponentGrid() {
    opponentGridPanel = new JPanel();
    GridLayout layout = new GridLayout(0, 10);
    layout.setHgap(0);
    layout.setVgap(0);
    int squareSize = 30;
    opponentGridPanel.setLayout(layout);
    // Opponent Grid code
    //opponentGridPanel.add(new JLabel("Opponent Grid")); // having trouble aligning
    int column, row;
    for (column = 0; column < 10; column++) {
      for (row = 0; row < 10; row++) {
        String boardPOS = String.format("%s%s", asChar(column), row + 1);
        JButton square = new JButton(boardPOS);
        square.setPreferredSize(new Dimension(squareSize, squareSize));
        square.setMaximumSize(new Dimension(squareSize, squareSize));
        int thisColumn = column;
        int thisRow = row;
        square.addActionListener(e -> {
          // square Actions:
          String msg = boardPOS + " Pressed\n";
          printTo(console, msg);
          if (opponentBoard[thisColumn][thisRow] == 0) {
            msg = String.format("%s was a miss!\n", boardPOS);
            square.setBackground(Color.RED);
            square.setEnabled(false);
          } else if (opponentBoard[thisColumn][thisRow] > 0) {
            msg = String.format("%s was a hit!\n", boardPOS);
            square.setBackground(Color.GREEN);
            square.setEnabled(false);
          }
          //printTo(console, msg);
        });
        opponentGridPanel.add(square);
      }
    }
    opponentGridPanel.setBorder(new TitledBorder(new EtchedBorder()));
  }

  private String asChar(int val) {
    String ans = "";
    switch (val) {
      case 0:
        ans = "A";
        break;
      case 1:
        ans = "B";
        break;
      case 2:
        ans = "C";
        break;
      case 3:
        ans = "D";
        break;
      case 4:
        ans = "E";
        break;
      case 5:
        ans = "F";
        break;
      case 6:
        ans = "G";
        break;
      case 7:
        ans = "H";
        break;
      case 8:
        ans = "I";
        break;
      case 9:
        ans = "J";
        break;
    }
    return ans;
  }
  private int getBtnRow(String text) {
    String storedValue = "";
    if (text.length() == 2) {
      storedValue = text.substring(text.length() - 1, text.length());
    }else if (text.length() == 3){
      storedValue = text.substring(text.length() - 2, text.length());
    }
    int ans = Integer.parseInt(storedValue) - 1; // start counting from 0 instead of 1
    return ans;
  }
  private int getBtnColumn(String text) {
    String firstChar = text.substring(0,1);
    int ans=0;
    switch (firstChar) {
      case "A":
        ans = 0;
        break;
      case "B":
        ans = 1;
        break;
      case "C":
        ans = 2;
        break;
      case "D":
        ans = 3;
        break;
      case "E":
        ans = 4;
        break;
      case "F":
        ans = 5;
        break;
      case "G":
        ans = 6;
        break;
      case "H":
        ans = 7;
        break;
      case "I":
        ans = 8;
        break;
      case "J":
        ans = 9;
        break;
    }
    return ans;
  }

  private void printTo(JTextArea location, String msg) {
    location.append(msg);
    location.setCaretPosition(location.getDocument().getLength());
  }
}