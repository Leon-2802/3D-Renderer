package entity;

import java.util.ArrayList;
import java.util.List;

import builder.BasicEntityBuilder;
import rendererInput.ClickType;
import rendererInput.Mouse;
import rendererPoint.MyVector;
import rendererPoint.PointConverter;

import java.awt.Color;
import java.awt.Graphics;

public class EntityManager {

    private List<IEntity> entities;
    private int initialX, initialY;
    private double mouseSensivity = 2.5;
    private MyVector lightVector = MyVector.normalize(new MyVector(1, 1, 1));

    public EntityManager() {
        this.entities = new ArrayList<IEntity>();
    }
    
    public void init() { //Hier das zu erstellende Objekt initieren!
        this.entities.add(BasicEntityBuilder.createDiamond(Color.YELLOW, 100, 0, 0, 0));
        //this.entities.add(BasicEntityBuilder.createCube(100, 0, 0, 0));
    }

    public void update(Mouse mouse) {
        int x = mouse.getX();
        int y = mouse.getY();
        if(mouse.getButton() == ClickType.LeftClick) {
            int xDif = x - initialX;
            int yDif = y - initialY;

            this.rotate(true, 0, yDif/mouseSensivity, -xDif/mouseSensivity);
        }
        else if(mouse.getButton() == ClickType.RightClick) {
            int xDif = x - initialX;

            this.rotate(true, -xDif/mouseSensivity, 0, 0);
        }

        if(mouse.getButton() == ClickType.ZoomIn) {
            PointConverter.zoomIn();
        }
        else if(mouse.getButton() == ClickType.ZoomOut) {
            PointConverter.zoomOut();
        }

        mouse.resetScroll();

        initialX = x;
        initialY = y;
    }
    public void automaticUpdate(Mouse mouse, boolean direction, double xAngle, double yAngle, double zAngle) {
        this.rotate(direction, xAngle, yAngle, zAngle);

        if(mouse.getButton() == ClickType.ZoomIn) {
            PointConverter.zoomIn();
        }
        else if(mouse.getButton() == ClickType.ZoomOut) {
            PointConverter.zoomOut();
        }

        mouse.resetScroll();
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