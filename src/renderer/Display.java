package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controls.ControlPanel;

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

    private static String title = "3D Renderer";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 800;
    public static final int CONTROLS_WIDTH = 300;
    public static final int CONTROLS_HEIGHT = 100;
    private static boolean running = false;

    private EntityManager entityManager;

    private Mouse mouse;
    public boolean automaticRot = true;
    public double x, y, z;

    public Display() {
        //Hauptdisplay:
        this.frame = new JFrame(title);
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        
        //ControlPanel:
        this.controlsFrame = new JFrame(title);
        this.controlsFrame.setLayout(new GridLayout(1, 2, 10, 0));
        panel.setLayout(new GridLayout(2, 4, 5, 0));
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(2, 2, 5, 5));
		controlsFrame.add(panel);
		controlsFrame.add(scrollPanel);

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

        // chooseInput();
        createControlsFrame();
    }

    private void createControlsFrame() {
        for(int b = 0; b < button.length; b++) {
            button[b] = new JButton();
            button[b].setBackground(Color.WHITE);
            button[b].addActionListener(new ControlPanel(button[b], controlsFrame, this));
            panel.add(button[b]);
        }

        button[0].setText("Mouse Controls");
        button[1].setText("Automatic Rotation");

        controlsFrame.pack();
    }
    // private void chooseInput() {
    //    if(rotation == 1){
    //         System.out.println("Enter the x, y and z values for the automatic rotation");
    //         x = Double.parseDouble(System.console().readLine());
    //         y = Double.parseDouble(System.console().readLine());
    //         z = Double.parseDouble(System.console().readLine());
    //         automaticRot = true;

    //     }
    //     else {
    //         System.out.println("Only use '0' and '1'");
    //     }
    // }

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