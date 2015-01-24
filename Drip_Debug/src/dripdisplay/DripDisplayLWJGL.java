/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dripdisplay;

import drip.Decoration;
import drip.DefaultCharacter;
import drip.Environment;
import drip.HealingScenery;
import drip.PermeableScenery;
import drip.Player;
import drip.Scenery;
import drip.Threat;
import drip.Tree;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

/**
 *
 * @author Dan Ford
 */
public class DripDisplayLWJGL {

    Environment e;
    private TextureMapper levelmap = null;
    private TextureMapper playermap = null;
    private TextureMapper scenerymap = null;
    private TextureMapper threatmap = null;
    private TextureMapper healthmap = null;
    private TextureMapper numbermap = null;
    private TextureMapper menumap = null;
    private TextureMapper healingmap = null;
    private TextureMapper temperaturemap = null;
    private TextureMapper leveleditormap = null;
    private TextureMapper interactivemap = null;
    private TextureMapper decorationmap = null;
    private TextureMapper treemap = null;

    /* choose between level editor or game */
    public boolean LEVEL_EDITOR = false;
    
    /* time at last frame */
    long lastFrame;

    /* frames per second */
    int fps;

    /* last fps time */
    long lastFPS;
    
    private float BACK_SCROLL = 1f;
    private float FORE_SCROLL = 1f;
    
    private int counter = 0;
    
