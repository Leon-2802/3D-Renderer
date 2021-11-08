package renderer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[66568]; //Status aller Tasten als boolean array
    private boolean left, right, up, down, forward, backward, zoomIn, zoomOut;

    public void update() {
        this.left = this.keys[KeyEvent.VK_LEFT] || this.keys[KeyEvent.VK_A];
        this.right = this.keys[KeyEvent.VK_RIGHT] || this.keys[KeyEvent.VK_D];
        this.forward = this.keys[KeyEvent.VK_UP] || this.keys[KeyEvent.VK_W];
        this.backward = this.keys[KeyEvent.VK_DOWN] || this.keys[KeyEvent.VK_S];
        this.up = this.keys[KeyEvent.VK_SPACE];
        this.down = this.keys[KeyEvent.VK_SHIFT];

        this.zoomIn = this.keys[KeyEvent.VK_PLUS];
        this.zoomOut = this.keys[KeyEvent.VK_MINUS];
    }

    public boolean getDown() {
        return this.down;
    }
    public boolean getUp() {
        return this.up;
    }
    public boolean getLeft() {
        return this.left;
    }
    public boolean getRight() {
        return this.right;
    }
    public boolean getForward() {
        return this.forward;
    }
    public boolean getBackward() {
        return this.backward;
    }
    public boolean getZoomIn() {return this.zoomIn;}
    public boolean getZoomOut() {return this.zoomOut;}

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
