import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class BasicGame implements GameLoop {
    GameManager GameManager = new GameManager();

    public enum gameState {
        INTRO, INTRO_OVER, CHARACTER_SELECT, BATTLE
    }

    gameState state = gameState.CHARACTER_SELECT;

    //Variables for intro text
    boolean isTitleShown = false;
    boolean isIntroComplete = false;
    long titleShownTime = 0;

    // Creates intro background and character
    String[] introBackground = new String[64];
    double[] bgIntroDelay = {0.12, 0.24, 0.16, 0.2, 0.2, 0.2, 0.2, 0.2, 0.08, 0.04, 0.04, 0.04, 0.2, 0.2, 0.24, 0.16, 0.2, 0.2, 0.2, 0.2, 0.24, 0.16, 0.08, 0.04, 0.04, 0.04, 0.24, 0.16, 0.16, 0.08, 0.04, 0.04, 0.04, 0.04, 0.2, 0.2, 0.16, 0.2, 0.2, 0.16, 0.2, 0.24, 0.04, 0.04, 0.04, 0.04, 0.16, 0.2, 0.2, 0.2, 0.2, 0.28, 0.16, 0.16, 0.08, 0.04, 0.04, 0.04, 0.04, 0.16, 0.24, 0.2, 0.2, 0.08};
    String[] introCharacter = new String[49];

    // Current animation array index variables
    int bgFrameIndex = 0;
    int charFrameIndex = 0;

    // Tracks time at which last frame played for the intro animation
    long lastBgFrameTime = 0;
    long lastCharFrameTime = 0;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1600, 672, 40);

    }

    @Override
    public void init() {

        // Fill intro background animation array
        for (int i = 0; i < introBackground.length; i++) {
            introBackground[i] = Variables.PATH + "intro/frame" + i + ".gif";
        }

        // Fill intro character animation array
        for (int i = 0; i < introCharacter.length; i++) {
            introCharacter[i] = Variables.PATH + "intro/intro" + i + ".gif";
        }

        GameManager.initMaps();
        GameManager.initFighters();
        GameManager.initPlayers();

    }


    @Override
    public void loop() {
        SaxionApp.clear();
        long currentTime = System.currentTimeMillis();

        switch (state) {
            case INTRO:
                animateIntroBackground(currentTime);
                showIntroAnimation(currentTime);

                break;
            case INTRO_OVER:
                // Looping for background
                animateIntroBackground(currentTime);
                showIntroAnimation(currentTime);
                displayTitle();
                GameManager.updatePlayer(GameManager.player1, GameManager.fighters[Variables.p1choice]);
                GameManager.updatePlayer(GameManager.player2, GameManager.fighters[Variables.p2choice]);
                break;
            case CHARACTER_SELECT:
                // DELETE NEXT 2 LINES BEFORE SHIPPING
                GameManager.updatePlayer(GameManager.player1, GameManager.fighters[Variables.p1choice]);
                GameManager.updatePlayer(GameManager.player2, GameManager.fighters[Variables.p2choice]);

                GameManager.drawCharSelectScreen();
                GameManager.displayAllStats(GameManager.player1);
                GameManager.displayAllStats(GameManager.player2);
                GameManager.player1.loopAnimation(GameManager.player1.idle, Variables.CS_P1X - 350, Variables.CS_P1Y - 186);
                GameManager.player2.loopAnimation(GameManager.player2.idle, Variables.CS_P2X - 338, Variables.CS_P2Y - 186);

                state = gameState.BATTLE;
                break;
            case BATTLE:
                if (GameManager.player1.state.equalsIgnoreCase("idle")) {
                    GameManager.player1.loopAnimation(GameManager.player1.idle, 250, 250);
                }

                break;
        }

        GameManager.jukebox(state);


    }

    public void animateIntroBackground(long currentTime) {
        // Looping for background
        if (currentTime - lastBgFrameTime >= bgIntroDelay[bgFrameIndex] * 1000) {
            bgFrameIndex = (bgFrameIndex + 1) % introBackground.length;
            lastBgFrameTime = currentTime;
        }

        SaxionApp.drawImage(introBackground[bgFrameIndex], 0, 0, 1600, 672);
    }

    public void showIntroAnimation(long currentTime) {

        // Stops character animation at final frame
        if (currentTime - lastCharFrameTime >= 50) {
            charFrameIndex = (charFrameIndex + 1);
            lastCharFrameTime = currentTime;

            if (charFrameIndex >= 48) {
                charFrameIndex = 48;
                state = gameState.INTRO_OVER;
            }
        }

        SaxionApp.drawImage(introCharacter[charFrameIndex], 320, -50);
    }

    public void displayTitle() {
        // Draw the title after the intro is complete
        SaxionApp.drawImage(Variables.PATH + "intro/title.png", 300, 50, 950, 192);
        if (!isTitleShown) {
            isTitleShown = true;
            titleShownTime = System.currentTimeMillis();
        }

        // Delay for the "press enter to start" text
        if (System.currentTimeMillis() - titleShownTime >= 1000) {
            SaxionApp.drawImage(Variables.PATH + "intro/press_enter.png", 370, 450, 800, 132);
            isIntroComplete = true;
        }
    }


    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        switch (state) {
            case INTRO_OVER:
                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_ENTER && isIntroComplete) {
                    state = gameState.CHARACTER_SELECT;
                }
                break;
            case CHARACTER_SELECT:
                // Player 1 controls
                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_D && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p1choice += 1;
                    Variables.csCursorP1X += 150;
                    if (Variables.csCursorP1X > 775) {
                        Variables.p1choice -= 2;
                        Variables.csCursorP1X = 625;
                    }
                    GameManager.player1.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_A && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p1choice -= 1;
                    Variables.csCursorP1X -= 150;
                    if (Variables.csCursorP1X < 600) {
                        Variables.p1choice += 2;
                        Variables.csCursorP1X = 775;
                    }
                    GameManager.player1.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_W && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p1choice -= 2;
                    Variables.csCursorP1Y -= 150;
                    if (Variables.csCursorP1Y < 340) {
                        Variables.p1choice += 4;
                        Variables.csCursorP1Y = 490;
                    }
                    GameManager.player1.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_S && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p1choice += 2;
                    Variables.csCursorP1Y += 150;
                    if (Variables.csCursorP1Y > 490) {
                        Variables.p1choice -= 4;
                        Variables.csCursorP1Y = 340;
                    }
                    GameManager.player1.resetIndexes();
                }

                // Player 2 controls
                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_RIGHT && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p2choice += 1;
                    Variables.csCursorP2X += 150;
                    if (Variables.csCursorP2X > 775) {
                        Variables.p2choice -= 2;
                        Variables.csCursorP2X = 625;
                    }
                    GameManager.player2.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_LEFT && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p2choice -= 1;
                    Variables.csCursorP2X -= 150;
                    if (Variables.csCursorP2X < 600) {
                        Variables.p2choice += 2;
                        Variables.csCursorP2X = 775;
                    }
                    GameManager.player2.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_UP && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p2choice -= 2;
                    Variables.csCursorP2Y -= 150;
                    if (Variables.csCursorP2Y < 340) {
                        Variables.p2choice += 4;
                        Variables.csCursorP2Y = 490;
                    }
                    GameManager.player2.resetIndexes();
                } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_DOWN && keyboardEvent.isKeyPressed()) {
                    GameManager.playSound(Variables.PATH + "cursor.wav");
                    Variables.p2choice += 2;
                    Variables.csCursorP2Y += 150;
                    if (Variables.csCursorP2Y > 490) {
                        Variables.p2choice -= 4;
                        Variables.csCursorP2Y = 340;
                    }
                    GameManager.player2.resetIndexes();
                }
                GameManager.updatePlayer(GameManager.player1, GameManager.fighters[Variables.p1choice]);
                GameManager.updatePlayer(GameManager.player2, GameManager.fighters[Variables.p2choice]);

                break;
            case BATTLE:
                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_W && keyboardEvent.isKeyPressed()) {
                    new Thread(() -> {
                        GameManager.player1.state = "attack";
                        GameManager.player1.playAnimation(GameManager.player1.attack, 250, 250, "");
                    }).start();
                }

        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}