    private void initTextures() {

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        levelmap = new TextureMapper();

        levelmap.initSheet(this.getClass().getResource("/dripimages/cavebackground_6level_spritesheet_4_12_2014.png"), "PNG");
        levelmap.addSpriteLocation("cavebackground", new Rectangle2D.Float(.439f, .2944f, .219f, .294f));
        levelmap.addSpriteLocation("caveforeground", new Rectangle2D.Float(.66f, 0f, .219f, .294f));
        levelmap.addSpriteLocation("cavebackgrounddark", new Rectangle2D.Float(0f, .608f, .219f, .294f));
        levelmap.addSpriteLocation("icebackground", new Rectangle2D.Float(.439f, 0f, .219f, .294f));
        levelmap.addSpriteLocation("halficebackground", new Rectangle2D.Float(.22f, .607f, .219f, .294f));
        levelmap.addSpriteLocation("largebackground", new Rectangle2D.Float(.01f, .01f, .428f, .595f));
        levelmap.addSpriteLocation("shade", new Rectangle2D.Float(.11f, .63f, .1f, .05f));
        levelmap.addSpriteLocation("empty", new Rectangle2D.Float(.667f, 0.63f, .1f, .1f));
        
        playermap = new TextureMapper();
       
        playermap.initSheet(this.getClass().getResource("/dripimages/drip_spritesheet_11_11_2014.png"), "PNG");        
        playermap.addSpriteLocation("drippyL", new Rectangle2D.Float(0f, .5f, .25f, .5f));
        playermap.addSpriteLocation("drippyR", new Rectangle2D.Float(.25f, .5f, .25f, .5f));
        playermap.addSpriteLocation("icey", new Rectangle2D.Float(.5f, 0f, .25f, .5f));
        playermap.addSpriteLocation("cloudyL", new Rectangle2D.Float(0f, 0f, .25f, .5f));
        playermap.addSpriteLocation("cloudyR", new Rectangle2D.Float(.25f, 0f, .25f, .5f));
        
        scenerymap = new TextureMapper();

        scenerymap.initSheet(this.getClass().getResource("/dripimages/scenery_spritesheet_4_12_2014.png"), "PNG");
        scenerymap.addSpriteLocation("mossflat", new Rectangle2D.Float(0f, 0f, .298f, .064f));
        scenerymap.addSpriteLocation("mosscurve", new Rectangle2D.Float(0f, .0643f, .298f, .064f));
        scenerymap.addSpriteLocation("platform", new Rectangle2D.Float(.001f, .13f, .297f, .049f));
        scenerymap.addSpriteLocation("iceplatform", new Rectangle2D.Float(0f, .18f, .3f, .025f));
        scenerymap.addSpriteLocation("fatplatform", new Rectangle2D.Float(0f, .205f, .295f, .075f));
        scenerymap.addSpriteLocation("largestalactite", new Rectangle2D.Float(0f, 0.2932f, .29f, .5198f));
        scenerymap.addSpriteLocation("largestalagmite", new Rectangle2D.Float(.3f, 0f, .285f, .515f));
        scenerymap.addSpriteLocation("smallstalagmite", new Rectangle2D.Float(.267f, .518f, .238f, .33f));
        scenerymap.addSpriteLocation("smallstalactite", new Rectangle2D.Float(.585f, 0f, .238f, .33f));
        scenerymap.addSpriteLocation("squareplatform", new Rectangle2D.Float(.588f, .331f, .195f, .195f));
        scenerymap.addSpriteLocation("leftmoss", new Rectangle2D.Float(.5f, .5275f, .181f, .064f));
        scenerymap.addSpriteLocation("rightmoss", new Rectangle2D.Float(.5f, .5917f, .181f, .0643f));
        
        treemap = new TextureMapper();

        treemap.initSheet(this.getClass().getResource("/dripimages/moredecoration_spritesheet_3_12_2014.png"), "PNG");
        treemap.addSpriteLocation("tree1", new Rectangle2D.Float(.932f, 0f, .07f, .2f));
        treemap.addSpriteLocation("tree2", new Rectangle2D.Float(.327f, 0f, .213f, .543f));
        treemap.addSpriteLocation("tree3", new Rectangle2D.Float(0f, 0f, .3255f, .99f));
        treemap.addSpriteLocation("root", new Rectangle2D.Float(.737f, .575f, .027f, .3f));
        
        interactivemap = new TextureMapper();
        
        interactivemap.initSheet(this.getClass().getResource("/dripimages/interactive_spritesheet_22_11_2014.png"), "PNG");
        interactivemap.addSpriteLocation("iceright", new Rectangle2D.Float(0f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("rockright", new Rectangle2D.Float(.1325f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("iceleft", new Rectangle2D.Float(.265f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("rockleft", new Rectangle2D.Float(.3975f, 0f, .1325f, .85f));
        
        decorationmap = new TextureMapper();
        
        decorationmap.initSheet(this.getClass().getResource("/dripimages/decoration_spritesheet_18_11_2014.png"), "PNG");
        decorationmap.addSpriteLocation("grass", new Rectangle2D.Float(0f, 0f, .582f, .0445f));
        decorationmap.addSpriteLocation("shroom1", new Rectangle2D.Float(0f, .05f, .205f, .162f));
        decorationmap.addSpriteLocation("shroom2", new Rectangle2D.Float(0f, .212f, .205f, .162f));
        decorationmap.addSpriteLocation("shroom3", new Rectangle2D.Float(.205f, .0483f, .21f, .165f));
        decorationmap.addSpriteLocation("shroom4", new Rectangle2D.Float(.205f, .216f, .21f, .165f));
        decorationmap.addSpriteLocation("grass1", new Rectangle2D.Float(.415f, .05f, .153f, .37f));
        decorationmap.addSpriteLocation("grass2", new Rectangle2D.Float(.588f, 0f, .153f, .37f));
        decorationmap.addSpriteLocation("grass3", new Rectangle2D.Float(.75f, 0f, .153f, .37f));
        decorationmap.addSpriteLocation("grass4", new Rectangle2D.Float(0f, .375f, .1512f, .37f));
        decorationmap.addSpriteLocation("shroom", new Rectangle2D.Float(.153f, .375f, .1506f, .162f));
        
        threatmap = new TextureMapper();
        
        threatmap.initSheet(this.getClass().getResource("/dripimages/fireice_spritesheet_16_11_2014.png"), "PNG");
        threatmap.addSpriteLocation("fire1", new Rectangle2D.Float(.00f, 0f, .155f, .403f));
        threatmap.addSpriteLocation("fire2", new Rectangle2D.Float(.156f, 0f, .155f, .403f));
        threatmap.addSpriteLocation("fire3", new Rectangle2D.Float(.312f, 0f, .154f, .403f));
        threatmap.addSpriteLocation("ice1", new Rectangle2D.Float(.47f, 0f, .15f, .41f));
        threatmap.addSpriteLocation("ice2", new Rectangle2D.Float(.0f, .4f, .15f, .41f));
        threatmap.addSpriteLocation("ice3", new Rectangle2D.Float(.1f, .4f, .15f, .41f));

        healingmap = new TextureMapper();
        
        healingmap.initSheet(this.getClass().getResource("/dripimages/pooldrip_spritesheet_16_11_2014.png"), "PNG");
        healingmap.addSpriteLocation("pool1", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool2", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool3", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool4", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool5", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("drop1", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop2", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop3", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop4", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop5", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop6", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop7", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        
        healthmap = new TextureMapper();

        healthmap.initSheet(this.getClass().getResource("/dripimages/waterhealth_spritesheet_11_11_2014.png"), "PNG");
        healthmap.addSpriteLocation("10", new Rectangle2D.Float(0f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("20", new Rectangle2D.Float(0f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("30", new Rectangle2D.Float(.25f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("40", new Rectangle2D.Float(.5f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("50", new Rectangle2D.Float(.5f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("60", new Rectangle2D.Float(.75f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("70", new Rectangle2D.Float(.75f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("80", new Rectangle2D.Float(0f, .5f, .25f, .25f));
        healthmap.addSpriteLocation("90", new Rectangle2D.Float(0f, .75f, .25f, .25f));
        healthmap.addSpriteLocation("100", new Rectangle2D.Float(.25f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("0", new Rectangle2D.Float(.9f, .9f, .01f, .01f));
        healthmap.addSpriteLocation("porthole", new Rectangle2D.Float(.25f, .5f, .25f, .25f));
        
        numbermap = new TextureMapper();

        numbermap.initSheet(this.getClass().getResource("/dripimages/number_spritesheet_11_11_2014.png"), "PNG");
        numbermap.addSpriteLocation("0", new Rectangle2D.Float(0f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("1", new Rectangle2D.Float(.21f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("2", new Rectangle2D.Float(.42f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("3", new Rectangle2D.Float(.63f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("4", new Rectangle2D.Float(0f, .206f, .2f, .2f));
        numbermap.addSpriteLocation("5", new Rectangle2D.Float(.21f, .206f, .2f, .2f));
        numbermap.addSpriteLocation("6", new Rectangle2D.Float(.42f, .206f, .2f, .206f));
        numbermap.addSpriteLocation("7", new Rectangle2D.Float(.63f, .206f, .2f, .206f));
        numbermap.addSpriteLocation("8", new Rectangle2D.Float(0f, .41f, .2f, .206f));
        numbermap.addSpriteLocation("9", new Rectangle2D.Float(.21f, .41f, .2f, .206f));
        numbermap.addSpriteLocation("blank", new Rectangle2D.Float(.63f, .41f, .2f, .206f));
        
        temperaturemap = new TextureMapper();

        temperaturemap.initSheet(this.getClass().getResource("/dripimages/temperature_spritesheet_17_11_2014.png"), "PNG");
        temperaturemap.addSpriteLocation("temp10", new Rectangle2D.Float(0f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp20", new Rectangle2D.Float(.18f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp30", new Rectangle2D.Float(.36f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp40", new Rectangle2D.Float(.54f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp50", new Rectangle2D.Float(0f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp60", new Rectangle2D.Float(.18f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp70", new Rectangle2D.Float(.36f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp80", new Rectangle2D.Float(.54f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp90", new Rectangle2D.Float(0f, .576f, .18f, .28f));
        
        menumap = new TextureMapper();
        
        menumap.initSheet(this.getClass().getResource("/dripimages/menu_spritesheet_1_12_2014.png"), "PNG");
        menumap.addSpriteLocation("levelcomplete", new Rectangle2D.Float(0f, 0f, .5f, .15f));
        menumap.addSpriteLocation("darken", new Rectangle2D.Float(0f, .2f, .1f, .1f));
        menumap.addSpriteLocation("paused", new Rectangle2D.Float(0f, .44f, .39f, .27f));
        menumap.addSpriteLocation("pressspacedark", new Rectangle2D.Float(0f, .715f, .35f, .0618f));
        menumap.addSpriteLocation("pressspacelight", new Rectangle2D.Float(0f, .78f, .35f, .0601f));
        menumap.addSpriteLocation("optionsdark", new Rectangle2D.Float(0f, .84f, .17f, .12f));
        menumap.addSpriteLocation("optionslight", new Rectangle2D.Float(0.17f, 0.84f, .17f, .12f));
        menumap.addSpriteLocation("quitlight", new Rectangle2D.Float(0.64f, .695f, .09f, .13f));
        menumap.addSpriteLocation("quitdark", new Rectangle2D.Float(0.5f, 0f, .09f, .13f));
        menumap.addSpriteLocation("d", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("r", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("drip", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("i", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("p", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        
        leveleditormap = new TextureMapper();
       
        leveleditormap.initSheet(this.getClass().getResource("/dripimages/leveleditor_spritesheet_22_11_2014.png"), "PNG");        
        leveleditormap.addSpriteLocation("bottombar", new Rectangle2D.Float(0f, 0f, .78f, .195f));
        leveleditormap.addSpriteLocation("sidebar", new Rectangle2D.Float(0f, .195f, .195f, .78f));
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL11.GL_REPEAT);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    protected void initGLLWJGL() {
        
        if (!LEVEL_EDITOR){
            GL11.glViewport(0, 0, 800, 600);
        } else {
            GL11.glViewport(0, 0, 1000, 800);
        }
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        if (!LEVEL_EDITOR){
            GL11.glOrtho(0, 800, 600, 0, 1, -1);
        } else {
            GL11.glOrtho(0, 1000, 800, 0, 1, -1);
        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        initTextures();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        createModel();
    }

    protected void createModel() {
        e = new Environment(LEVEL_EDITOR);
    }

    protected void renderGL(Point locs) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glPushMatrix();
        {
            drawBackground(locs);
            drawForeGround();
            if (e.LEVEL_COMPLETE == false){
                drawShadow(locs);
            }
            drawUI();
        }
        GL11.glPopMatrix();
    }
    
    protected void startLWJGL(){
        try{
            if (!LEVEL_EDITOR){
                Display.setDisplayMode(new DisplayMode(800, 600));
            } else {
                Display.setDisplayMode(new DisplayMode(1000, 800));
            }
            Display.create();
	} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
        }
        Display.setTitle("Drip");
     //	Display.setResizable(true);
        initGLLWJGL();
        getDelta(); //initialize the last frame
        lastFPS = getTime();

        // GAME INTRO HERE
        //intro();

        while( !Display.isCloseRequested()){
                int delta = getDelta();
                menuOptions();
                Point locs = onClockTick(delta);
                renderGL(locs);
                if (e.LEVEL_COMPLETE){
                    showLevelComplete();
                    if (e.CLICKED_CONTINUE == true){
                        loadNextLevel();
                    }
                }
                
                Display.update();
                Display.sync(60);
        }
        Display.destroy();
    }
    
    private void drawBackground(Point locs) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        levelmap.getSheetID());
        drawTile(e.GAME_BACKGROUND_RECT, levelmap.getSpriteLocation(e.GAME_BACKGROUND)); 
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        levelmap.getSheetID());
        drawTile(e.GAME_FOREGROUND_RECT, levelmap.getSpriteLocation(e.GAME_FOREGROUND));
    }

    private void drawForeGround() {
        String r;
        Rectangle bounds;
        ArrayList<Scenery> sceneries = e.getSceneries();
        for (DefaultCharacter scenery : sceneries) {
            if(scenery.getEditor() == false){
                r = scenery.getTexture();
                bounds = scenery.getScreenPosition();
                if (scenery instanceof HealingScenery){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            healingmap.getSheetID());

                    drawTile(bounds, healingmap.getSpriteLocation(r));
                } else if (scenery instanceof PermeableScenery){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            interactivemap.getSheetID());
                    
                    drawTile(bounds, interactivemap.getSpriteLocation(r));
                } else if (scenery instanceof Tree){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            treemap.getSheetID());
                    
                    drawTile(bounds, treemap.getSpriteLocation(r));
                } else if (scenery instanceof Decoration){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            decorationmap.getSheetID());
                    
                    drawTile(bounds, decorationmap.getSpriteLocation(r));
                } else {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            scenerymap.getSheetID());

                    drawTile(bounds, scenerymap.getSpriteLocation(r));
                }
            }
        }
        if(!LEVEL_EDITOR){
            ArrayList<Player> characters = e.getCharacters();
            for (DefaultCharacter character : characters) {
                r = character.getTexture();
                bounds = character.getScreenPosition();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        playermap.getSheetID());

                drawTile(bounds, playermap.getSpriteLocation(r));
            }
        }
        ArrayList<Threat> threats = e.getThreats();
        for (DefaultCharacter threat : threats) {
            if(threat.getEditor() == false){
                r = threat.getTexture();
                bounds = threat.getScreenPosition();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        threatmap.getSheetID());

                drawTile(bounds, threatmap.getSpriteLocation(r));
            }
        }
    }
    
    private void drawShadow(Point locs) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        levelmap.getSheetID());
        drawTile(e.GAME_SHADOW_RECT, levelmap.getSpriteLocation(e.GAME_SHADOW));
    }
    
    private void drawUI() {
        if (!LEVEL_EDITOR){
            int health = e.getPlayerHealth();
            String texture, firstNumberTexture, secondNumberTexture;
            if (health == 1000){
                texture = "100";
            } else if (health >= 900){
                texture = "90";
            } else if (health >= 800){
                texture = "80";
            } else if (health >= 700){
                texture = "70";
            } else if (health >= 600){
                texture = "60";
            } else if (health >= 500){
                texture = "50";
            } else if (health >= 400){
                texture = "40";
            } else if (health >= 300){
                texture = "30";
            } else if (health >= 200){
                texture = "20";
            } else if (health >= 100){
                texture = "10";
            } else {
                texture = "0";
            }
            if (health > 100){
                Rectangle bounds = new Rectangle(10, 10, 64, 64);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                healthmap.getSheetID());
                drawTile(bounds, healthmap.getSpriteLocation(texture)); 
            }
            Rectangle bounds2 = new Rectangle(10, 10, 64, 64);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            healthmap.getSheetID());
            drawTile(bounds2, healthmap.getSpriteLocation("porthole"));

            int healthTens = (health/100)%100;
            switch (healthTens){
                case 1:
                    firstNumberTexture = "1";
                    break;
                case 2:
                    firstNumberTexture = "2";
                    break;
                case 3:
                    firstNumberTexture = "3";
                    break;
                case 4:
                    firstNumberTexture = "4";
                    break;
                case 5:
                    firstNumberTexture = "5";
                    break;
                case 6:
                    firstNumberTexture = "6";
                    break;
                case 7:
                    firstNumberTexture = "7";
                    break;
                case 8:
                    firstNumberTexture = "8";
                    break;
                case 9:
                    firstNumberTexture = "9";
                    break;
                case 0:
                    firstNumberTexture = "0";
                    break;
                default:
                    if (health == 1000){
                        firstNumberTexture = "0";
                    } else {
                        firstNumberTexture = "blank";
                    }
                    break;
            }
            int healthOnes = (health%100)/10;
            switch (healthOnes){
                case 1:
                    secondNumberTexture = "1";
                    break;
                case 2:
                    secondNumberTexture = "2";
                    break;
                case 3:
                    secondNumberTexture = "3";
                    break;
                case 4:
                    secondNumberTexture = "4";
                    break;
                case 5:
                    secondNumberTexture = "5";
                    break;
                case 6:
                    secondNumberTexture = "6";
                    break;
                case 7:
                    secondNumberTexture = "7";
                    break;
                case 8:
                    secondNumberTexture = "8";
                    break;
                case 9:
                    secondNumberTexture = "9";
                    break;
                case 0:
                    secondNumberTexture = "0";
                    break;
                default:
                    secondNumberTexture = "0";

            }
            if (health != 1000){
                        Rectangle num1 = new Rectangle(29, 28, 13, 26);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                numbermap.getSheetID());
                drawTile(num1, numbermap.getSpriteLocation(firstNumberTexture));

                Rectangle num2 = new Rectangle(42, 28, 13, 26);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                numbermap.getSheetID());
                drawTile(num2, numbermap.getSpriteLocation(secondNumberTexture));
            }

                int temper = e.getPlayerTemperature();
                String textemp;
                if (temper >= 800){
                    textemp = "temp90";                
                } else if (temper >= 700){
                    textemp = "temp80";                
                } else if (temper >= 600){
                    textemp = "temp70";                
                } else if (temper >= 500){
                    textemp = "temp60";                
                } else if (temper >= 400){
                    textemp = "temp50";                
                } else if (temper >= 350){
                    textemp = "temp40";                
                } else if (temper >= 300){
                    textemp = "temp30";                
                } else if (temper >= 200){
                    textemp = "temp20";                
                } else if (temper >= 0){
                    textemp = "temp10";                
                } else {
                    textemp = "temp10";
                } 

                Rectangle temperature = new Rectangle(16, 88, 23, 74);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                temperaturemap.getSheetID());
                drawTile(temperature, temperaturemap.getSpriteLocation(textemp));
        } else {
            /**
             * THIS IS WHERE LEVEL EDITOR SIDE BARS GO
             */
            Rectangle bottombar = new Rectangle(0, 600, 800, 200);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        leveleditormap.getSheetID());
            drawTile(bottombar, leveleditormap.getSpriteLocation("bottombar"));
            
            Rectangle sidebar = new Rectangle(800, 0, 200, 800);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        leveleditormap.getSheetID());
            drawTile(sidebar, leveleditormap.getSpriteLocation("sidebar"));
            
            String r;
            Rectangle bounds;
            ArrayList<Scenery> sceneries = e.getSceneries();
            for (DefaultCharacter scenery : sceneries) {
                if(scenery.getEditor() == true){
                    r = scenery.getTexture();
                    bounds = scenery.getScreenPosition();
                    if (scenery instanceof HealingScenery){
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                healingmap.getSheetID());

                        drawTile(bounds, healingmap.getSpriteLocation(r));
                    }else if (scenery instanceof PermeableScenery){
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                                interactivemap.getSheetID());
                    
                        drawTile(bounds, interactivemap.getSpriteLocation(r));
                    } else if (scenery instanceof Tree){
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            treemap.getSheetID());
                    
                        drawTile(bounds, treemap.getSpriteLocation(r));
                    } else if (scenery instanceof Decoration){
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            decorationmap.getSheetID());
                    
                        drawTile(bounds, decorationmap.getSpriteLocation(r));
                    } else {
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                scenerymap.getSheetID());

                        drawTile(bounds, scenerymap.getSpriteLocation(r));
                    }
                }
            }
            if(!LEVEL_EDITOR){
                ArrayList<Player> characters = e.getCharacters();
                for (DefaultCharacter character : characters) {
                    r = character.getTexture();
                    bounds = character.getScreenPosition();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            playermap.getSheetID());

                    drawTile(bounds, playermap.getSpriteLocation(r));
                }
            }
            ArrayList<Threat> threats = e.getThreats();
            for (DefaultCharacter threat : threats) {
                if(threat.getEditor() == true){
                    r = threat.getTexture();
                    bounds = threat.getScreenPosition();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            threatmap.getSheetID());

                    drawTile(bounds, threatmap.getSpriteLocation(r));
                }
            }
        }
         if (e.getPaused() == true){
            Rectangle grey = new Rectangle(0, 0, 800, 600);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        menumap.getSheetID());
            drawTile(grey, menumap.getSpriteLocation("darken"));
            
            Rectangle title = new Rectangle(300, 220, 200, 72);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        menumap.getSheetID());
            drawTile(title, menumap.getSpriteLocation("paused"));
            
            Rectangle options = new Rectangle(357, 300, 86, 28);
            if (checkHover(options) == true){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(options, menumap.getSpriteLocation("optionslight"));
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(options, menumap.getSpriteLocation("optionsdark"));
            }
            
            Rectangle quit = new Rectangle(377, 335, 46, 32);
            if (checkHover(quit) == true){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(quit, menumap.getSpriteLocation("quitlight"));
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(quit, menumap.getSpriteLocation("quitdark"));
            }
        }
    }

    private void drawTile(Rectangle bounds, Rectangle2D.Float r) {
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(r.x, r.y + r.height);
        GL11.glVertex2f(bounds.getX(), bounds.getY() + bounds.getHeight());

        GL11.glTexCoord2f(r.x + r.width, r.y + r.height);
        GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());

        GL11.glTexCoord2f(r.x + r.width, r.y);
        GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY());

        GL11.glTexCoord2f(r.x, r.y);
        GL11.glVertex2f(bounds.getX(), bounds.getY());

        GL11.glEnd();
    }

    protected void destroyGL() {
       /**/
    }

    protected Point onClockTick(int delta) {
        Point locs = e.onClockTick(delta);
        updateFPS(delta); // update FPS Counter
        return locs;
    }

    protected void resetGL() {
        GL11.glViewport(0, 0, 800, 600);
    }

    public int getDelta(){
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public void updateFPS(int delta){
        if (getTime() - lastFPS > 1000){
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    /**
     * Get the accurate time system
     * 
     * @return 
     */
    public long getTime(){		
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public static void main(String[] args) {
        DripDisplayLWJGL gl = new DripDisplayLWJGL();
        gl.startLWJGL();
    }

    private void menuOptions() {
        while(Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                if (e.getPaused() == false){
                    e.pause();
                } else {
                    e.unpause();
                }
            }
            if (e.LEVEL_COMPLETE == true){
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                    e.CLICKED_CONTINUE = true;
                }
            }
        }
    }

    private void showLevelComplete() {
        
        Rectangle title = new Rectangle(272, counter, 256, 39);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                    menumap.getSheetID());
        drawTile(title, menumap.getSpriteLocation("levelcomplete"));
        
        Rectangle pressspace = new Rectangle(311, counter+50, 179, 16);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                    menumap.getSheetID());
        drawTile(pressspace, menumap.getSpriteLocation("pressspacelight"));
        if (counter < 150){
            counter++;
        }
    }

    private void loadNextLevel() {
        e.level++;
        e.clearLevel();
        e.CLICKED_CONTINUE = false;
        e.LEVEL_COMPLETE = false;
        
    }
    
    private boolean checkHover(Rectangle rect) {
        int mousex = Mouse.getX();
        int mousey = 600-Mouse.getY();
        if (mousex >= rect.getX()
                && mousex <= rect.getX() + rect.getWidth()
                && mousey >= rect.getY()
                && mousey <= rect.getY() + rect.getHeight()){
             return true;
        }     
        return false;
    }
}
