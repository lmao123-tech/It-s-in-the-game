import nl.saxion.app.SaxionApp;
import nl.saxion.app.audio.MediaPlayer;

import java.time.temporal.ValueRange;
import java.util.ArrayList;

public class Player {
    public int hp;
    public int sp;
    public int atk;
    public int satk;
    public int def;
    public int sdef;
    public int spd;
    public int maxHp;

    public String fighterName;
    public String name;
    public String pic;
    public Map map;
    public String state = "idle";
    public int playerX;
    public int playerY = 250;
    private String currentAnimation = "idle"; // Current animation type
    private int animationIndex = 0; // Current frame index
    private long lastFrameTime = 0; // Timestamp of the last frame update
    public boolean animationComplete = true;
    boolean player1Dash;
    boolean player2Dash;
    boolean moveChoicePlayer2 = false;
    boolean moveChoicePlayer1 = false;

    public ArrayList<String> attack = new ArrayList<>();
    public ArrayList<String> dead = new ArrayList<>();
    public ArrayList<String> defend = new ArrayList<>();
    public ArrayList<String> hit = new ArrayList<>();
    public ArrayList<String> idle = new ArrayList<>();
    public ArrayList<String> run = new ArrayList<>();
    public ArrayList<String> sattack = new ArrayList<>();
    public ArrayList<String> special = new ArrayList<>();
    public ArrayList<String> ultimate = new ArrayList<>();

    // Set the animation state and reset the frame index
    public void setAnimation(String animationName) {
        if (!currentAnimation.equals(animationName) || animationComplete) {
            currentAnimation = animationName;
            animationIndex = 0; // Reset frame index for the new animation
            lastFrameTime = 0; // Reset timing
            animationComplete = false;
        }
    }

    // Get the frame delay for each animation in milliseconds
    private long getFrameDelay(String animationName) {
        return switch (animationName) {
            case "attack" -> 47;
            case "dead", "idle" -> 70;
            case "run" -> 1;
            default -> 50;
        };
    }

    // Get the current animation frames
    private ArrayList<String> getCurrentAnimationFrames() {
        return switch (currentAnimation) {
            case "attack" -> attack;
            case "dead" -> dead;
            case "hit" -> hit;
            case "sattack" -> sattack;
            case "special" -> special;
            case "ultimate" -> ultimate;
            case "run" -> run;
            // Add cases for other animations as needed
            default -> idle;
        };
    }

    // Draw the current frame of the animation
    public void drawCurrentAnimation(int x, int y, long currentTime) {
        ArrayList<String> frames = getCurrentAnimationFrames();
        long frameDelay = getFrameDelay(currentAnimation); // Adjust frame delay for smoother animations (in ms)

        // Update the frame index if enough time has passed
        if (currentTime - lastFrameTime >= frameDelay) {
            animationIndex = (animationIndex + 1) % frames.size();
            lastFrameTime = currentTime;

            // Reset to idle state if the animation completes and isn't looping
            if (animationIndex == 0 && !currentAnimation.equals("idle")) {
                setAnimation("idle");
                animationComplete = true;
            }
        }

        // Draw the current frame
        if (animationIndex >= 0 && animationIndex < frames.size()) {
            SaxionApp.drawImage(frames.get(animationIndex), x, y);
        }
    }

    public void updatePlayer(Fighter fighter) {
        this.pic = fighter.pic;
        this.map = fighter.map;
        this.fighterName = fighter.name;

        importStats(fighter);

        if (this.name.equalsIgnoreCase("player1")) {
            importAllAnimationsR(fighter);
        } else {
            importAllAnimationsL(fighter);
        }
    }

    public void importAllAnimationsR(Fighter fighter) {
        this.attack = fighter.attackR;
        this.dead = fighter.deadR;
        this.defend = fighter.defendR;
        this.hit = fighter.hitR;
        this.idle = fighter.idleR;
        this.run = fighter.runR;
        this.sattack = fighter.sattackR;
        this.special = fighter.specialR;
        this.ultimate = fighter.ultimateR;
    }

