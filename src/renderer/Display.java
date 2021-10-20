package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
// import java.awt.Toolkit;

import entity.EntityManager;
import rendererInput.Mouse;

public class Display extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "3D Renderer";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 800;
    private static boolean running = false;

    private EntityManager entityManager;

    private Mouse mouse;
    private boolean automaticRot = true;
    double x, y, z;

    public Display() {
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // this.setSize(screenSize.width, screenSize.height);

        this.mouse = new Mouse();

        this.entityManager = new EntityManager();

        this.addMouseListener(this.mouse); //Aus Canvas-Library
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);
        display.Start();
    }

    public synchronized void Start() {
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();

        chooseInput();
    }

    private void chooseInput() {
        String input;
        System.out.println("Enter '0' if you want to control the rotation with Mouse, Enter '1' if you want automatic rotation");
        input = System.console().readLine();
        int rotation = Integer.parseInt(input);

        if(rotation == 0) {
            automaticRot = false;
        }
        else if(rotation == 1){
            System.out.println("Enter the x, y and z values for the automatic rotation");
            x = Double.parseDouble(System.console().readLine());
            y = Double.parseDouble(System.console().readLine());
            z = Double.parseDouble(System.console().readLine());
            automaticRot = true;

        }
        else {
            System.out.println("Only use '0' and '1'");
        }
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