package org.projectgame.project2dgame.Entities;

public class CharacterInfo {
    private static int money = 0;
    private static int health = 100;
    private static int maxHealth = 100;
    private static int damage = 10;
    private static int speed = 200;
    private static long fireRate = 500;

    public static void reset() {
        money = 0;
        health = 100;
        maxHealth = 100;
        damage = 10;
        speed = 200;
        fireRate = 500;
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
}