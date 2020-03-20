package dk.sdu.mmmi.tower;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.WeaponPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.commontower.Tower;

public class SingleDamageTowerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        PositionPart p = new PositionPart(0, 0, 0);
        LifePart l = new LifePart();
        CollisionPart c = new CollisionPart();
        WeaponPart w = new WeaponPart(10, 50, 10);
        
        Tower tower = new Tower(p, l, c, w);
    }

    @Override
    public void stop(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
