/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drip;

/**
 *
 * @author Dan
 */
public class Tree extends Decoration {

    public Tree(int x, int y, int width, int height, int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    @Override
    public boolean isBloomable(){
        if (getTexture().equals("tree1")||getTexture().equals("tree2")){
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void waterPlant(int amount){
        if (GROWTH < 301){
            GROWTH += amount;
            if (GROWTH < 100 && getTexture().equals("tree1")){
                setTex("tree1");
            }else if (GROWTH < 200 && (getTexture().equals("tree1"))){
                setTex("tree2");
                setRelativeX(getRelativeX()-(441- position.getWidth())/2);
                setRelativeY(getRelativeY()-(557 - position.getHeight())+20);
                position.setWidth(441);
                position.setHeight(557);
            } else if (getTexture().equals("tree2") && GROWTH < 200){
                setTex("tree2");
            }else if (GROWTH < 300 && (getTexture().equals("tree2"))){
                setTex("tree3");
                setRelativeX(getRelativeX()-(667- position.getWidth())/2);
                setRelativeY(getRelativeY()-(1018 - position.getHeight()));
                position.setWidth(667);
                position.setHeight(1018);
                BLOOMED = true;
            } else if (getTexture().equals("tree3")){
                setTex("tree3");
            }
        }
    }
    
}
