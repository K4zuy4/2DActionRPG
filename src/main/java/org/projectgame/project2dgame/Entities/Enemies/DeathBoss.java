package org.projectgame.project2dgame.Entities.Enemies;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.projectgame.project2dgame.Controller.CollisionCheck;
import org.projectgame.project2dgame.Entities.Character;
import org.projectgame.project2dgame.Entities.Entity;
import org.projectgame.project2dgame.Entities.EntityManagement;
import org.projectgame.project2dgame.GameField.GameField;
import java.util.Random;

import static javafx.scene.paint.Color.rgb;

public class DeathBoss extends Entity {

    // Zustände des Bosses
    public enum BossState { CHASING, ATTACKING, SUMMONING, TELEPORTING }

    private BossState state;
    private double stateTimer;  // Laufzeitzähler in Sekunden
    private int attackCount;    // Zählt die 2 Hits

    // Timing-Konstanten (in Sekunden)
    private final double attackInterval = 0.7;   // Zeit zwischen den Angriffen
    private final double summonDelay = 1.0;      // Verzögerung vor der Beschwörung

    // Distanz, ab der der Boss angreift (in Pixeln)
    private final double attackRange = 30;

    // Timing-Konstanten für den Teleport-Delay (in Sekunden)
    private final double teleportPreDelay = 1.2;
    private final double teleportPostDelay = 0.7;
    private boolean hasTeleported = false;

    // Timing-Konstanten damit der Chase nicht zu lange dauert
    private final double maxChaseDuration = 5.0;
    private double chaseTimer = 0;
    private boolean teleportNearPlayer = false;

    private String currentAnimation = "";

    // Extra Hitbox für den Attack des Bosses
    private Rectangle damageHitbox;

    public DeathBoss(double x, double y, int health, int entitySpeed, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, entitySpeed, gamePane, entityManagement);
        this.state = BossState.CHASING;
        this.stateTimer = 0;
        this.attackCount = 0;

        // Setze initial das Idle-Sprite
        currentAnimation = "deathboss-idle";
        this.sprite = EntityManagement.getImage("deathboss-idle");
        this.sprite.setFitWidth(GameField.getTileSize() * 4.6);
        this.sprite.setFitHeight(GameField.getTileSize() * 4.6);

        sprite.setX(this.x);
        sprite.setY(this.y);

        // Hitbox des Bosses
        this.hitbox = new Rectangle(x, y, GameField.getTileSize() * 3, GameField.getTileSize() * 3);
        if(GameField.isDebug()) {
            hitbox.setFill(rgb(255, 255, 255, 0.5));
        } else {
            hitbox.setFill(rgb(255, 0, 0, 0));
        }

        this.healthBar.setPrefWidth(GameField.getTileSize() * 2);
        this.healthBar.setPrefHeight(20);

        double damageWidth = hitbox.getWidth() * 1.7;
        double damageHeight = hitbox.getHeight() * 1.7;
        this.damageHitbox = new Rectangle(x, y, damageWidth, damageHeight);
        if(GameField.isDebug()) {
            damageHitbox.setFill(rgb(255, 0, 0, 0.5));
        } else {
            damageHitbox.setFill(rgb(255, 0, 0, 0));
        }

        entityManagement.getGamePane().getChildren().add(damageHitbox);

