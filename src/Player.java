import nl.saxion.app.SaxionApp;

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

    public ArrayList<String> attack = new ArrayList<>();
    public int attackIndex = 0;

    public ArrayList<String> dead = new ArrayList<>();
    public int deadIndex = 0;

    public ArrayList<String> defend = new ArrayList<>();
    public int defendIndex;

    public ArrayList<String> hit = new ArrayList<>();
    public int hitIndex = 0;

    public ArrayList<String> idle = new ArrayList<>();
    public int idleIndex = 0;

    public ArrayList<String> run = new ArrayList<>();
    public int runIndex;

    public ArrayList<String> sattack = new ArrayList<>();
    public int sattackIndex = 0;

    public ArrayList<String> special = new ArrayList<>();
    public int specialIndex = 0;

    public ArrayList<String> ultimate = new ArrayList<>();
    public int ultimateIndex = 0;

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

    public void playAnimation(ArrayList<String> animation, String soundEffect) {

        // Sound effects here
//        MediaPlayer player = new MediaPlayer(soundEffect,false);
//        player.play();

        for (String frame : animation) {
            SaxionApp.drawImage(frame, 0, 240);
            SaxionApp.sleep(0.001);
        }
    }

    public void resetIndexes() {
        this.attackIndex = 0;
        this.defendIndex = 0;
        this.deadIndex = 0;
        this.hitIndex = 0;
        this.idleIndex = 0;
        this.runIndex = 0;
        this.sattackIndex = 0;
        this.specialIndex = 0;
        this.ultimateIndex = 0;
    }

    public void loopAnimation(ArrayList<String> animation, int x, int y) {
        if (idleIndex > animation.size()) {
            idleIndex = 0;
        }

        SaxionApp.drawImage(animation.get(idleIndex), x, y);
        SaxionApp.sleep(0.02);

        idleIndex = (idleIndex + 1) % animation.size();
    }
}
