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

    public GameManager() {
        //load fighter choices;

    }

    public void jukebox(BasicGame.gameState state) {
        if (!musicPlaying) {
            new Thread(() -> {
                switch (state) {
                    case INTRO:
                        playSong("resources/m1.wav", true);
                        break;
                    case CHARACTER_SELECT:
                        break;
                }
            }).start();
            musicPlaying = true;
        }
    }

    public void playSong(String song, boolean loop) {
        musicPlayer.setFile(song);
        musicPlayer.setLoop(loop);
        musicPlayer.play();
    }

    public void playSound(String sound) {
        MediaPlayer soundPlayer = new MediaPlayer(sound, false);
        new Thread(soundPlayer::play).start();
    }

    public void chooseFighter(int player, Fighter fighter) {

    }

    public Fighter getFighter(String name) {
        for (Fighter fighter : fighters) {
            if (fighter.name.equalsIgnoreCase(name)){
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
        player1.name = "player 1";
        player2.name = "player 2";
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
        if (player.name.equalsIgnoreCase("player 1")) {
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

    public void selectCharacter(Player player, boolean player1, boolean player2) {
        int x = (player.name.equalsIgnoreCase("player 1")) ? Variables.csCursorP1X : Variables.csCursorP2X;
        int y = (player.name.equalsIgnoreCase("player 1")) ? Variables.csCursorP1Y : Variables.csCursorP2Y;

        if (x == 625 && y == 340) {
            player.updatePlayer(newFighters[0]);
        } else if (x == 775 && y == 340) {
            player.updatePlayer(newFighters[1]);
        } else if (x == 625 && y == 490) {
            player.updatePlayer(newFighters[2]);
        } else if (x == 775 && y == 490) {
            int randomFighter = SaxionApp.getRandomValueBetween(0, newFighters.length);
            player.updatePlayer(newFighters[randomFighter]);
        }

    }
}
