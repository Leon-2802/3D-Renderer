package controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import renderer.Display;

public class ControlPanel implements ActionListener {

    JButton button;
    JFrame frame;
    Display display;

    public ControlPanel(JButton button, JFrame frame, Display display) {
        this.button = button;
        this.frame = frame;
        this.display = display;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(button.getText() == "Mouse Controls") {
            display.automaticRot = false;
            System.out.println("Mouse");
        }
        if(button.getText() == "Automatic Rotation") {
            System.out.println("auto");
        }
        
    }


    
}