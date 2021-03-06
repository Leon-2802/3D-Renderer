package renderer.point;

import java.awt.Point;
import renderer.Display;

public class PointConverter {

    private static double scale = 1;
    private static int perspective = 1400;

    public static final double ZoomFactor = 1.01;
    public static void zoomIn() {
        scale *= ZoomFactor;
    }
    public static void zoomOut() {
        scale /= ZoomFactor;
    }
    
    public static Point convertPoint(MyPoint point3D) {
        double x3d = point3D.y * scale;
        double y3d = point3D.z * scale;
        double depth = point3D.x * scale;
        double[] newVal = scale(x3d, y3d, depth);
        int x2d = (int) (Display.WIDTH / 2 + newVal[0]);
        int y2d = (int) (Display.HEIGHT / 2 - newVal[1]);
        
        Point point2D = new Point(x2d, y2d);
        return point2D;
    }

    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt(x3d*x3d + y3d*y3d);
        double theta = Math.atan2(y3d, x3d);
        double depth2 = 15 - depth; //Kamera 15 weg von Null
        double localScale = Math.abs(perspective/(depth2+perspective)); //Umso höher depth2 ist, umso kleiner und näher am Ursprung ist der Punkt
        dist *= localScale; //Entfernung an Scale angepasst
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(theta);
        newVal[1] = dist * Math.sin(theta);
        return newVal;
    }

    public static void rotateAxisX(MyPoint p, boolean CW, double degrees) { //X-Achse -> Y und Z Werte ändern sich
        double radius = Math.sqrt(p.y*p.y + p.z*p.z); //Radius der aktuellen Position
        double theta = Math.atan2(p.y, p.z); //Winkel zwischen y und z (= y und x in normalen Koordinatensystem)
        theta += 2*Math.PI/360*degrees*(CW?1:-1); //Mal 1 oder -1, abhängig von Drehungsrichtung
        p.y = radius * Math.sin(theta); //Neues y
        p.z = radius * Math.cos(theta); //Neues z
    }
    public static void rotateAxisY(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.x*p.x + p.z*p.z); 
        double theta = Math.atan2(p.x, p.z); 
        theta += 2*Math.PI/360*degrees*(CW?1:-1); 
        p.x = radius * Math.sin(theta); 
        p.z = radius * Math.cos(theta); 
    }
    public static void rotateAxisZ(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y*p.y + p.x*p.x); 
        double theta = Math.atan2(p.y, p.x); 
        theta += 2*Math.PI/360*degrees*(CW?-1:1); 
        p.y = radius * Math.sin(theta); 
        p.x = radius * Math.cos(theta); 
    }


}
