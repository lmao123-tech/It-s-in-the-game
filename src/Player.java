import nl.saxion.app.SaxionApp;
import nl.saxion.app.audio.MediaPlayer;

import java.util.ArrayList;

public class Player {
    public int hp;
    public int sp;
    public int atk;
    public int satk;
    public int def;
    public int sdef;
    public int spd;

    public String name;
    public String pic;
    public Map map;
    public String state = "idle";
    private String currentAnimation = "idle"; // Current animation type
    private int animationIndex = 0; // Current frame index
    private long lastFrameTime = 0; // Timestamp of the last frame update
    public boolean animationComplete = true;

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

        if (this.name.equalsIgnoreCase("player 1")) {
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
        this.sp = fighter.sp;
        this.atk = fighter.atk;
        this.satk = fighter.satk;
        this.def = fighter.def;
        this.sdef = fighter.sdef;
        this.spd = fighter.spd;
    }


    public void playSound(String soundEffect) {
        MediaPlayer player = new MediaPlayer(soundEffect,false);
        new Thread(player::play).start();

    }

    public void resetIndexes() {
        this.animationIndex = 0; // Reset the animation index
        this.lastFrameTime = 0;
    }
}
