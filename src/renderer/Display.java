package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controls.ControlPanelMode;
import controls.ControlPanelValues;
import controls.ControlPanelMode;

import java.awt.GridLayout;

import entity.EntityManager;
import rendererInput.Mouse;

public class Display extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;

    private JFrame controlsFrame;
    static JButton[] button = new JButton[2];
    private JPanel panel = new JPanel();
    public static JSpinner[] numberFields = new JSpinner[3];
    JPanel rotationPanel = new JPanel();

    private static String title = "3D Renderer";
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int CONTROLS_WIDTH = 300;
    public static final int CONTROLS_HEIGHT = 100;
    private static boolean running = false;

    private EntityManager entityManager;

    private Mouse mouse;
    public boolean automaticRot = true;
    public int x, y, z;

    public Display() {
        //Hauptdisplay:
        this.frame = new JFrame(title);
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        
        //ControlPanelMode:
        this.controlsFrame = new JFrame("3D Renderer | Controls");
        this.controlsFrame.setLayout(new GridLayout(1, 2, 10, 0));
        panel.setLayout(new GridLayout(2, 4, 5, 0));
        rotationPanel.setLayout(new GridLayout(1, 3, 5, 5));
		controlsFrame.add(panel);
		controlsFrame.add(rotationPanel);

        this.mouse = new Mouse();

        this.entityManager = new EntityManager();

        this.addMouseListener(this.mouse); //Aus Canvas-Library
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.controlsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.controlsFrame.setLocationRelativeTo(null);
        display.controlsFrame.setResizable(false);
        display.controlsFrame.setVisible(true);

        display.Start();
    }

    public synchronized void Start() {
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();

        createControlsFrame();
    }

    private void createControlsFrame() {
        for(int b = 0; b < button.length; b++) {
            button[b] = new JButton();
            button[b].setBackground(Color.WHITE);
            button[b].addActionListener(new ControlPanelMode(button[b], controlsFrame, this));
            panel.add(button[b]);
        }
        button[0].setText("Mouse Controls");
        button[1].setText("Automatic Rotation");

        for(int c = 0; c < numberFields.length; c++) {
            SpinnerModel number = new SpinnerNumberModel(0, 0, 10, 1);
            numberFields[c] = new JSpinner(number);
            numberFields[c].addChangeListener(new ControlPanelValues(numberFields[c], this));
            String name = Integer.toString(c);
            numberFields[c].setName(name);
            rotationPanel.add(numberFields[c]);
        }

        controlsFrame.pack();
    }

    public synchronized void Stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;

        this.entityManager.init();

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                Update(automaticRot);
                delta--;
            }
            Render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }

        Stop();
    }

    private void Render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        this.entityManager.render(g);

        g.dispose();
        bs.show();
    }

    private void Update(boolean automaticRot) {

        if(automaticRot == false)
            this.entityManager.update(this.mouse);
        else
            this.entityManager.automaticUpdate(this.mouse, true, x, y, z);

    }
}