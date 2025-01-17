import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.util.Objects;

public class BasicGame implements GameLoop {
    GameManager GameManager = new GameManager();
    boolean showHelper1 = false;
    boolean showHelper2 = false;

    public enum gameState {
        INTRO, INTRO_OVER, CHARACTER_SELECT, BATTLE, GAME_OVER
    }

    gameState state = gameState.INTRO;

    //Random index for the map
    int randomMapIndex = SaxionApp.getRandomValueBetween(0, 2);

    //Check if the players locked in
    boolean player1Choice = false;
    boolean player2Choice = false;

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

    public static boolean countdownFinished = false;

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
                GameManager.jukebox(state);

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
                GameManager.player1.drawCurrentAnimation(Variables.CS_P1X - 350, Variables.CS_P1Y - 186, currentTime);
                GameManager.player2.drawCurrentAnimation(Variables.CS_P2X - 338, Variables.CS_P2Y - 186, currentTime);

                if (player1Choice) {
                    SaxionApp.drawImage("resources/charselect/ready.png", 270, 150);
                }
                if (player2Choice) {
                    SaxionApp.drawImage("resources/charselect/ready.png", 1120, 150);
                }
                if (player1Choice && player2Choice) {
                    randomizeFighter();
                    state = gameState.BATTLE;
                }
                break;
            case BATTLE:
                Map[] maps = new Map[]{GameManager.player1.map, GameManager.player2.map};
                maps[randomMapIndex].animateMap();

                // Draw player animations
                if (GameManager.actingPlayer.equals("player 1")) {
                    GameManager.player2.drawCurrentAnimation(GameManager.player2.playerX, GameManager.player2.playerY, currentTime);
                    GameManager.player1.drawCurrentAnimation(GameManager.player1.playerX, GameManager.player1.playerY, currentTime);
                } else {
                    GameManager.player1.drawCurrentAnimation(GameManager.player1.playerX, GameManager.player1.playerY, currentTime);
                    GameManager.player2.drawCurrentAnimation(GameManager.player2.playerX, GameManager.player2.playerY, currentTime);
                }

                if (GameManager.player1.moveChoice) {
                    SaxionApp.drawImage("resources/charselect/ready.png", 240, 70);
                }
                if (GameManager.player2.moveChoice) {
                    SaxionApp.drawImage("resources/charselect/ready.png", 1150, 70);
                }


                if (GameManager.player1.moveChoice && GameManager.player2.moveChoice) {
                    GameManager.inRound = true;
                    GameManager.player1.characterDash();
                    GameManager.player2.characterDash();
                    GameManager.playRound();
                }

                if (GameManager.player1.isStunned) {
                    GameManager.player1.playStunnedAnimation();
                } else if (GameManager.player2.isStunned) {
                    GameManager.player2.playStunnedAnimation();
                }

                if (showHelper1) {
                    GameManager.player1.drawDescription();
                } else {
                    GameManager.player1.drawIcons();
                }

                if (showHelper2) {
                    GameManager.player2.drawDescription();
                } else {
                    GameManager.player2.drawIcons();
                }

                if (GameManager.hpCheck(GameManager.player1, GameManager.player2)) {
                    state = gameState.GAME_OVER;
                }

                if (GameManager.player1.hp <= 0) {
                    GameManager.player1.state = "dead";
                    GameManager.player1.setAnimation("dead");
                }

                if (GameManager.player2.hp <= 0) {
                    GameManager.player2.state = "dead";
                    GameManager.player2.setAnimation("dead");
                }

                SaxionApp.drawImage("resources/battle/hpbar1.png", Variables.xPositionP1, 50, 470, 138);
                GameManager.player1.drawHpBar();
                GameManager.player1.drawSpBar();
                SaxionApp.drawImage("resources/battle/hpbar2.png", Variables.xPositionP2, 50, 470, 138);
                GameManager.player2.drawHpBar();
                GameManager.player2.drawSpBar();
                GameManager.displayBattleProfiles(GameManager.player1, GameManager.player2);

                if (!countdownFinished) {
                    GameManager.drawCountdown();
                }
                GameManager.victoryScreen();