        updateHitboxPosition();
    }

    @Override
    public void update(double deltaTime) {
        // Hol den Player (falls nicht null)
        Character player = entityManagement.getCharacter();
        if (player == null) return;

        stateTimer += deltaTime;

        switch(state) {
            case CHASING: {
                double dx = player.getX() - this.x;
                double dy = player.getY() - this.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                updateSpriteDirection(dx, dy);

                chaseTimer += deltaTime;

                // Falls der Boss zu lange gejagt hat, wird er in die Nähe des Spielers teleportieren
                if (chaseTimer >= maxChaseDuration) {
                    state = BossState.TELEPORTING;
                    stateTimer = 0;
                    chaseTimer = 0; // Reset für den nächsten Lauf
                    teleportNearPlayer = true;
                    currentAnimation = "deathboss-teleport";
                    sprite.setImage(EntityManagement.getImage("deathboss-teleport").getImage());
                    break;
                }

                if (distance > attackRange) {
                    double moveX = (dx / distance) * entitySpeed * deltaTime;
                    double moveY = (dy / distance) * entitySpeed * deltaTime;
                    this.x += moveX;
                    this.y += moveY;
                    sprite.setX(this.x);
                    sprite.setY(this.y);
                    updateHitboxPosition();
                    if (!"deathboss-idle".equals(currentAnimation)) {
                        currentAnimation = "deathboss-idle";
                        sprite.setImage(EntityManagement.getImage("deathboss-idle").getImage());
                    }
                } else {
                    // Wenn der Spieler nah genug ist, starte den Angriff – und reset den chaseTimer
                    state = BossState.ATTACKING;
                    stateTimer = 0;
                    attackCount = 0;
                    chaseTimer = 0;
                    currentAnimation = "deathboss-attack";
                    sprite.setImage(EntityManagement.getImage("deathboss-attack").getImage());
                }
                break;
            }


            case ATTACKING: {
                if (stateTimer >= attackInterval) {
                    attackCount++;
                    stateTimer = 0;
                    // Verwende die DamageHitbox für die Treffererkennung
                    if (damageHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
                        player.takeDamage(30, true);
                    }
                    if (attackCount >= 2) {
                        state = BossState.SUMMONING;
                        stateTimer = 0;
                        currentAnimation = "deathboss-summon";
                        sprite.setImage(EntityManagement.getImage("deathboss-summon").getImage());
                    } else {
                        currentAnimation = "deathboss-attack";
                        sprite.setImage(EntityManagement.getImage("deathboss-attack").getImage());
                    }
                }
                break;
            }


            case SUMMONING: {
                if(teleportNearPlayer) {
                    // Teleportiere den Boss in die Nähe des Spielers
                    double dx = player.getX() - this.x;
                    double dy = player.getY() - this.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double moveX = (dx / distance) * entitySpeed * deltaTime;
                    double moveY = (dy / distance) * entitySpeed * deltaTime;
                    this.x += moveX;
                    this.y += moveY;
                    sprite.setX(this.x);
                    sprite.setY(this.y);
                    updateHitboxPosition();
                    teleportNearPlayer = false;
                }
                if (stateTimer >= summonDelay) {
                    int skeletonCount = 0;
                    for(Entity e : entityManagement.getEntity()) {
                        if(e instanceof Skeleton) {
                            skeletonCount++;
                        }
                    }
                    if(skeletonCount < 3) {;
                        spawnSkeleton();
                    }
                    // Nach der Beschwörung teleportiert sich der Boss
                    state = BossState.TELEPORTING;
                    stateTimer = 0;
                    currentAnimation = "deathboss-teleport";
                    sprite.setImage(EntityManagement.getImage("deathboss-teleport").getImage());
                }
                break;
            }
            case TELEPORTING: {
                // Wenn der Boss noch nicht teleportiert hat, warte bis teleportPreDelay erreicht ist
                if (!hasTeleported) {
                    if (stateTimer >= teleportPreDelay) {
                        teleport();
                        hasTeleported = true;
                        stateTimer = 0;
                    }
                } else {
                    // Nach dem Teleport bleibt der Boss an der neuen Position stehen, bis teleportPostDelay abgelaufen ist
                    if (stateTimer >= teleportPostDelay) {
                        state = BossState.CHASING;
                        stateTimer = 0;
                        hasTeleported = false;
                        if (!"deathboss-idle".equals(currentAnimation)) {
                            currentAnimation = "deathboss-idle";
                            sprite.setImage(EntityManagement.getImage("deathboss-idle").getImage());
                        }
                    }
                }
                break;
            }

        }
    }

    @Override
    public void updateHitboxPosition() {
        // Positioniere die Hitbox zentriert zum Boss-Sprite
        hitbox.setX(x + (sprite.getFitWidth() - hitbox.getWidth()) / 2);
        hitbox.setY(y + (sprite.getFitHeight() - hitbox.getHeight()) / 2);

        double damageWidth = hitbox.getWidth() * 1.7;
        double damageHeight = hitbox.getHeight() * 1.7;
        damageHitbox.setWidth(damageWidth);
        damageHitbox.setHeight(damageHeight);
        damageHitbox.setX(x + (sprite.getFitWidth() - damageWidth) / 2);
        damageHitbox.setY(y + (sprite.getFitHeight() - damageHeight) / 2);

        healthBar.setLayoutX(x + sprite.getFitWidth() / 2 - healthBar.getPrefWidth() / 2);
        healthBar.setLayoutY(y - 20);
    }

    @Override
    protected void updateSpriteDirection(double dx, double dy) {
        if (dx < 0) {
            sprite.setScaleX(-1);
        } else {
            sprite.setScaleX(1);
        }
    }


    // Beschwört ein Skeleton in der Nähe des Bosses mit Offset
    private void spawnSkeleton() {
        Random random = new Random();
        int offsetX = random.nextInt(100) - 50;
        int offsetY = random.nextInt(100) - 50;
        int spawnX = (int) this.x + offsetX;
        int spawnY = (int) this.y + offsetY;
        int skeletonHealth = 160;  // Beispielwert für Level 5
        Skeleton skeleton = new Skeleton(spawnX, spawnY, skeletonHealth, gamePane, entityManagement);
        entityManagement.getEntity().add(skeleton);
        gamePane.getChildren().addAll(skeleton.getSprite(), skeleton.getHitbox(), skeleton.getHealthBar());
    }

    private void teleport() {
        CollisionCheck collisionCheck = entityManagement.getCollisionCheck();
        Random random = new Random();
        int attempts = 0;
        boolean valid = false;
        double newX = this.x;
        double newY = this.y;
        while (attempts < 10 && !valid) {
            newX = 64 + random.nextInt((int)(GameField.getScreenWidth() - 128));
            newY = 64 + random.nextInt((int)(GameField.getScreenHeight() - 128));
            // Berechne die potenzielle neue Hitbox basierend auf neuen Koordinaten
            double hitboxX = newX + (sprite.getFitWidth() - hitbox.getWidth()) / 2;
            double hitboxY = newY + (sprite.getFitHeight() - hitbox.getHeight()) / 2;
            Rectangle newHitbox = new Rectangle(hitboxX, hitboxY, hitbox.getWidth(), hitbox.getHeight());
            // Wenn keine Kollision auftritt, gilt die Position als valide
            if (!collisionCheck.checkCollisionEntity(newHitbox, 0, 0)) {
                valid = true;
            }
            attempts++;
        }
        if (valid) {
            this.x = newX;
            this.y = newY;
            sprite.setX(this.x);
            sprite.setY(this.y);
            updateHitboxPosition();
        }
        // Falls kein gültiger Ort gefunden wurde, bleibt der Boss einfach an der aktuellen Position
    }

}
