/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author mÅrden
 */
public class Activator implements BundleActivator {
    
    @Override
    public void start(BundleContext context) throws Exception {
        GroundEnemy ground = new GroundEnemy();
        context.registerService(IGamePluginService.class, ground, null);
        FlyingEnemy flyBaby = new FlyingEnemy();
        context.registerService(IGamePluginService.class, flyBaby, null);
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        //TODO add deactivation code here
    }
    
}
