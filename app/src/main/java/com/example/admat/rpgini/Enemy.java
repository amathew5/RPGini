package com.example.admat.rpgini;

/**
 * Created by galax_000 on 12/11/2016.
 */

public class Enemy {
    private String name;
    private int maxHp, Hp;
    private int powerPhys, powerMagic;
    private int frame, level;

    public Enemy (int frame) {
        this.frame = frame;

        maxHp = Hp = 50;
        powerMagic = powerPhys = 5;
        name = "Enemy "+frame;
    }

    public Enemy (int frame, boolean isMagical, int powerLevel) {
        this.frame = frame;

        level = powerLevel;
        maxHp = Hp = powerLevel * powerLevel * 5;
        powerMagic = isMagical?powerLevel*5:powerLevel/3;
        powerPhys = isMagical?powerLevel/3:powerLevel*5;

        name = "Enemy "+frame;
    }

    public int getFrame() {
        return frame;
    }

    public int getHp() {
        return Hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int damageByStrength(int i) {
        if(powerPhys > powerMagic) {
            // Enemy is Physical
            Hp -= i;
            return i;
        } else {
            // Enemy is Magical
            Hp -= (i*3)/2;
            return (i*3)/2;
        }
    }

    public int damageByMagic(int i) {
        if(powerPhys > powerMagic) {
            // Enemy is Physical
            Hp -= (i*3)/2;
            return (i*3)/2;
        } else {
            // Enemy is Magical
            Hp -= i;
            return i;
        }
    }

    public String getName() {
        return name;
    }

    public int getLvl() {
        return level;
    }

    public int getDamage() {
        if(powerPhys > powerMagic) {
            // Enemy is Physical
            return powerPhys;
        } else {
            // Enemy is Magical
            return powerMagic;
        }
    }

    public int getXpReward() {
        return level*5;
    }
}
