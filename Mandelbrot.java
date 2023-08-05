import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Mandelbrot extends JComponent {
  JFrame frame;
  private BufferedImage buffer;
  public static final int WIDTH = 800;
  public static final int HEIGHT = 800;
  public static final int ITERATIONS = 100;

  public static final double startX = -2;
  public static final double width = 4;
  public static final double startY = 2;
  public static final double height = 4;

  public static final double dx = width / (WIDTH - 1);
  public static final double dy = height / (HEIGHT - 1);

  public Mandelbrot() {
    buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    render();
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Mandelbrot");
    frame.getContentPane().add(this);

    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void addNotify() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(buffer, 0, 0, null);
  }

  public void render() {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        int color = calculatePoints(x, y);
        buffer.setRGB(x, y, color);
      }
    }
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

    if (i == ITERATIONS) {
      return 0x2222DDFF;
    } else {
      return 0x33333333;
    }
  }

  public static ComplexNumber convertToComplex(int x, int y) {
    double real = startX + x * dx;
    double imaginary = 2 - y * dy;
    return new ComplexNumber(real, imaginary);
  }

  public static void main(String[] args) {
    new Mandelbrot();
  }
}