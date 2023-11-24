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
        // Barras estiradas
        drawStretchedBars();

        // Barras diagonales inclinadas
        drawDiagonalBars();
    }

    private void drawStretchedBars() {
        // Coordenadas de los vértices de la primera barra
        int[][] vertices1 = {
                { -50, -50, -50 }, { -50, -50, 50 },
                { -50, 50, -50 }, { -50, 50, 50 },
                { 50, -50, -50 }, { 50, -50, 50 },
                { 50, 50, -50 }, { 50, 50, 50 }
        };

        // Coordenadas de los vértices de la segunda barra (encima de la primera)
        int[][] vertices2 = new int[8][3];
        for (int i = 0; i < 8; i++) {
            vertices2[i][0] = vertices1[i][0];
            vertices2[i][1] = vertices1[i][1] + 100; // Ajusta la altura 
            vertices2[i][2] = vertices1[i][2];
        }

        // Coordenadas de los vértices de la tercera barra (encima de la segunda)
        int[][] vertices3 = new int[8][3];
        for (int i = 0; i < 8; i++) {
            vertices3[i][0] = vertices1[i][0];
            vertices3[i][1] = vertices2[i][1] + 100; // Ajusta la altura 
            vertices3[i][2] = vertices1[i][2];
        }

        // Caras de las barras
        int[][] faces = {
                { 0, 1, 3, 2 }, // Cara frontal
                { 4, 5, 7, 6 }, // Cara posterior
                { 0, 1, 5, 4 }, // Cara izquierda
                { 2, 3, 7, 6 }, // Cara derecha
                { 0, 2, 6, 4 }, // Cara inferior
                { 1, 3, 7, 5 } // Cara superior
        };

        // Dibujar las caras de la primera barra
        drawBarFaces(faces, vertices1, Color.RED.getRGB());

        // Dibujar las caras de la segunda barra
        drawBarFaces(faces, vertices2, Color.GREEN.getRGB());

        // Dibujar las caras de la tercera barra
        drawBarFaces(faces, vertices3, Color.BLUE.getRGB());
    }

    private void drawDiagonalBars() {
        // Coordenadas de los vértices de la cuarta barra diagonal
        int[][] vertices4 = {
                { -50, -50, -50 }, { -50, -50, 50 },
                { -50, 50, -50 }, { -50, 50, 50 },
                { 50, -50, -50 }, { 50, -50, 50 },
                { 50, 50, -50 }, { 50, 50, 50 }
        };

        // Inclina la barra diagonal ajustando las coordenadas de los vértices
        for (int i = 0; i < 8; i++) {
            vertices4[i][0] += 50; // Ajusta la inclinación según tu preferencia
            vertices4[i][1] += 50; // Ajusta la inclinación según tu preferencia
        }

        // Coordenadas de los vértices de la quinta barra diagonal (encima de la cuarta)
        int[][] vertices5 = new int[8][3];
        for (int i = 0; i < 8; i++) {
            vertices5[i][0] = vertices4[i][0];
            vertices5[i][1] = vertices4[i][1] + 100; // Ajusta la altura 
            vertices5[i][2] = vertices4[i][2];
        }

        // Coordenadas de los vértices de la sexta barra diagonal (encima de la quinta)
        int[][] vertices6 = new int[8][3];
        for (int i = 0; i < 8; i++) {
            vertices6[i][0] = vertices4[i][0];
            vertices6[i][1] = vertices5[i][1] + 100; // Ajusta la altura 
        }

        // Caras de las barras diagonales
        int[][] faces = {
                { 0, 1, 3, 2 }, // Cara frontal
                { 4, 5, 7, 6 }, // Cara posterior
                { 0, 1, 5, 4 }, // Cara izquierda
                { 2, 3, 7, 6 }, // Cara derecha
                { 0, 2, 6, 4 }, // Cara inferior
                { 1, 3, 7, 5 } // Cara superior
        };

        // Dibujar las caras de la cuarta barra diagonal
        drawBarFaces(faces, vertices4, Color.YELLOW.getRGB());

        // Dibujar las caras de la quinta barra diagonal
        drawBarFaces(faces, vertices5, Color.CYAN.getRGB());

        // Dibujar las caras de la sexta barra diagonal
        drawBarFaces(faces, vertices6, Color.MAGENTA.getRGB());
    }

    private void drawBarFaces(int[][] faces, int[][] vertices, int color) {
        // Ordenar las caras por su profundidad (coordenada z promedio)
        sortFacesByDepth(faces, vertices);

        // Dibujar las caras en orden de profundidad
        for (int[] face : faces) {
            fillPolygon(face, vertices, color);
        }
    }

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

    private double calculateAverageDepth(int[] face, int[][] vertices) {
        double sum = 0;
        for (int vertexIndex : face) {
            sum += vertices[vertexIndex][2];
        }
        return sum / face.length;
    }

    private void fillPolygon(int[] face, int[][] vertices, int color) {
        int[] xPoints = new int[face.length];
        int[] yPoints = new int[face.length];

        for (int i = 0; i < face.length; i++) {
            xPoints[i] = vertices[face[i]][0] + WIDTH / 2;
            yPoints[i] = HEIGHT / 2 - vertices[face[i]][1];
        }

        Polygon polygon = new Polygon(xPoints, yPoints, face.length);

        for (int y = polygon.getBounds().y; y < polygon.getBounds().y + polygon.getBounds().height; y++) {
            for (int x = polygon.getBounds().x; x < polygon.getBounds().x + polygon.getBounds().width; x++) {
                if (polygon.contains(x, y)) {
                    putPixel(x, y, color);
                }
            }
        }
    }

    private void putPixel(int x, int y, int color) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            zBuffer[x][y] = color;
        }
    }

    private void drawImage(Graphics g) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, zBuffer[x][y]);
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
