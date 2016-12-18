package com.example.admat.rpgini;

/**
 * Created by galax_000 on 12/17/2016.
 */
public class CurrentPlayerData {
    private static CurrentPlayerData ourInstance = new CurrentPlayerData();

    public static CurrentPlayerData getInstance() {
        return ourInstance;
    }

    public static void reset() {
        ourInstance = new CurrentPlayerData();
    }

    private String name;
    private int physical,magical,health,xp,level;

    private CurrentPlayerData() {
        name = "Harambe";
        physical=20;
        magical=10;
        health=100;
        xp=0;
        level=1;
    }

    public void loadData(MyDB db, String username, String table) {
        name = db.getCharName(username,table).toString();
        physical = db.getPhysical(username,table);
        magical = db.getMagical(username,table);
        health = db.getHealth(username,table);
        level = db.getLevel(username,table);
        xp = db.getXP(username,table);
    }

    public void saveData(MyDB db, String username, String table) {
        db.setCharName(username, name, table);
        db.setPhysical(username, physical, table);
        db.setMagical(username, magical, table);
        db.setHealth(username, health, table);
        db.setLevel(username, level, table);
        db.setXP(username, xp, table);
    }

    public int getPhysical() {
        return physical;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
    }

    public int getMagical() {
        return magical;
    }

    public void setMagical(int magical) {
        this.magical = magical;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int xpEarned) {
        this.xp += xpEarned;
    }

    public int getNextXp() {
        return level * level * 100;
    }

    public boolean checkLevelUp() {
        if(xp > getNextXp()) {
            health += (level * 50);
            xp -= getNextXp();
            level++;
            physical += Math.abs(Math.cos(Math.toRadians(level*45)) * 5);
            magical += Math.abs(Math.sin(Math.toRadians(level*45)) * 5);
            return true;
        }
        return false;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
