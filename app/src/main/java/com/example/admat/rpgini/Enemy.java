package com.example.admat.rpgini;

/**
 * Created by galax_000 on 12/11/2016.
 */

public class Enemy {
    private String name;
    private int maxHp, Hp;
    private int powerPhys, powerMagic;
    private int frame;

    public Enemy (int frame) {
        this.frame = frame;

        maxHp = Hp = 50;
        powerMagic = powerPhys = 5;
        name = "Gobbo "+frame;
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

    public void damageByStrength(int i) {
        Hp -= i;
    }

    public void damageByMagic(int i) {
        Hp -= i;
    }

    public String getName() {
        return name;
    }
}
