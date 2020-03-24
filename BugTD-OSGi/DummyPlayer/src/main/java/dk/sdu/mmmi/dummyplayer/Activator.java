package dk.sdu.mmmi.dummyplayer;

import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        DummyPlayerPlugin dpp = new DummyPlayerPlugin();
        context.registerService(IGamePluginService.class, dpp, null);
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
