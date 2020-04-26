package dk.sdu.mmmi.commonai.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.events.Event;
import java.util.List;

public class RouteCalculatedEvent extends Event {

    private List<EnemyCommand> enemyCommands;

    public RouteCalculatedEvent(Entity source, List<EnemyCommand> commands) {
        super(source);
        this.enemyCommands = commands;
    }

    public Entity getEnemy() {
        return (Entity) source;
    }

    public List<EnemyCommand> getEnemyCommands() {
        return enemyCommands;
    }
}