                break;
            case GAME_OVER:
                SaxionApp.sleep(5);
                player1Choice = false;
                player2Choice = false;
                countdownFinished = false;
                GameManager.resetGame();
                state = gameState.CHARACTER_SELECT;
        }


    }

    public void animateIntroBackground(long currentTime) {
        // Looping for background
        if (currentTime - lastBgFrameTime >= bgIntroDelay[bgFrameIndex] * 1000) {
            bgFrameIndex = (bgFrameIndex + 1) % introBackground.length;
            lastBgFrameTime = currentTime;

            // Plays lightning sfx at set frames
            if (state == gameState.INTRO_OVER && (bgFrameIndex == 8 || bgFrameIndex == 22 || bgFrameIndex == 29 || bgFrameIndex == 42 || bgFrameIndex == 55)) {
                GameManager.playSound("resources/sfx/shock.wav", 0.5f);
            }
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

            switch (charFrameIndex) {
                case 1:
                    GameManager.playSound("resources/sfx/chain_loop.wav", 1);
                    break;
                case 5, 9, 13, 17, 21: // lightning dash sfx
                    GameManager.playSound("resources/sfx/lightning_loop.wav", 1);
                    break;
                case 8, 12, 16, 20, 24: // slash sfx
                    GameManager.playSound("resources/sfx/hit_shock.wav", 1);
                    break;
                case 29: // big lightning sfx
                    GameManager.playSound("resources/sfx/thunder.wav", 1);
                    break;
                case 28: // chain lightning sfx
                    GameManager.playSound("resources/sfx/thunder_ripple.wav", 1);
                    break;
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

    public void randomizeFighter() {
        if (Variables.p1choice == 3) {
            Variables.p1choice = SaxionApp.getRandomValueBetween(0, 3);
            GameManager.player1.updatePlayer(GameManager.fighters[Variables.p1choice]);
        }
        if (Variables.p2choice == 3) {
            Variables.p2choice = SaxionApp.getRandomValueBetween(0, 3);
            GameManager.player2.updatePlayer(GameManager.fighters[Variables.p2choice]);
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
                if (!player1Choice) {
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_D && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p1choice += 1;
                        Variables.csCursorP1X += 150;
                        if (Variables.csCursorP1X > 775) {
                            Variables.p1choice -= 2;
                            Variables.csCursorP1X = 625;
                        }
                        GameManager.player1.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_A && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p1choice -= 1;
                        Variables.csCursorP1X -= 150;
                        if (Variables.csCursorP1X < 600) {
                            Variables.p1choice += 2;
                            Variables.csCursorP1X = 775;
                        }
                        GameManager.player1.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_W && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p1choice -= 2;
                        Variables.csCursorP1Y -= 150;
                        if (Variables.csCursorP1Y < 340) {
                            Variables.p1choice += 4;
                            Variables.csCursorP1Y = 490;
                        }
                        GameManager.player1.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_S && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p1choice += 2;
                        Variables.csCursorP1Y += 150;
                        if (Variables.csCursorP1Y > 490) {
                            Variables.p1choice -= 4;
                            Variables.csCursorP1Y = 340;
                        }
                        GameManager.player1.resetIndexes();
                    }
                }

                // Player 2 controls
                if (!player2Choice) {
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_RIGHT && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p2choice += 1;
                        Variables.csCursorP2X += 150;
                        if (Variables.csCursorP2X > 775) {
                            Variables.p2choice -= 2;
                            Variables.csCursorP2X = 625;
                        }
                        GameManager.player2.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_LEFT && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p2choice -= 1;
                        Variables.csCursorP2X -= 150;
                        if (Variables.csCursorP2X < 600) {
                            Variables.p2choice += 2;
                            Variables.csCursorP2X = 775;
                        }
                        GameManager.player2.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_UP && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p2choice -= 2;
                        Variables.csCursorP2Y -= 150;
                        if (Variables.csCursorP2Y < 340) {
                            Variables.p2choice += 4;
                            Variables.csCursorP2Y = 490;
                        }
                        GameManager.player2.resetIndexes();
                    } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_DOWN && keyboardEvent.isKeyPressed()) {
                        GameManager.playSound(Variables.PATH + "cursor.wav", 1);
                        Variables.p2choice += 2;
                        Variables.csCursorP2Y += 150;
                        if (Variables.csCursorP2Y > 490) {
                            Variables.p2choice -= 4;
                            Variables.csCursorP2Y = 340;
                        }
                        GameManager.player2.resetIndexes();
                    }
                }

                GameManager.updatePlayer(GameManager.player1, GameManager.fighters[Variables.p1choice]);
                GameManager.updatePlayer(GameManager.player2, GameManager.fighters[Variables.p2choice]);

                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_SPACE && keyboardEvent.isKeyPressed()) {
                    player1Choice = true;
                }
                if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_ENTER && keyboardEvent.isKeyPressed()) {
                    player2Choice = true;
                }
                break;
            case BATTLE:
                if (keyboardEvent.isKeyPressed() && countdownFinished) {
                    // Player 1 Keys
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_W && !GameManager.inRound) {
                        GameManager.player1.moveChoice = true;
                        GameManager.player1.playerAction = "attack";
                        GameManager.player1.attackModifier = 1.0;
                        GameManager.player1.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_A && !GameManager.inRound) {
                        GameManager.player1.moveChoice = true;
                        GameManager.player1.playerAction = "sattack";
                        GameManager.player1.attackModifier = 1.5;
                        GameManager.player1.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_S && !GameManager.inRound && GameManager.player1.sp >= 10) {
                        GameManager.player1.moveChoice = true;
                        GameManager.player1.playerAction = "special";
                        GameManager.player1.attackModifier = 2.0;
                        GameManager.player1.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_D && !GameManager.inRound && GameManager.player1.sp >= 30) {
                        GameManager.player1.moveChoice = true;
                        GameManager.player1.playerAction = "ultimate";
                        GameManager.player1.attackModifier = 3.0;
                        GameManager.player1.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_Q && !GameManager.inRound) {
                        GameManager.player1.moveChoice = true;
                        GameManager.player1.playerAction = "defend";
                        GameManager.player1.acting = true;
                    }

                    // Player 2 Keys
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_UP && !GameManager.inRound) {
                        GameManager.player2.moveChoice = true;
                        GameManager.player2.playerAction = "attack";
                        GameManager.player2.attackModifier = 1.0;
                        GameManager.player2.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_LEFT && !GameManager.inRound) {
                        GameManager.player2.moveChoice = true;
                        GameManager.player2.playerAction = "sattack";
                        GameManager.player2.attackModifier = 1.5;
                        GameManager.player2.acting = true;
                    }

                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_DOWN && !GameManager.inRound && GameManager.player2.sp >= 10) {
                        GameManager.player2.moveChoice = true;
                        GameManager.player2.playerAction = "special";
                        GameManager.player2.attackModifier = 2.0;
                        GameManager.player2.acting = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_RIGHT && !GameManager.inRound && GameManager.player2.sp >= 30) {
                        GameManager.player2.moveChoice = true;
                        GameManager.player2.playerAction = "ultimate";
                        GameManager.player2.attackModifier = 3.0;
                        GameManager.player2.acting = true;
                    }
                    if ((keyboardEvent.getKeyCode() == KeyboardEvent.VK_0 || keyboardEvent.getKeyCode() == KeyboardEvent.VK_NUMPAD0) && !GameManager.inRound) {
                        GameManager.player2.moveChoice = true;
                        GameManager.player2.playerAction = "defend";
                        GameManager.player2.acting = true;
                    }

                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_SHIFT && !GameManager.inRound) {
                        showHelper1 = true;
                    }
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_BACK_SLASH && !GameManager.inRound) {
                        showHelper2 = true;
                    }
                } else {
                    if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_SHIFT || keyboardEvent.getKeyCode() == KeyboardEvent.VK_BACK_SLASH) {
                        showHelper1 = false;
                        showHelper2 = false;
                    }
                }


                break;

        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}