    public void importAllAnimationsL(Fighter fighter) {
        this.attack = fighter.attackL;
        this.dead = fighter.deadL;
        this.defend = fighter.defendL;
        this.hit = fighter.hitL;
        this.idle = fighter.idleL;
        this.run = fighter.runL;
        this.sattack = fighter.sattackL;
        this.special = fighter.specialL;
        this.ultimate = fighter.ultimateL;
    }

    public void importStats(Fighter fighter) {
        this.hp = fighter.hp;
        this.maxHp = fighter.maxHp;
        this.sp = fighter.sp;
        this.atk = fighter.atk;
        this.satk = fighter.satk;
        this.def = fighter.def;
        this.sdef = fighter.sdef;
        this.spd = fighter.spd;
    }

    public void setBattleCoords() {
        if (this.name.equalsIgnoreCase("player1")) {
            this.playerX = -70;
        } else {
            this.playerX = 750;
        }
    }

    public void playSound(String soundEffect) {
        MediaPlayer player = new MediaPlayer(soundEffect, false);
        new Thread(player::play).start();

    }

    public void resetIndexes() {
        this.animationIndex = 0; // Reset the animation index
        this.lastFrameTime = 0;
    }

    public void characterDash(Player player) {
        if (this.name.equalsIgnoreCase("player1")) {
            System.out.print(player1Dash);
            System.out.println(this.playerX);

            if (moveChoicePlayer1 && this.playerX < 240) {
                this.playerX = this.playerX + 25;
                this.setAnimation("run");
                player1Dash = true;


                if (this.playerX >= 240) {
                    player1Dash = false;
                    setAnimation("idle");
                    state = "idle";

                }
            }
        } else if (this.name.equalsIgnoreCase("player2")) {
            if ((moveChoicePlayer2 && this.playerX > 410)) {
                this.playerX = this.playerX - 25;
                this.setAnimation("run");
                player2Dash = true;

                if (player.playerX <= 410) {
                    player2Dash = false;
                    setAnimation("idle");
                    state = "idle";
                }
            }
        }

    }

    public void drawHpBar() {
        // Ensure current HP is within valid bounds (0 to maxHp)
        int currentHp = Math.max(0, Math.min(maxHp, hp));

        // Calculate the percentage of HP remaining (use double to avoid integer division)
        double hpPercentage = ((double) currentHp / maxHp) * 100;

        // Calculate the image index based on the percentage
        int imageIndex = (int) Math.ceil(hpPercentage / 5.0);

        // Clamp the image index to the range [1, 20]
        imageIndex = Math.max(0, Math.min(20, imageIndex));

        if (this.name.equalsIgnoreCase("player1")) {
            // Construct the file path dynamically
            String imagePath = "resources/battle/hp1-" + imageIndex + ".png";
            // Draw the image
            SaxionApp.drawImage(imagePath, Variables.xPositionP1, 50, 470, 138);
        }
        if (this.name.equalsIgnoreCase("player2")) {
            String imagePath = "resources/battle/hp2-" + imageIndex + ".png";
            SaxionApp.drawImage(imagePath, Variables.xPositionP2, 50, 470, 138);
        }
    }

    public void drawSpBar() {
            int currentSp = sp;
            if (currentSp > Variables.maxSp) {
                currentSp = 50;
            }
            double SpPercentage = ((double) currentSp / Variables.maxSp) * 100;
            int imageIndex = (int) Math.ceil(SpPercentage / 5.0);
            imageIndex = Math.max(0, Math.min(20, imageIndex));
            if (this.name.equalsIgnoreCase("player1")) {
                String imagePath = "resources/battle/sp1-" + imageIndex + ".png";
                SaxionApp.drawImage(imagePath, Variables.xPositionP1, 50, 470, 138);
            } else if (this.name.equalsIgnoreCase("player2")) {
                String imagePath = "resources/battle/sp2-" + imageIndex + ".png";
                SaxionApp.drawImage(imagePath, Variables.xPositionP2, 50, 470, 138);
            }
    }

