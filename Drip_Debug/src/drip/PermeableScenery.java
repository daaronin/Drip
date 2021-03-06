/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drip;

import org.lwjgl.util.Point;


/**
 *
 * @author Dan
 */
public class PermeableScenery extends Scenery{

    public PermeableScenery(Point p, int width, int height) {
        super(p, width, height);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
    }
    
    public PermeableScenery(Point p, int width, int height,  int health) {
        super(p, width, height, health);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
    }
        
    public PermeableScenery(int x, int y, int width, int height,  int health) {
        super(x, y, width, height, health);
        setRelativeX(x);
        setRelativeY(y);
    }

    public PermeableScenery(int x, int y, int width, int height,  int health, State state) {
        super(x, y, width, height, health, state);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public PermeableScenery(int x, int y, int width, int height,  int health, String tex) {
        super(x, y, width, height, health, tex);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public PermeableScenery(int x, int y, int width, int height, int health, int temper, String tex) {
        super(x, y, width, height, health, temper, tex);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public PermeableScenery(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        setRelativeX(x);
        setRelativeY(y);
    }
}
