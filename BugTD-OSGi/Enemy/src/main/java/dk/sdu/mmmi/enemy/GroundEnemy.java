/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonenemy.Enemy;

/**
 *
 * @author marcu
 */
public class GroundEnemy implements IGamePluginService {

    private Entity groundEnemy;
    
    @Override
    public void start(GameData gameData, World world) {
        groundEnemy = createGroundEnemy(gameData);
        
    }
    
    private Entity createGroundEnemy(GameData gameData){
        
        //attributes
        float speed;
        float attackSpeed;
        float damage;
        float range;
        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;
        
        Entity gEnemy = new Enemy();
        
        //Parts
        gEnemy.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        gEnemy.add(new PositionPart(x, y, radians));
        return gEnemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(groundEnemy);
    }
    
}