    int xCordP1 = 20;
    int xCordP2 = 1130;

    public void drawIcons(){
        if (this.name.equalsIgnoreCase("player1")) {
            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_attack.png", xCordP1, 200);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_w.png", 5, 190);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_shield.png", xCordP1, 270);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_q.png", 5, 260);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_attackx2.png", xCordP1, 340);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_a.png", 5, 330);

            if (this.sp < 10) {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "special_grey.png", xCordP1, 410);
            } else {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "special.png", xCordP1, 410);
            }
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_s.png", 5, 400);

            if (this.sp < 20) {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "ult_grey.png", xCordP1, 480);
            } else {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "ult.png", xCordP1, 480);
            }
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_d.png", 5, 470);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "shift.png", 5, 635);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "description.png", 75, 642);
        }
        if (this.name.equalsIgnoreCase("player2")) {
            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_attack_p2.png", 1459, 200 );
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_up.png", xCordP2 + 370, 190);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_shield.png", 1459, 270);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_0.png", xCordP2 + 370, 260);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_sattack_p2.png", 1459, 340);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_left.png", xCordP2 + 370, 330);

            if (this.sp < 10) {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "special_grey.png", 1459, 410);
            } else {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "special.png", 1459, 410);
            }
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_down.png", xCordP2 + 370, 400);

            if (this.sp < 20) {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "ult_grey.png", 1459, 480);
            } else {
                SaxionApp.drawImage(Variables.PATH_BATTLE + "icon_" + this.fighterName + "ult.png", 1459, 480);
            }
            SaxionApp.drawImage(Variables.PATH_BATTLE + "key_right.png", xCordP2 + 370, 470);

            SaxionApp.drawImage(Variables.PATH_BATTLE + "description.png", 1310, 640);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "help_key_p2.png", 1490, 635, 35, 35);
        }
    }


    public void drawDescription() {
        SaxionApp.turnBorderOff();
        if (this.name.equalsIgnoreCase("player1")) {
            SaxionApp.drawImage( Variables.PATH_BATTLE + "desc_1attack.png", xCordP1, 200);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_1defend.png", xCordP1, 270);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_1sattack.png", xCordP1, 340);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_1special_" + this.fighterName + ".png", xCordP1, 410);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_1ultimate_" + this.fighterName + ".png", xCordP1, 480);
        }
        if (this.name.equalsIgnoreCase("player2")) {
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_2attack.png", xCordP2, 200);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_2defend.png", xCordP2, 270);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_2sattack.png", xCordP2, 340);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_2special_" + this.fighterName + ".png", xCordP2, 410);
            SaxionApp.drawImage(Variables.PATH_BATTLE + "desc_2ultimate_" + this.fighterName + ".png", xCordP2, 480);

        }
    }

    public int xCoordinateChange() {
        if (this.name.equalsIgnoreCase("player1")) {
            playerX = -70;
        } else if (this.name.equalsIgnoreCase("player2")) {
            playerX = 750;
        }

        return playerX;
    }

    public int mover() {
        int mover = 0;
        if (this.name.equalsIgnoreCase("player1")) {
            mover = 25;
        } else if (this.name.equalsIgnoreCase("player2")) {
            mover = -25;
        }
        return mover;
    }

    public int limit() {
        int limit = 0;
        if (this.name.equalsIgnoreCase("player1")) {
            limit = 200;
        } else if (this.name.equalsIgnoreCase("player2")) {
            limit = 370;
        }
        return limit;
    }


    public int dmgTaken(int atk, int def, boolean defending) {
        int dmg = atk - def;
        if (defending){
            dmg = (int) (dmg * ( 0.25));
        }
        return dmg;
    }
    public void playSelectedMove(Player player){
        if (player.moveChoicePlayer2){
            this.setAnimation(state);
        }
    }
}
