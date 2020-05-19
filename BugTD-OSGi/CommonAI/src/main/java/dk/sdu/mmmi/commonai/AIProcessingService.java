/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.commonai;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;

/**
 *
 * @author oliver
 */
public interface AIProcessingService {
    Entity calculateClosetsTower(World world, PositionPart enemyPositionPart);
    float distance(PositionPart entity1, PositionPart entity2);
}
