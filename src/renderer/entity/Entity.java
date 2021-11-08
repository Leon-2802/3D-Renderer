package renderer.entity;

import java.awt.Graphics;
import java.util.List;

import renderer.point.MyVector;
import renderer.shapes.Polyhedron;

public class Entity implements IEntity { //Erstellt aus vielen Tetrahedrons eine Entity, also ein zusammenhängendes Objekt

    private List<Polyhedron> polyhedra;

    public Entity(List<Polyhedron> polyhedra) {
        this.polyhedra = polyhedra;
    }

    @Override
    public void render(Graphics g) {
        for(Polyhedron tetra : this.polyhedra) {
            tetra.Render(g);
        }
        
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        for(Polyhedron tetra : this.polyhedra) {
            tetra.Rotate(CW, xDegrees, yDegrees, zDegrees, lightVector);
        }
        
    }

    @Override
    public void setLighting(MyVector lightVector) {
        for(Polyhedron tetra : this.polyhedra) {
            tetra.setLighting(lightVector);
        }
    }
    
}