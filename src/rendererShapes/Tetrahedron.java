package rendererShapes;

import java.awt.Color;
import java.awt.Graphics;

public class Tetrahedron {
    
    private MyPolygon[] polygons;
    private Color color;

    public Tetrahedron(Color color, MyPolygon... polygons) {
        this.color = color;
        this.polygons = polygons;
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

    public void Rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for(MyPolygon p : this.polygons) {
            p.Rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        this.SortPolygons();
    }

    private void SortPolygons() {
        MyPolygon.sortPolygons(this.polygons);
    }

    private void SetPolygonColor() {
        for(MyPolygon poly : this.polygons) {
            poly.SetColor(this.color);
        }
    }
}