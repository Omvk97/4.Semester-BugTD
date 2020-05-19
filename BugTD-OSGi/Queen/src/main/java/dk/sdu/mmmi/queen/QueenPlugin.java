package dk.sdu.mmmi.queen;

import dk.sdu.mmmi.cbse.Game;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.*;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventObserver;
import dk.sdu.mmmi.cbse.common.events.QueenSpawnedEvent;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commonmap.MapSPI;
import dk.sdu.mmmi.commontower.Queen;

public class QueenPlugin extends EventObserver implements IGamePluginService  {

    MapSPI mapSPI;
    
    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity queen : world.getEntities(Queen.class)) {
            world.removeEntity(queen);
        }
    }

    @Override
    public void methodToCall(Event event) {
        if (event.getClass() == QueenSpawnedEvent.class) {
            createQueen((QueenSpawnedEvent) event);
        }
    }

    private void createQueen(QueenSpawnedEvent queenSpawnedEvent) {
        int tileSize = 16;
        PositionPart pos = new PositionPart(queenSpawnedEvent.getX(), queenSpawnedEvent.getY(), 0);
        LifePart life = new LifePart(queenSpawnedEvent.getLife());
        CollisionPart collision = new CollisionPart(3* tileSize, 3*tileSize);
        WeaponPart weapon = new WeaponPart(queenSpawnedEvent.getDamage(), queenSpawnedEvent.getRange(), queenSpawnedEvent.getAttackSpeed());
        SpritePart sprite = new SpritePart("towers/queen.png", 3*tileSize, 3*tileSize, 1);
        Queen queen = new Queen(pos, life, collision, weapon, sprite);
        Game.getInstance().getWorld().addEntity(queen);
        mapSPI.setQueenID(queen.getID());
    }

    public void setMapSPI(MapSPI mapSPI) {
        this.mapSPI = mapSPI;
    }

    public void removeMapSPI(MapSPI mapSPI) {
        this.mapSPI = null;
    }
}
