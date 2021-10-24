package rendererShapes;

import rendererPoint.MyPoint;
import rendererPoint.MyVector;
import rendererPoint.PointConverter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPolygon {

    private static final double AmbientLight = 0.05;
    
    private MyPoint[] points;
    private Color baseColor, lightColor;

    public MyPolygon(Color color, MyPoint... points) {
        this.baseColor = this.lightColor = color;
        this.points = new MyPoint[points.length];
        for(int i = 0; i < points.length; i++) {
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public void Render(Graphics g) {
        Polygon poly = new Polygon();
        for(int i = 0; i < points.length; i++) {
            Point p = PointConverter.convertPoint(points[i]);
            poly.addPoint(p.x, p.y);
        }

        g.setColor(this.lightColor);
        g.fillPolygon(poly);
    }

    public void Rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        for(MyPoint p : points) {
            PointConverter.rotateAxisX(p, CW, xDegrees);
            PointConverter.rotateAxisY(p, CW, yDegrees);
            PointConverter.rotateAxisZ(p, CW, zDegrees);
        }

        this.setLighting(lightVector);
    }

    public double GetAverageX() {
        double sum = 0;
        for(MyPoint p : this.points) {
            sum += p.x;
        }

        return sum / this.points.length;
    }

    public void SetColor(Color color) {
        this.baseColor = color;
    }

    public static MyPolygon[] sortPolygons(MyPolygon[] polygons) {
        List<MyPolygon> polygonList = new ArrayList<MyPolygon>();

        for(MyPolygon poly : polygons) {
            polygonList.add(poly);
        }

        Collections.sort(polygonList, new Comparator<MyPolygon>(){
            @Override
            public int compare(MyPolygon p1, MyPolygon p2) {
                return p2.GetAverageX() - p1.GetAverageX() < 0 ? 1 : -1;
            }
        });

        for(int i = 0; i < polygons.length; i++) {
            polygons[i] = polygonList.get(i);
        }

        return polygons;
    }

    public void setLighting(MyVector lightVector) { //setzt die Anpassung der blanken Farbwerte zum Lighting
        if(this.points.length < 3) 
            return;

        MyVector v1 = new MyVector(this.points[0], this.points[1]);
        MyVector v2 = new MyVector(this.points[1], this.points[2]);
        MyVector orthogonal = MyVector.normalize(MyVector.scalar(v2, v1));
        double dot = MyVector.dot(orthogonal, lightVector);
        double sign = dot < 0 ? -1 : 1;
        dot = sign * dot * dot;
        dot = (dot + 1) / 2 * (1 - AmbientLight);

        double lightRatio = Math.min(1, Math.max(0, AmbientLight + dot)); //Verhindert Crashes wo die Farbwerte über 255 oder unter 0 verschoben werden
        this.updateLightingColor(lightRatio);
    }

    private void updateLightingColor( double lightRatio) { //Setzt die endgültige LightingColor

        int red = (int) (this.baseColor.getRed() * lightRatio);
        int green = (int) (this.baseColor.getGreen() * lightRatio);
        int blue = (int) (this.baseColor.getBlue() * lightRatio);
        this.lightColor = new Color(red, green, blue);

    }
}
