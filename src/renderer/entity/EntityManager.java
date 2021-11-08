package renderer.entity;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import renderer.builder.BasicEntityBuilder;
import renderer.input.ClickType;
import renderer.input.Keyboard;
import renderer.input.Mouse;
import renderer.input.UserInput;
import renderer.point.MyVector;
import renderer.point.PointConverter;

import java.awt.Color;
import java.awt.Graphics;

public class EntityManager {

    private List<IEntity> entities;
    private int initialX, initialY;
    private double mouseSensivity = 2.5;
    private MyVector lightVector = MyVector.normalize(new MyVector(1, 1, 1));
    private Mouse mouse;
    private Keyboard keyboard;

    public EntityManager() {
        this.entities = new ArrayList<IEntity>();
    }
    
    public void init(UserInput userInput) { //Hier das zu erstellende Objekt initieren!
        this.mouse = userInput.mouse;
        this.keyboard = userInput.keyboard;
        this.entities.add(BasicEntityBuilder.createDiamond(Color.CYAN, 100, 0, 0, 0));
        //this.entities.add(BasicEntityBuilder.createCube(100, 0, 0, 0));
        //this.entities.add(BasicEntityBuilder.createSphere(Color.GREEN, 100, 400, 0, 0, 0));
        this.setLighting();
    }

    public void update(UserInput userInput) {
        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if(this.mouse.getButton() == ClickType.LeftClick) {
            int xDif = x - initialX;
            int yDif = y - initialY;

            this.rotate(true, 0, yDif/mouseSensivity, -xDif/mouseSensivity);
        }
        else if(this.mouse.getButton() == ClickType.RightClick) {
            int xDif = x - initialX;

            this.rotate(true, -xDif/mouseSensivity, 0, 0);
        }

        if(this.mouse.getButton() == ClickType.ZoomIn) {
            PointConverter.zoomIn();
        }
        else if(this.mouse.getButton() == ClickType.ZoomOut) {
            PointConverter.zoomOut();
        }

        if(this.keyboard.getZoomIn() == true) {
            PointConverter.zoomIn();
        }
        else if(this.keyboard.getZoomOut() == true) {
            PointConverter.zoomOut();
        }

        this.mouse.resetScroll();
        this.keyboard.update();

        initialX = x;
        initialY = y;
    }
    public void automaticUpdate(UserInput userInput, boolean direction, double xAngle, double yAngle, double zAngle) {
        this.rotate(direction, xAngle, yAngle, zAngle);

        if(this.mouse.getButton() == ClickType.ZoomIn) {
            PointConverter.zoomIn();
        }
        else if(this.mouse.getButton() == ClickType.ZoomOut) {
            PointConverter.zoomOut();
        }

        if(this.keyboard.getZoomIn() == true) {
            PointConverter.zoomIn();
        }
        else if(this.keyboard.getZoomOut() == true) {
            PointConverter.zoomOut();
        }

        this.mouse.resetScroll();
        this.keyboard.update();
    }


    public void render(Graphics g) {
        for(IEntity e : this.entities) {
            e.render(g); //1. Rendering alle Tetrahedrons in Entity, 2. Rendering aller Polygons in MyPolygon, 3. Alle Points des Polygons zu 2D konvertiert und gerendert
        }
    }

    private void rotate(boolean direction, double xAngle, double yAngle, double zAngle) {
        for(IEntity entity : this.entities) {
            entity.rotate(direction, xAngle, yAngle, zAngle, this.lightVector);
        }
    }

    private void setLighting() {
        for(IEntity entity : this.entities) {
            entity.setLighting(this.lightVector);
        }
    }
}