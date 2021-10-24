package rendererShapes;

import java.awt.Color;
import java.awt.Graphics;

import rendererPoint.MyVector;

public class Tetrahedron {
    
    private MyPolygon[] polygons;
    private Color color;

    public Tetrahedron(Color color, Boolean decay, MyPolygon... polygons) {
        this.color = color;
        this.polygons = polygons;
        if(decay)
            this.SetDecayingPolygonColor();
        else
            this.SetPolygonColor();
    }
    public Tetrahedron(MyPolygon... polygons) {
        this.color = Color.WHITE;
        this.polygons = polygons;
    }

    public void Render(Graphics g) {
        for(MyPolygon poly : this.polygons) {
            poly.Render(g);
        }
    }

    public void Rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        for(MyPolygon p : this.polygons) {
            p.Rotate(CW, xDegrees, yDegrees, zDegrees, lightVector);
        }
        this.SortPolygons();
    }

    public void setLighting(MyVector lightVector) {
        for(MyPolygon p : this.polygons) {
            p.setLighting(lightVector);
        }
    }

    private void SortPolygons() {
        MyPolygon.sortPolygons(this.polygons);
    }

    private void SetPolygonColor() {
        for(MyPolygon poly : this.polygons) {
            poly.SetColor(this.color);
        }
    }

    private void SetDecayingPolygonColor() {
        double decayFactor = 0.95;
        for(MyPolygon poly : this.polygons) {
            poly.SetColor(this.color);
            int r = (int) (this.color.getRed() * decayFactor);
            int g = (int) (this.color.getGreen() * decayFactor);
            int b = (int) (this.color.getBlue() * decayFactor);
            this.color = new Color(r, g, b);
        }
    }
}