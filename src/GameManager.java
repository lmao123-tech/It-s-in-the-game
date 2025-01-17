import nl.saxion.app.SaxionApp;
import nl.saxion.app.audio.MediaPlayer;

import java.util.ArrayList;


public class GameManager {
    // Creates backgrounds
    Map mapFire = new Map();
    Map mapLightning = new Map();
    Map mapWater = new Map();

    // Creates fighters
    Fighter fire = new Fighter();
    Fighter water = new Fighter();
    Fighter lightning = new Fighter();
    Fighter random = new Fighter();

    // Creates players
    Player player1 = new Player();
    Player player2 = new Player();

    // Creates an array of fighters that players select from on the Character Select screen
    ArrayList<Fighter> fighterChoices = new ArrayList();
    Fighter[] fighters = new Fighter[4];
    Fighter[] newFighters = new Fighter[3];

    // Creates the music player
    MediaPlayer musicPlayer = new MediaPlayer("intro.mp3", true);
    boolean musicPlaying = false;

    boolean damageCalculated = true;
    public String actingPlayer = "";
    boolean inRound = false;

    public GameManager() {
        //load fighter choices;

    }

    public void jukebox(BasicGame.gameState state) {
        int randomSong = SaxionApp.getRandomValueBetween(1, 7);
        if (!musicPlaying) {
            new Thread(() -> {
                switch (state) {
                    case INTRO:
//                        playSong("resources/m1.wav", true);
                        break;
                    case INTRO_OVER:
                        playSong("resources/m" + randomSong + ".wav", true);
                        break;
                    case CHARACTER_SELECT:
                        break;
                    case BATTLE:
                        break;
                }
            }).start();
            musicPlaying = true;
        }
    }

    public void playSong(String song, boolean loop) {
        musicPlayer.stop();
        musicPlayer.setFile(song);
        musicPlayer.setLoop(loop);
        musicPlayer.play();
    }

    public void playSound(String sound, float volume) {
        MediaPlayer soundPlayer = new MediaPlayer(sound, false);
        soundPlayer.setVolume(volume);
        new Thread(soundPlayer::play).start();
    }

    public void chooseFighter(int player, Fighter fighter) {

    }

    public Fighter getFighter(String name) {
        for (Fighter fighter : fighters) {
            if (fighter.name.equalsIgnoreCase(name)) {
                return fighter;
            }
        }
        return null;
    }

    public void startFight() {

    }

    public void setPlayer1Fighter() {

    }

    public void draw() {
        for (int i = 0; i < fighters.length; i++) {
//            fighters[i].draw();
        }
    }

    public void initFighters() {
        // Sets path of images for each fighter
        fire.imagePath = "resources/fire/";
        water.imagePath = "resources/water/";
        lightning.imagePath = "resources/lightning/";


        fire.name = "Fire";
        water.name = "Water";
        lightning.name = "Lightning";
        random.name = "Random";

        fire.setMap(mapFire);
        water.setMap(mapWater);
        lightning.setMap(mapLightning);

        fire.initFighter();
        water.initFighter();
        lightning.initFighter();

        random.idleR.add(Variables.PATH_CS + "randomcharacterr.png");
        random.idleL.add(Variables.PATH_CS + "randomcharacterl.png");

        fighters[0] = fire;
        fighters[1] = water;
        fighters[2] = lightning;
        fighters[3] = random;

        newFighters[0] = fire;
        newFighters[1] = water;
        newFighters[2] = lightning;
    }

    public void initMaps() {
        mapFire.createBackgroundArray("fire", 1, 8);
        mapLightning.createBackgroundArray("lightning", 2, 16);
        mapWater.createBackgroundArray("water", 3, 8);
    }

    public void initPlayers() {
        player1.name = "player1";
        player2.name = "player2";

        player1.setBattleCoords();
        player2.setBattleCoords();
    }

    public void updatePlayer(Player player, Fighter fighter) {
        player.updatePlayer(fighter);
    }

