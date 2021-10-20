package entity;

import java.awt.Graphics;
import java.util.List;

import rendererShapes.Tetrahedron;

public class Entity implements IEntity { //Erstellt aus vielen Tetrahedrons eine Entity, also ein zusammenhängendes Objekt

    private List<Tetrahedron> tetrahedrons;

    public Entity(List<Tetrahedron> tetrahedrons) {
        this.tetrahedrons = tetrahedrons;
    }

    @Override
    public void render(Graphics g) {
        for(Tetrahedron tetra : this.tetrahedrons) {
            tetra.Render(g);
        }
        
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for(Tetrahedron tetra : this.tetrahedrons) {
            tetra.Rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        
    }
    
}