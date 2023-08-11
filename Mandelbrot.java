import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Mandelbrot extends JPanel {
  // change controls from keyboard to mouse??
  JFrame frame;
  private BufferedImage buffer;
  public static final int WIDTH = 800;
  public static final int HEIGHT = 800;
  public static final int ITERATIONS = 100;

  public static double startX = -2;
  public static double startY = 2;
  public static double width = 4;
  public static double height = 4;
  // public static double zoom = 1;

  public static double dx = width / (WIDTH - 1);
  public static double dy = height / (HEIGHT - 1);

  public Mandelbrot() {
    ActionMap actionMap = getActionMap();
    int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
    InputMap inputMap = getInputMap(condition);

    String vkR = "VK_R";
    String vkT = "VK_T";
    String vkUp = "VK_UP";
    String vkDown = "VK_DOWN";
    String vkLeft = "VK_LEFT";
    String vkRight = "VK_RIGHT";

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), vkR);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), vkT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), vkUp);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), vkDown);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), vkLeft);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), vkRight);

    actionMap.put(vkR, new KeyAction(vkR));
    actionMap.put(vkT, new KeyAction(vkT));
    actionMap.put(vkUp, new KeyAction(vkUp));
    actionMap.put(vkDown, new KeyAction(vkDown));
    actionMap.put(vkLeft, new KeyAction(vkLeft));
    actionMap.put(vkRight, new KeyAction(vkRight));
    render();
  }

  private class KeyAction extends AbstractAction {
    public KeyAction(String actionCommand) {
      putValue(ACTION_COMMAND_KEY, actionCommand);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvt) {
      String cmd = actionEvt.getActionCommand();
      if (cmd.equals("VK_R")) {
        // zoom -= 1;
        width *= 1.2;
        height *= 1.2;
        startX *= 1.2;
        startY *= 1.2;
        dx = width / (WIDTH - 1);
        dy = height / (HEIGHT - 1);
        render();
      } else if (cmd.equals("VK_T")) {
        // zoom += 1;
        width /= 1.2;
        height /= 1.2;
        startX /= 1.2;
        startY /= 1.2;
        dx = width / (WIDTH - 1);
        dy = height / (HEIGHT - 1);
        render();
      } else if (cmd.equals("VK_UP")) {
        startY += 0.1;
        render();
      } else if (cmd.equals("VK_DOWN")) {
        startY -= 0.1;
        render();
      } else if (cmd.equals("VK_LEFT")) {
        startX -= 0.1;
        render();
      } else if (cmd.equals("VK_RIGHT")) {
        startX += 0.1;
        render();
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    g.drawImage(
      buffer, 
      0, 0, HEIGHT, WIDTH,
      null);  
  }

  public void render() {
    this.repaint();
    buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        int color = calculatePoints(x, y);
        buffer.setRGB(x, y, color);
      }
    }

    this.repaint();
  }

  public int calculatePoints(int x, int y) {
    ComplexNumber number = convertToComplex(x, y);
    ComplexNumber z = number;
    int i;

    for (i = 0; i < ITERATIONS; i++) {
      z = z.times(z).add(number);

      if (z.abs() > 2.0) {
        break;
      }
    }

    return generateColor(i);
  }

  public static int generateColor(int i) {
    if (i == ITERATIONS) {
      return 0x0000000;
    }
    int r = (i * 2) % 255;
    int g = (i * 3) % 255;
    int b = (i * 4) % 255;
    return (r << 16) + (g << 8) + b;
  }

  public static ComplexNumber convertToComplex(int x, int y) {
    double real = startX + x * dx;
    double imaginary = startY - y * dy;
    return new ComplexNumber(real, imaginary);
  }

  public static void intializeGui() {
    Mandelbrot mandy = new Mandelbrot();

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    frame.setTitle("Mandelbrot"); 
    frame.getContentPane().add(mandy);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setLocationByPlatform(true);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        intializeGui();
      }
    });
  }
}