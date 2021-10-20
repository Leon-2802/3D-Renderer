package builder;

import entity.Entity;
import entity.IEntity;
import rendererPoint.MyPoint;
import rendererShapes.MyPolygon;
import rendererShapes.Tetrahedron;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BasicEntityBuilder {

    public static IEntity createCube(double s, double centerX, double centerY, double centerZ) {

        MyPoint p1 = new MyPoint(centerX + s/2, centerY -s/2, centerZ -s/2);
        MyPoint p2 = new MyPoint(centerX + s/2, centerY + s/2, centerZ -s/2);
        MyPoint p3 = new MyPoint(centerX + s/2, centerY + s/2, centerZ + s/2);
        MyPoint p4 = new MyPoint(centerX + s/2, centerY -s/2, centerZ + s/2);
        MyPoint p5 = new MyPoint(centerX -s/2, centerY -s/2, centerZ -s/2);
        MyPoint p6 = new MyPoint(centerX -s/2, centerY + s/2, centerZ -s/2);
        MyPoint p7 = new MyPoint(centerX -s/2, centerY + s/2, centerZ + s/2);
        MyPoint p8 = new MyPoint(centerX -s/2, centerY -s/2, centerZ + s/2);
        Tetrahedron tetra = new Tetrahedron( 
            new MyPolygon(Color.BLUE, p1, p2, p3 , p4),
            new MyPolygon(Color.WHITE, p1, p2, p6 , p5),
            new MyPolygon(Color.YELLOW, p1, p5, p8 , p4),
            new MyPolygon(new Color(255, 140, 26), p2, p6, p7 , p3),
            new MyPolygon(Color.GREEN, p4, p3, p7 , p8),
            new MyPolygon(Color.RED, p5, p6, p7 , p8));

        List<Tetrahedron> tetras = new ArrayList<Tetrahedron>();
        tetras.add(tetra);

        return new Entity(tetras);
    }

    public static IEntity createDiamond(Color color, double size, double centerX, double centerY, double centerZ) {

        List<Tetrahedron> tetras = new ArrayList<Tetrahedron>();
        
        int edges = 10;
        double inFactor = 0.8; //Ring innerhalb 80% kleiner als außen
        MyPoint bottom = new MyPoint(centerX, centerY, centerZ - size/2); //Liegt bei Hälfte des Rings
        MyPoint[] outerPoints = new MyPoint[edges]; //Points die auf innerem Ring liegen
        MyPoint[] innerPoints = new MyPoint[edges]; // - " - auf äußerem Ring
        for(int i = 0; i < edges; i++) {
            double theta = 2 * Math.PI / edges * i;
            double xPos = Math.sin(theta) * size/2;
            double yPos = Math.cos(theta) * size/2;
            double zPos = size/2;
            outerPoints[i] = new MyPoint(centerX + xPos, centerY + yPos, centerZ + zPos);
            innerPoints[i] = new MyPoint(centerX + xPos * inFactor, centerY + yPos * inFactor, centerZ + zPos / inFactor);
        }

        MyPolygon polygons[] = new MyPolygon[2 * edges + 1]; //Spitze mit 10 Seiten, das gleiche für den Raum zwischen äußerem und innerem Kreis, und das letzte für den top-part
        for(int i = 0; i < edges; i++) {
            polygons[i] = new MyPolygon(color, outerPoints[i], bottom, outerPoints[(i+1)%edges]); //%edges (modulo) verhindert OutOfBoundsError bei i > edges
        }
        for(int i = 0; i < edges; i++) {
            polygons[i + edges] = new MyPolygon(color, outerPoints[i], outerPoints[(i+1)%edges], innerPoints[(i+1)%edges], innerPoints[i]); //von äußerem Punkt links zu äußerem Rechts, zu innerem Rechts zu innerem Links ein Trapez formen
        }
        polygons[edges * 2] = new MyPolygon(color, innerPoints);//Top-Part, innerhalb der inneren Punkte

        Tetrahedron tetra = new Tetrahedron(color, true, polygons);
        tetras.add(tetra);

        return new Entity(tetras);
    }
    
}