package org.projectgame.project2dgame.Entities;

public class CharacterInfo {
    private static int money = 0;
    private static int health = 100;
    private static int maxHealth = 100;
    private static int damage = 25;
    private static int speed = 200;
    private static long fireRate = 500;
    private static int levelDone = 1;
    private static int damagePrice = 40;
    private static int speedPrice = 45;
    private static int fireratePrice = 60;
    private static int healPrice = 30;

    public static void reset() {
        money = 0;
        health = 100;
        maxHealth = 100;
        damage = 25;
        speed = 200;
        fireRate = 500;
        levelDone = 1;
        damagePrice = 40;
        speedPrice = 45;
        fireratePrice = 60;
        healPrice = 30;
    }

    public static int getMoney() {
        return money;
    }

    public static int getHealth() {
        return health;
    }

    public static int getMaxHealth() {
        return maxHealth;
    }

    public static int getDamage() {
        return damage;
    }

    public static int getSpeed() {
        return speed;
    }

    public static long getFireRate() {
        return fireRate;
    }

    public static int getLevelDone() {
        return levelDone;
    }

    public static int getDamagePrice() {
        return damagePrice;
    }

    public static int getSpeedPrice() {
        return speedPrice;
    }

    public static int getFireratePrice() {
        return fireratePrice;
    }

    public static int getHealPrice() {
        return healPrice;
    }

    public static void setMoney(int newMoney) {
        money = newMoney;
    }

    public static void setHealth(int newHealth) {
        health = newHealth;
    }

    public static void setMaxHealth(int newMaxHealth) {
        maxHealth = newMaxHealth;
    }

    public static void setDamage(int newDamage) {
        damage = newDamage;
    }

    public static void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public static void setFireRate(long newFireRate) {
        if((fireRate - newFireRate) > 0) fireRate = newFireRate;
    }

    public static void setLevelDone(int newLevelDone) {
        levelDone = newLevelDone;
    }

    public static void setDamagePrice(int newDamagePrice) {
        damagePrice = newDamagePrice;
    }

    public static void setSpeedPrice(int newSpeedPrice) {
        speedPrice = newSpeedPrice;
    }

    public static void setFireratePrice(int newFireratePrice) {
        fireratePrice = newFireratePrice;
    }

    public static void setHealPrice(int newHealPrice) {
        healPrice = newHealPrice;
    }
}