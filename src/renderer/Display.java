package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
// import java.awt.Toolkit;

import rendererInput.ClickType;
import rendererInput.Mouse;
import rendererPoint.MyPoint;
import rendererPoint.PointConverter;
import rendererShapes.MyPolygon;
import rendererShapes.Tetrahedron;

public class Display extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "3D Renderer";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;

    private Tetrahedron tetra;

    private Mouse mouse;

    public Display() {
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // this.setSize(screenSize.width, screenSize.height);

        this.mouse = new Mouse();

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

        Init();

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                Update();
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

    private void Init() {
        int s = 100;
        MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
        MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
        MyPoint p3 = new MyPoint(s/2, s/2, s/2);
        MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
        MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
        MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
        MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
        MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);
        this.tetra = new Tetrahedron( 
            new MyPolygon(Color.BLUE, p1, p2, p3 , p4),
            new MyPolygon(Color.WHITE, p1, p2, p6 , p5),
            new MyPolygon(Color.YELLOW, p1, p5, p8 , p4),
            new MyPolygon(new Color(255, 140, 26), p2, p6, p7 , p3),
            new MyPolygon(Color.GREEN, p4, p3, p7 , p8),
            new MyPolygon(Color.RED, p5, p6, p7 , p8));
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

        tetra.Render(g);

        g.dispose();
        bs.show();
    }

    ClickType prevMouse = ClickType.Unknown;
    int initialX, initialY;
    double mouseSensivity = 2.5;

    private void Update() {
        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if(this.mouse.getButton() == ClickType.LeftClick) {
            int xDif = x - initialX;
            int yDif = y - initialY;

            this.tetra.Rotate(true, 0, yDif/mouseSensivity, -xDif/mouseSensivity);
        }
        else if(this.mouse.getButton() == ClickType.RightClick) {
            int xDif = x - initialX;

            this.tetra.Rotate(true, -xDif/mouseSensivity, 0, 0);
        }

        if(this.mouse.getButton() == ClickType.ZoomIn) {
            PointConverter.zoomIn();
            System.out.println("ZoomIn");
        }
        else if(this.mouse.getButton() == ClickType.ZoomOut) {
            PointConverter.zoomOut();
            System.out.println("ZoomOut");
        }

        this.mouse.resetScroll();

        initialX = x;
        initialY = y;
    }
}
