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
    private final double teleportDelay = 0.5;    // Verzögerung während Teleportation

    // Distanz, ab der der Boss angreift (in Pixeln)
    private final double attackRange = 100;

    public DeathBoss(double x, double y, int health, int entitySpeed, Pane gamePane, EntityManagement entityManagement) {
        super(x, y, health, entitySpeed, gamePane, entityManagement);
        this.state = BossState.CHASING;
        this.stateTimer = 0;
        this.attackCount = 0;

        // Setze initial das Idle-Sprite
        this.sprite = EntityManagement.getImage("deathboss-idle");
        this.sprite.setFitWidth(GameField.getTileSize() * 4);
        this.sprite.setFitHeight(GameField.getTileSize() * 4);

        // Hitbox des Bosses
        this.hitbox = new Rectangle(x, y, GameField.getTileSize() * 3, GameField.getTileSize() * 3);
        if(GameField.isDebug()) {
            hitbox.setFill(rgb(0, 255, 0, 0.5));
        } else {
            hitbox.setFill(rgb(255, 0, 0, 0));
        }
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

                updateSpriteDirection(dx, dy);

                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance > attackRange) {
                    double moveX = (dx / distance) * entitySpeed * deltaTime;
                    double moveY = (dy / distance) * entitySpeed * deltaTime;
                    // Prüfen, ob sich der Boss mit diesem Offset kollidiert
                        this.x += moveX;
                        this.y += moveY;
                        // Sprite und Hitbox synchronisieren
                        this.sprite.setX(this.x);
                        this.sprite.setY(this.y);
                        updateHitboxPosition();
                    // Setze das Idle-Sprite
                    if (!sprite.getImage().equals(EntityManagement.getImage("deathboss-idle").getImage())) {
                        sprite.setImage(EntityManagement.getImage("deathboss-idle").getImage());
                    }
                } else {
                    state = BossState.ATTACKING;
                    stateTimer = 0;
                    attackCount = 0;
                    sprite.setImage(EntityManagement.getImage("deathboss-attack").getImage());
                }
                break;
            }

            case ATTACKING: {
                if (stateTimer >= attackInterval) {
                    attackCount++;
                    stateTimer = 0;
                    // Füge dem Player Schaden zu, wenn er in range ist
                    if (Math.hypot(player.getX() - this.x, player.getY() - this.y) <= attackRange) {
                        player.takeDamage(30);
                    }
                    if (attackCount >= 2) {
                        // Nach 2 Schlägen wechselt er in den Beschwörungsmodus
                        state = BossState.SUMMONING;
                        stateTimer = 0;
                        sprite.setImage(EntityManagement.getImage("deathboss-summon").getImage());
                    } else {
                        // Falls es der zweite Schlag wird, halte die Attack-Animation nochmal kurz
                        sprite.setImage(EntityManagement.getImage("deathboss-attack").getImage());
                    }
                }
                break;
            }
            case SUMMONING: {
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
                    sprite.setImage(EntityManagement.getImage("deathboss-teleport").getImage());
                }
                break;
            }
            case TELEPORTING: {
                if (stateTimer >= teleportDelay) {
                    teleport();
                    // Nach dem Teleport kehrt er in den Chasing-Modus zurück
                    state = BossState.CHASING;
                    stateTimer = 0;
                    sprite.setImage(EntityManagement.getImage("deathboss-idle").getImage());
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
        // HealthBar oberhalb des Bosses
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
