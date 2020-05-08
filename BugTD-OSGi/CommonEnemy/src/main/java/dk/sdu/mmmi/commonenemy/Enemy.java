package dk.sdu.mmmi.commonenemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovementInstruction;
import dk.sdu.mmmi.cbse.common.data.entityparts.PreciseMovingPart;
import dk.sdu.mmmi.commonai.events.EnemyCommand;
import dk.sdu.mmmi.weaponpart.WeaponPart;
import java.util.List;

public class Enemy extends Entity {

    private List<EnemyCommand> commands = null;
    private int commandIndex = 0;
    private EnemyType type;
    private WeaponPart weaponPart;
    private AnimationPart animationPart;
    private PositionPart positionPart;
    private PreciseMovingPart movingPart;
    private LifePart lifePart;

    public static Entity createGroundEnemy(float x, float y, int life) {
        //attributes
        float weaponDamage = 12.5f;
        float weaponRange = 50;
        float weaponSpeed = 0.5f;
        float speedPerMovement = 8;
        float radians = 3.1415f / 2;

        Entity gEnemy = new Enemy(EnemyType.GROUND,
                new WeaponPart(weaponDamage, weaponRange, weaponSpeed),
                new AnimationPart("texturesprites/enemy/enemyup.atlas", 16, 16, 0),
                new PositionPart(x, y, radians),
                new PreciseMovingPart(speedPerMovement),
                new LifePart(life));

        return gEnemy;
    }

    public static Entity createFlyingEnemy(float x, float y, int life) {
        Entity fEnemy = new Enemy();
        //attributes
        float damage = 10;
        float range = 50;
        float speed = 10;
        float deacceleration = 100;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float radians = 3.1415f / 2;

        //Parts
        AnimationPart anm = new AnimationPart("texturesprites/enemy/enemyup.atlas", 32, 32);
        fEnemy.add(anm);
        //Parts
        //SpritePart sprite = new SpritePart("enemy/enemyright/right_01.png", 32, 32);
        //fEnemy.add(sprite);
        WeaponPart wpn = new WeaponPart(damage, range, speed);
        fEnemy.add(wpn);
        MovingPart mov = new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed);
        fEnemy.add(mov);
        PositionPart pos = new PositionPart(x, y, radians);
        fEnemy.add(pos);
        LifePart lif = new LifePart(life);
        fEnemy.add(lif);

        return fEnemy;
    }

    public Enemy() {
        super();
    }

    public Enemy(EnemyType type, WeaponPart weaponPart, AnimationPart animationPart, PositionPart positionPart, PreciseMovingPart movingPart, LifePart lifePart) {
        this.type = type;
        this.weaponPart = weaponPart;
        add(weaponPart);
        this.animationPart = animationPart;
        add(animationPart);
        this.positionPart = positionPart;
        add(positionPart);
        this.movingPart = movingPart;
        add(movingPart);
        this.lifePart = lifePart;
        add(lifePart);
    }

    public List<EnemyCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<EnemyCommand> moveCommands) {
        this.commands = moveCommands;
        resetCommandIndex();
    }

    public void incrementCommandIndex() {
        this.commandIndex++;
    }

    private void resetCommandIndex() {
        this.commandIndex = 0;
    }

    public int getCommandIndex() {
        return this.commandIndex;
    }

    public boolean isDoneFollowingCommands() {
        return commandIndex == commands.size();
    }

    public float getXTarget() {
        return commands.get(commandIndex).getX();
    }

    public float getYTarget() {
        return commands.get(commandIndex).getY();
    }

    public EnemyType getType() {
        return type;
    }

    public float getCurrentX() {
        return positionPart.getX();
    }

    public float getCurrentY() {
        return positionPart.getY();
    }

    public float calculateFutureXPosition() {
        return movingPart.calculateFutureXPosition(getCurrentX());
    }

    public float calculateFutureYPosition() {
        return movingPart.calculateFutureYPosition(getCurrentY());
    }

    public void addMovement(PreciseMovementInstruction preciseMovementInstruction) {
        movingPart.addMovement(preciseMovementInstruction);
    }

    public WeaponPart getWeaponPart() {
        return weaponPart;
    }

    public AnimationPart getAnimationPart() {
        return animationPart;
    }

    public PositionPart getPositionPart() {
        return positionPart;
    }

    public PreciseMovingPart getMovingPart() {
        return movingPart;
    }
}
