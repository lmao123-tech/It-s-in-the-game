import nl.saxion.app.SaxionApp;
import nl.saxion.app.audio.MediaPlayer;

import java.awt.*;
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
    boolean moving = false;

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
            System.out.print(moving);
            System.out.println(this.playerX);

            if (moving && this.playerX < 240) {
                this.playerX = this.playerX + 25;
                this.setAnimation("run");

                if (this.playerX >= 240) {
                    moving = false;
                    setAnimation("idle");
                    state = "idle";
                }
            }
        } else if (this.name.equalsIgnoreCase("player2")) {
            System.out.print(moving);

            if ((moving && this.playerX > 410)) {
                this.playerX = this.playerX - 25;
                this.setAnimation("run");

                if (player.playerX <= 410) {
                    moving = false;
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
        imageIndex = Math.max(1, Math.min(20, imageIndex));
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

    int maxSp = 50;

    public void drawSpBar(int player) {
        if (player == 1) {
            int currentSp = sp;
            if (currentSp > maxSp) {
                currentSp = 50;
            }
            double SpPercentage = ((double) currentSp / maxSp) * 100;
            int imageIndex = (int) Math.ceil(SpPercentage / 5.0);
            imageIndex = Math.max(0, Math.min(20, imageIndex));
            String imagePath = "resources/battle/sp1-" + imageIndex + ".png";
            SaxionApp.drawImage(imagePath, Variables.xPositionP1, 50, 470, 138);
        }
    }

    public void drawDescription() {
        SaxionApp.turnBorderOff();
        if (this.name.equalsIgnoreCase("player1")) {
            SaxionApp.drawImage("resources/battle/desc_attack.png", 20, 200);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 20, 270);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 20, 340);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 20, 410);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 20, 480);
        }
        if (this.name.equalsIgnoreCase("player2")) {
            SaxionApp.drawImage("resources/battle/desc_attack.png", 1480, 200);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 1480, 270);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 1480, 340);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 1480, 410);
            SaxionApp.drawImage("resources/battle/desc_attack.png", 1480, 480);

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
}
