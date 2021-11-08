package renderer.builder;

import renderer.entity.Entity;
import renderer.entity.IEntity;
import renderer.point.MyPoint;
import renderer.shapes.MyPolygon;
import renderer.shapes.Polyhedron;
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
        Polyhedron tetra = new Polyhedron(
            new MyPolygon(Color.BLUE, p1, p2, p3 , p4),
            new MyPolygon(Color.WHITE, p1, p2, p6 , p5),
            new MyPolygon(Color.YELLOW, p1, p5, p8 , p4),
            new MyPolygon(new Color(255, 140, 26), p2, p6, p7 , p3),
            new MyPolygon(Color.GREEN, p4, p3, p7 , p8),
            new MyPolygon(Color.RED, p5, p6, p7 , p8));

        List<Polyhedron> tetras = new ArrayList<Polyhedron>();
        tetras.add(tetra);

        return new Entity(tetras);
    }

    public static IEntity createDiamond(Color color, double size, double centerX, double centerY, double centerZ) {

        List<Polyhedron> tetras = new ArrayList<Polyhedron>();
        
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

        Polyhedron tetra = new Polyhedron(color, false, polygons);
        tetras.add(tetra);

        return new Entity(tetras);
    }

    public static IEntity createSphere(Color color, int resolution, double size, double centerX, double centerY, double centerZ) {
        List<Polyhedron> polyhedrons = new ArrayList<Polyhedron>();
        List<MyPolygon> polygons = new ArrayList<MyPolygon>(); //Über Liste so viele Polygons wie gewollt hinzufügen

        MyPoint bottom = new MyPoint(centerX, centerY, centerZ - size/2);
        MyPoint top = new MyPoint(centerX, centerY, centerZ + size/2);

        MyPoint[][] points = new MyPoint[resolution - 1][resolution];

        for(int i = 1; i < resolution; i++) { //Lässt top und bottom aus, daher start bei 1, ende bei i < resolution
            double theta = Math.PI / resolution * i; //
            double zPos = -Math.cos(theta) * size / 2; //
            double currentRadius = Math.abs(Math.sin(theta) * size/2); //Gibt den Radius von Mittelpunktachse zu Punkt, daher sin * Hälfte des Durchmessers. Cos wäre die Länge von oben nach unten, vom Mittelpunkt aus
            for(int j = 0; j < resolution; j++) {
                double alpha = 2 * Math.PI/resolution * j;//Ist der Wert, der im Kreis weiter iteriert und einmal im Kreis geht
                double xPos = -Math.sin(alpha) * currentRadius; //Da im Moment das Polygon noch von oben betrachtet wird (z zeigt direkt aus dem Bildschirm), müssen alle drei Koordinatenwerte angepasst werden
                double yPos = Math.cos(alpha) * currentRadius;
                points[i-1][j] = new MyPoint(centerX + xPos, centerY+ yPos, centerZ + zPos); //i-1 weil top und bottom natürlich dazu müssen
            }
        }

        for(int i = 1; i < resolution; i++) {
            for(int j = 0; j < resolution; j++) {
                if(i == 1) { //Bottom part erstellen
                    polygons.add(
                            new MyPolygon(color,
                                    points[i-1][j],
                                    points[i-1][(j+1)%resolution],
                                    bottom));

                }
                else if(i == resolution) {//Top-Cap
                    polygons.add(
                            new MyPolygon(color,
                                    points[i-2][(j+1)%resolution],//Werte für j (Z-Koordinaten) austauschen, da es gespiegelt sein muss
                                    points[i-2][j],
                                    top));
                }
            }
        }

        MyPolygon[] polygonArray = new MyPolygon[polygons.size()];
        polygonArray = polygons.toArray(polygonArray);//Und die Liste dann zu einem Array machen um die Polygons zu nutzen

        Polyhedron polyhedron = new Polyhedron(color, false, polygonArray);
        polyhedrons.add(polyhedron);

        return new Entity(polyhedrons);
    }
    
}