import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Algoritmo_Pintor extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private int[][] zBuffer;

    public Algoritmo_Pintor() {
        setTitle("Algoritmo del Pintor");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        zBuffer = new int[WIDTH][HEIGHT];

        // renderización 3D
        render3DScene();

        // JPanel personalizado para mostrar la imagen
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g);
            }
        };

        getContentPane().add(panel);
    }

    private void render3DScene() {

        // cubo con relleno
        drawFilledCube();
    }

    private void drawFilledCube() {
        // Coordenadas de los vértices del cubo
        int[][] cubeVertices = {
                { -50, -50, -50 }, { -50, -50, 50 },
                { -50, 50, -50 }, { -50, 50, 50 },
                { 50, -50, -50 }, { 50, -50, 50 },
                { 50, 50, -50 }, { 50, 50, 50 }
        };

        // caras del cubo
        int[][] cubeFaces = {
                { 0, 1, 3, 2 }, // Cara frontal
                { 4, 5, 7, 6 }, // Cara posterior
                { 0, 1, 5, 4 }, // Cara izquierda
                { 2, 3, 7, 6 }, // Cara derecha
                { 0, 2, 6, 4 }, // Cara inferior
                { 1, 3, 7, 5 } // Cara superior
        };

        // Ordenar las caras del cubo por su profundidad (coordenada z promedio)
        sortFacesByDepth(cubeFaces, cubeVertices);

        // Dibujar las caras del cubo en orden de profundidad
        for (int[] face : cubeFaces) {
            fillPolygon(face, cubeVertices, Color.WHITE.getRGB());
        }
    }

    // Método para ordenar las caras por su profundidad promedio
    private void sortFacesByDepth(int[][] faces, int[][] vertices) {
        for (int i = 0; i < faces.length - 1; i++) {
            for (int j = 0; j < faces.length - i - 1; j++) {
                double depth1 = calculateAverageDepth(faces[j], vertices);
                double depth2 = calculateAverageDepth(faces[j + 1], vertices);
                if (depth1 < depth2) {
                    int[] temp = faces[j];
                    faces[j] = faces[j + 1];
                    faces[j + 1] = temp;
                }
            }
        }
    }

    // Método para calcular la profundidad promedio de una cara
    private double calculateAverageDepth(int[] face, int[][] vertices) {
        double sum = 0;
        for (int vertexIndex : face) {
            sum += vertices[vertexIndex][2];
        }
        return sum / face.length;
    }

    // Método para dibujar un polígono con relleno
    private void fillPolygon(int[] face, int[][] vertices, int color) {
        int[] xPoints = new int[face.length];
        int[] yPoints = new int[face.length];

        for (int i = 0; i < face.length; i++) {
            xPoints[i] = vertices[face[i]][0] + WIDTH / 2;
            yPoints[i] = HEIGHT / 2 - vertices[face[i]][1];
        }

        Polygon polygon = new Polygon(xPoints, yPoints, face.length);

        // Rellenar el polígono
        for (int y = polygon.getBounds().y; y < polygon.getBounds().y + polygon.getBounds().height; y++) {
            for (int x = polygon.getBounds().x; x < polygon.getBounds().x + polygon.getBounds().width; x++) {
                if (polygon.contains(x, y)) {
                    putPixel(x, y, color);
                }
            }
        }
    }

    // Método para dibujar un píxel en la pantalla
    private void putPixel(int x, int y, int color) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            zBuffer[x][y] = Integer.MAX_VALUE;
        }
    }

    // Método para convertir el zBuffer en una imagen y dibujarla
    private void drawImage(Graphics g) {
        // Implementación para convertir la matriz de píxeles en una imagen y dibujarla
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int pixelColor = zBuffer[x][y] == Integer.MAX_VALUE ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                image.setRGB(x, y, pixelColor);
            }
        }

        g.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Algoritmo_Pintor painter = new Algoritmo_Pintor();
            painter.setVisible(true);
        });
    }
}
