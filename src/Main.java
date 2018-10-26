import Mandelbrot.Mandelbrot;
import Mandelbrot.complex;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private final double LENGTH = 400;
    private final int LOOP = 50;
    private final int GRID = 500;
    private final int INDEX = 2;
    private final int MAGNIFICATION = 3;
    private final Color ORIGINAL = Color.BLUE;
    private final Color TERMINAL = Color.GREEN;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, LENGTH, LENGTH);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(LENGTH, LENGTH);
        root.getChildren().add(canvas);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Mandelbrot mandelbrot = new Mandelbrot.Builder(ORIGINAL, TERMINAL, INDEX).build(LOOP, GRID);
        paint(primaryStage, scene, graphicsContext, mandelbrot, 1);
    }


    public static void main(String[] args) {
        launch(args);
    }

    void paint(Stage stage, Scene scene, GraphicsContext graphicsContext, Mandelbrot mandelbrot, int loop) {
        mandelbrot.draw(graphicsContext);

        EventHandler<MouseEvent> click = (event) -> {
            switch (event.getButton()) {
                case SECONDARY:
                    stage.hide();
                    break;
                default: {
                    // draw Rectangle
                    graphicsContext.setStroke(Color.RED);
                    graphicsContext.strokeRect(event.getX() - scene.getWidth() / MAGNIFICATION / 2.0, event.getY() - scene.getHeight() / MAGNIFICATION / 2.0, scene.getWidth() / MAGNIFICATION, scene.getHeight() / MAGNIFICATION);

                    complex oldCenter = mandelbrot.getCenter();
                    double real = (0.5d - (scene.getWidth() - event.getX()) / scene.getWidth()) * mandelbrot.getLength() + oldCenter.real;
                    double imaginary = (0.5d - (scene.getHeight() - event.getY()) / scene.getHeight()) * mandelbrot.getLength() + oldCenter.imaginary;
                    complex center = new complex(real, imaginary);

                    Mandelbrot mandelbrot1 = new Mandelbrot.Builder(ORIGINAL, TERMINAL, INDEX).build(LOOP * (loop + 1), GRID, mandelbrot.getLength() / (double) MAGNIFICATION, center);

                    Stage stage1 = new Stage();
                    stage1.setX(stage.getX() + event.getX() * 1.2);
                    stage1.setY(stage.getY() + event.getY() * 1.2);
                    Group root = new Group();
                    Scene scene1 = new Scene(root, LENGTH, LENGTH);
                    stage1.setScene(scene1);

                    Canvas canvas = new Canvas(LENGTH, LENGTH);
                    root.getChildren().add(canvas);
                    GraphicsContext graphicsContext1 = canvas.getGraphicsContext2D();
                    paint(stage1, scene1, graphicsContext1, mandelbrot1, loop + 1);
                }
            }
        };
        scene.setOnMouseClicked(click);
        stage.show();
    }
}