    public void drawCharSelectScreen() {
        SaxionApp.drawImage(Variables.PATH_CS + "char_select.jpg", 0, 0, 1600, 672);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_red.png", Variables.CS_P1X, Variables.CS_P1Y);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_blue.png", Variables.CS_P2X, Variables.CS_P2Y);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_fire.png", Variables.CS_P2X - 475, Variables.CS_P1Y + 140);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_water.png", Variables.CS_P2X - 325, Variables.CS_P2Y + 140);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_lightning.png", Variables.CS_P2X - 475, Variables.CS_P1Y + 290);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_random.png", Variables.CS_P2X - 325, Variables.CS_P2Y + 290);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_p1.png", Variables.csCursorP1X, Variables.csCursorP1Y);
        SaxionApp.drawImage(Variables.PATH_CS + "frame_p2.png", Variables.csCursorP2X, Variables.csCursorP2Y);
        SaxionApp.drawImage(Variables.PATH_CS + "cs_topbar.png", 0, 0, 1600, 128);
        SaxionApp.drawImage(Variables.PATH_CS + "label_charselect.png", 544, 20);
        SaxionApp.drawImage(Variables.PATH_CS + "label_p1.png", Variables.CS_P1X - 40, Variables.CS_P1Y - 120);
        SaxionApp.drawImage(Variables.PATH_CS + "label_p2.png", Variables.CS_P2X - 40, Variables.CS_P2Y - 120);
        SaxionApp.drawImage(Variables.PATH_CS + "cs_statbg.png", Variables.CS_P1X - 89, Variables.CS_P1Y + 280);
        SaxionApp.drawImage(Variables.PATH_CS + "cs_statbg.png", Variables.CS_P2X - 89, Variables.CS_P2Y + 280);
        SaxionApp.drawImage(Variables.PATH_CS + "cs_keysp1.png", 5, 610);
        SaxionApp.drawImage(Variables.PATH_CS + "cs_keysp2.png", 1370, 610);

        SaxionApp.drawImage(Variables.PATH_CS + "label_hp.png", Variables.CS_P1X - 70, Variables.CS_P1Y + 300);
        SaxionApp.drawImage(Variables.PATH_CS + "label_hp.png", Variables.CS_P2X - 70, Variables.CS_P2Y + 300);
        SaxionApp.drawImage(Variables.PATH_CS + "label_sp.png", Variables.CS_P1X + 95, Variables.CS_P1Y + 300);
        SaxionApp.drawImage(Variables.PATH_CS + "label_sp.png", Variables.CS_P2X + 95, Variables.CS_P2Y + 300);
        SaxionApp.drawImage(Variables.PATH_CS + "label_atk.png", Variables.CS_P1X - 70, Variables.CS_P1Y + 330);
        SaxionApp.drawImage(Variables.PATH_CS + "label_atk.png", Variables.CS_P2X - 70, Variables.CS_P2Y + 330);
        SaxionApp.drawImage(Variables.PATH_CS + "label_satk.png", Variables.CS_P1X + 95, Variables.CS_P1Y + 330);
        SaxionApp.drawImage(Variables.PATH_CS + "label_satk.png", Variables.CS_P2X + 95, Variables.CS_P2Y + 330);
        SaxionApp.drawImage(Variables.PATH_CS + "label_def.png", Variables.CS_P1X - 70, Variables.CS_P1Y + 360);
        SaxionApp.drawImage(Variables.PATH_CS + "label_def.png", Variables.CS_P2X - 70, Variables.CS_P2Y + 360);
        SaxionApp.drawImage(Variables.PATH_CS + "label_sdef.png", Variables.CS_P1X + 95, Variables.CS_P1Y + 360);
        SaxionApp.drawImage(Variables.PATH_CS + "label_sdef.png", Variables.CS_P2X + 95, Variables.CS_P2Y + 360);
        SaxionApp.drawImage(Variables.PATH_CS + "label_spd.png", Variables.CS_P1X - 70, Variables.CS_P1Y + 390);
        SaxionApp.drawImage(Variables.PATH_CS + "label_spd.png", Variables.CS_P2X - 70, Variables.CS_P2Y + 390);
    }

    public void displayAllStats(Player player) {
        if (player.name.equalsIgnoreCase("player1")) {
            displayStat(player.hp, Variables.CS_P1X, Variables.CS_P1Y + 300);
            displayStat(player.sp, Variables.CS_P1X + 180, Variables.CS_P1Y + 300);
            displayStat(player.atk, Variables.CS_P1X, Variables.CS_P1Y + 330);
            displayStat(player.satk, Variables.CS_P1X + 180, Variables.CS_P1Y + 330);
            displayStat(player.def, Variables.CS_P1X, Variables.CS_P1Y + 360);
            displayStat(player.sdef, Variables.CS_P1X + 180, Variables.CS_P1Y + 360);
            displayStat(player.spd, Variables.CS_P1X, Variables.CS_P1Y + 390);
        } else {
            displayStat(player.hp, Variables.CS_P2X, Variables.CS_P2Y + 300);
            displayStat(player.sp, Variables.CS_P2X + 180, Variables.CS_P2Y + 300);
            displayStat(player.atk, Variables.CS_P2X, Variables.CS_P2Y + 330);
            displayStat(player.satk, Variables.CS_P2X + 180, Variables.CS_P2Y + 330);
            displayStat(player.def, Variables.CS_P2X, Variables.CS_P2Y + 360);
            displayStat(player.sdef, Variables.CS_P2X + 180, Variables.CS_P2Y + 360);
            displayStat(player.spd, Variables.CS_P2X, Variables.CS_P2Y + 390);
        }

    }

    public void displayBattleProfiles(Player player1, Player player2) {
        switch (player1.maxHp) {
            case 500:
                SaxionApp.drawImage("resources/battle/profile_fire1.png", Variables.xPositionP1, 50, 470, 138);
                break;
            case 360:
                SaxionApp.drawImage("resources/battle/profile_lightning1.png", Variables.xPositionP1, 50, 470, 138);
                break;
            case 410:
                SaxionApp.drawImage("resources/battle/profile_water1.png", Variables.xPositionP1, 50, 470, 138);
                break;
        }

        switch (player2.maxHp) {
            case 500:
                SaxionApp.drawImage("resources/battle/profile_fire2.png", Variables.xPositionP2, 50, 470, 138);
                break;
            case 360:
                SaxionApp.drawImage("resources/battle/profile_lightning2.png", Variables.xPositionP2, 50, 470, 138);
                break;
            case 410:
                SaxionApp.drawImage("resources/battle/profile_water2.png", Variables.xPositionP2, 50, 470, 138);

                break;
        }
    }

    public void displayStat(int stat, int x, int y) {
        if (stat > 0) {
            int hundreds;
            int tens;
            int ones;
            int temp = stat;

            ones = temp % 10;
            temp = temp / 10;

            tens = temp % 10;
            temp = temp / 10;

            hundreds = temp % 10;

            if (hundreds != 0) {
                SaxionApp.drawImage(Variables.PATH_CS + hundreds + ".png", x, y);
            }
            SaxionApp.drawImage(Variables.PATH_CS + tens + ".png", x + 18, y);
            SaxionApp.drawImage(Variables.PATH_CS + ones + ".png", x + 36, y);
        } else {
            SaxionApp.drawImage(Variables.PATH_CS + "unknown.png", x, y);
            SaxionApp.drawImage(Variables.PATH_CS + "unknown.png", x + 18, y);
            SaxionApp.drawImage(Variables.PATH_CS + "unknown.png", x + 36, y);
        }
    }

    int descent = -275;
    int printedNumber = 3;

    public void drawCountdown() {
        if (printedNumber == 0) {
            SaxionApp.drawImage(Variables.PATH + "battle/Fight!!.png", 400, descent);
        } else {
            SaxionApp.drawImage(Variables.PATH + "battle/" + printedNumber + "_Countdown.png", 625, descent);
        }

        descent += 45;
        if (descent >= 200) {
            printedNumber--;
            descent = -275;
            if (printedNumber < 0) {
                BasicGame.countdownFinished = true;
            }
        }
    }

    public void takeDamage(Player defendingPlayer, Player attackingPlayer, double modifier) {
        defendingPlayer.hp = defendingPlayer.hp - defendingPlayer.dmgTaken((int) (attackingPlayer.atk * modifier), defendingPlayer.def, false);

    }

    public int determineTurnOrder(Player player1, Player player2) {
        do {
            player1.modifiedSpeed = player1.spd + SaxionApp.getRandomValueBetween(-10, 11);
            player2.modifiedSpeed = player2.spd + SaxionApp.getRandomValueBetween(-10, 11);
        } while (player1.modifiedSpeed == player2.modifiedSpeed);

        if (player1.playerAction.equals("attack") && player2.playerAction.equals("defend")) {
            return 1;
        } else if (player1.playerAction.equals("defend") && player2.playerAction.equals("attack")) {
            return 2;
        } else if (player1.playerAction.equals("defend")) {
            return 1;
        } else if (player2.playerAction.equals("defend")) {
            return 2;
        }

        return player1.modifiedSpeed > player2.modifiedSpeed ? 1 : 2;
    }

    public boolean checkIfStunned(int firstPlayerSpeed, int secondPlayerSpeed) {
        double speedDifferential = 1.2 * (firstPlayerSpeed - secondPlayerSpeed);

        return SaxionApp.getRandomValueBetween(0, 101) <= speedDifferential;
    }

    public void playRound() {
            new Thread(() -> {
                player1.moveChoice = false;
                player2.moveChoice = false;

                SaxionApp.sleep(1);

                int turnOrder = determineTurnOrder(player1, player2);
                double delay = 0.1;

                player1.currentAnimationArray = player1.setCurrentAnimationFrames(player1.playerAction);
                player2.currentAnimationArray = player2.setCurrentAnimationFrames(player2.playerAction);

                if (player1.playerAction.equals("defend") && player2.playerAction.equals("defend")) {
                    player1.setAnimation(player1.playerAction);
                    player1.state = player1.playerAction;
                    player1.updateSp(player1.playerAction);


                    player2.setAnimation(player2.playerAction);
                    player2.state = player2.playerAction;
                    player2.updateSp(player2.playerAction);

                    SaxionApp.sleep(1);
                } else {
                    if (turnOrder == 1) {
                        actingPlayer = "player 1";
                        player1.setAnimation(player1.playerAction);
                        player1.state = player1.playerAction;
                        player1.updateSp(player1.playerAction);

                        if (!player1.playerAction.equals("defend")) {
                            player2.playHitAnimation();
                        }

                        checkIfStunned(player1.modifiedSpeed, player2.modifiedSpeed);

                        SaxionApp.sleep(delay * player1.currentAnimationArray.size());
                        takeDamage(player2, player1, player1.attackModifier);


                        if (player2.playerAction.equals("defend") && !player1.playerAction.equals("defend")) {
                            player2.isStunned = true;
                            SaxionApp.sleep(2.5);

                        } else if (!checkIfStunned(player1.modifiedSpeed, player2.modifiedSpeed)) {
                            actingPlayer = "player 2";
                            player2.setAnimation(player2.playerAction);
                            player2.state = player2.playerAction;
                            player2.updateSp(player2.playerAction);

                            player1.playHitAnimation();

                            SaxionApp.sleep(delay * player2.currentAnimationArray.size());
                            takeDamage(player1, player2, player2.attackModifier);

                        } else {
                            player2.isStunned = true;
                            SaxionApp.sleep(2.5);

                        }

                    } else {
                        actingPlayer = "player 2";
                        player2.setAnimation(player2.playerAction);
                        player2.state = player2.playerAction;
                        player2.updateSp(player2.playerAction);


                        if (!player2.playerAction.equals("defend")) {
                            player1.playHitAnimation();
                        }
                        checkIfStunned(player2.modifiedSpeed, player1.modifiedSpeed);


                        SaxionApp.sleep(delay * player2.currentAnimationArray.size());
                        takeDamage(player1, player2, player2.attackModifier);


                        if (player1.playerAction.equals("defend") && !player2.playerAction.equals("defend")) {
                            player1.isStunned = true;
                            SaxionApp.sleep(2.5);

                        } else if (!checkIfStunned(player2.modifiedSpeed, player1.modifiedSpeed)) {
                            actingPlayer = "player 1";
                            player1.setAnimation(player1.playerAction);
                            player1.state = player1.playerAction;
                            player1.updateSp(player1.playerAction);

                            player2.playHitAnimation();

                            SaxionApp.sleep(delay * player1.currentAnimationArray.size());
                            takeDamage(player2, player1, player1.attackModifier);

                        } else {
                            player1.isStunned = true;
                            SaxionApp.sleep(2.5);

                        }

                    }
                }

                player1.characterDashBack();
                player2.characterDashBack();

                SaxionApp.sleep(1);


                inRound = false;

            }).start();
    }

    public void victoryScreen() {
        if (player1.hp <= 0) {
            SaxionApp.drawImage("resources/battle/Player-2-wins.png", 250, 300);
        }
        if (player2.hp <= 0) {
            SaxionApp.drawImage("resources/battle/Player-1-wins.png", 250, 300);
        }
    }



    public boolean hpCheck(Player player1, Player player2) {
        return (player1.hp <= 0.0) || (player2.hp <= 0.0);
    }

    public void resetGame() {
        Variables.p1choice = 0;
        Variables.p2choice = 1;

        printedNumber = 3;

        Variables.csCursorP1X = 625;
        Variables.csCursorP1Y = 340;
        Variables.csCursorP2X = 775;
        Variables.csCursorP2Y = 340;
    }


}
