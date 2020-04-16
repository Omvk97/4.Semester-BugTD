package dk.sdu.mmmi.osgienemyspawner;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        EnemySpawner enemySpawner = new EnemySpawner();
        context.registerService(IEntityProcessingService.class, enemySpawner, null);
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
