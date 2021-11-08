package renderer.controls;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import renderer.Display;

public class ControlPanelValues implements ChangeListener {

    JSpinner number;
    Display display;

    public ControlPanelValues(JSpinner number, Display display) {
        this.number = number;
        this.display = display;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(number.getName().equals("0")) {
            display.x = (int) number.getValue();
            System.out.println("0");
        }
        if(number.getName().equals("1"))
            display.y = (int) number.getValue();
        if(number.getName().equals("2"))
            display.z = (int) number.getValue();
        
    }
    
}// https://www.tabnine.com/code/java/methods/javax.swing.SpinnerNumberModel/getValue