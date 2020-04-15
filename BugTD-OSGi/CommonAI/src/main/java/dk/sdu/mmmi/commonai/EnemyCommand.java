package dk.sdu.mmmi.commonai;


import dk.sdu.mmmi.commonmap.Tile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author oliver
 */
public class EnemyCommand {
    Tile tile;
    Command command;

    public EnemyCommand(Tile tile, Command command) {
        this.tile = tile;
        this.command = command;
    }
}
