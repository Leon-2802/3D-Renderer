package renderer.entity;

import java.awt.Graphics;

import renderer.point.MyVector;

public interface IEntity {
    
    void render(Graphics g);

    void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector);

    void setLighting(MyVector lightVector);

}