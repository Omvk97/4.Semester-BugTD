package dk.sdu.mmmi.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.AnimationPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.events.ClickEvent;
import dk.sdu.mmmi.cbse.common.events.PlayerArrivedEvent;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonplayer.Player;
import dk.sdu.mmmi.commonweapon.WeaponPart;
import dk.sdu.mmmi.commonweapon.WeaponPart.Color;


public class PlayerPlugin implements IGamePluginService {

    //private Entity player;
    @Override
    public void start(GameData gameData, World world) {

        Entity aPlayer = new Player();

        // attributes
        float deacceleration = 100;
        float acceleration = 200;
        float maxSpeed = 500;
        float rotation = 5;
        float x = gameData.getDisplayWidth() / 3;
        float y = gameData.getDisplayWidth() / 2;
        float radians = 3.1415f / 2;
        
        float damage = 10; 
        float range = 200;
        float speed = 1;
        Color color = WeaponPart.Color.YELLOW;
        

        // parts 
        AnimationPart animation = new AnimationPart("texturesprites/player32/stand.atlas", 32, 32);
        MovingPart moving = new MovingPart(deacceleration, acceleration, maxSpeed, rotation);
        PositionPart position = new PositionPart(x, y, radians);
        WeaponPart wp = new WeaponPart(damage, range, speed, color);
        
         aPlayer.add(animation);
         aPlayer.add(moving);
         aPlayer.add(position);
         aPlayer.add(wp);

        world.addEntity(aPlayer);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            world.removeEntity(player);
            gameData.getEvents().removeAll(gameData.getEvents(PlayerArrivedEvent.class));
            gameData.getEvents().removeAll(gameData.getEvents(ClickEvent.class));
        }
    }

}
