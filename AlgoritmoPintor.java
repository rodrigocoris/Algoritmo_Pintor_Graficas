import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AlgoritmoPintor extends JPanel {
    private ArrayList<Rectangulo> rectangulos;

    public AlgoritmoPintor() {
        rectangulos = new ArrayList<>();

        // Se crean tres rectángulos con diferentes dimensiones y colores
        rectangulos.add(new Rectangulo(50, 50, 200, 100, Color.RED));
        rectangulos.add(new Rectangulo(100, 150, 250, 200, Color.GREEN));
        rectangulos.add(new Rectangulo(150, 100, 300, 150, Color.BLUE));

        // Se ordenan los rectángulos basados en la posición Y de su punto superior
        // izquierdo
        Collections.sort(rectangulos);
    }

    // Método para dibujar los rectángulos en el panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Se dibujan los rectángulos y se rellenan
        for (Rectangulo r : rectangulos) {
            r.RectanguloRelleno(g);
            r.Rectangulos(g);
        }
    }

    // Crear el JPanel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Algoritmo del Pintor");
        AlgoritmoPintor algoritmoPintor = new AlgoritmoPintor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(algoritmoPintor);
        frame.setVisible(true);
    }
}

class Rectangulo implements Comparable<Rectangulo> {
    private int x1, y1, x2, y2;
    private Color color;

    // Constructor que inicializa un rectángulo con sus coordenadas, dimensiones y
    // color
    public Rectangulo(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    // Métodos getter para obtener las coordenadas y el color del rectángulo
    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Color getColor() {
        return color;
    }

    // Método para dibujar los bordes del rectángulo
    public void Rectangulos(Graphics g) {
        LineaMedia(g, x1, y1, x2, y1, Color.black);
        LineaMedia(g, x2, y1, x2, y2, Color.black);
        LineaMedia(g, x2, y2, x1, y2, Color.black);
        LineaMedia(g, x1, y2, x1, y1, Color.black);
    }

    // Método para rellenar el rectángulo
    public void RectanguloRelleno(Graphics g) {
        for (int y = y1; y <= y2; y++) {
            LineaMedia(g, x2, y, x1, y, color);
        }
    }

    // Algoritmo de trazado de líneas (Bresenham)
    public void LineaMedia(Graphics g, int x0, int y0, int x1, int y1, Color c) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int err = dx - dy;
        int x = x0;
        int y = y0;

        while (true) {
            pix(g, x, y, c);

            if (x == x1 && y == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }

    // Método para dibujar un punto en el panel
    public void pix(Graphics g, int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(x, y, 1, 1);
    }

    // Método de comparación basado en la coordenada Y del primer punto del
    // rectángulo
    @Override
    public int compareTo(Rectangulo otro) {
        return Integer.compare(this.y1, otro.y1);
    }
}
