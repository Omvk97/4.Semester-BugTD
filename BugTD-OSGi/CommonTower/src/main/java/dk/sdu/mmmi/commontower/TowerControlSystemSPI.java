package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;

public interface TowerControlSystemSPI {

    /* TOWER PLACEMENT */
    void placeNewTowers(GameData gameData, World world);

    Tower constructNewTower(int xpos, int ypos);

    boolean isLegalPlacement(Entity e);

    void showTowerPlacementPreview(GameData gameData, World world);

    /* ATTACK ENEMIES */
    Entity calculateClosestEnemy(World world, PositionPart towerPosPart);

    Entity calculateLowestHealthEnemy();

    void attackEnemies(GameData gameData, World world);
}
