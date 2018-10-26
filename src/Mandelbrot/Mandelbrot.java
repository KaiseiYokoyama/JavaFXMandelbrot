package Mandelbrot;

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mandelbrot {
    public static class Builder {
        private Color original, terminal;
        private int index = 2;

        public Builder() {
        }

        /**
         * @param original inside of set
         * @param terminal outside of set
         */
        public Builder(Color original, Color terminal) {
            this.original = original;
            this.terminal = terminal;
        }

        public Builder(Color original, Color terminal, int index) {
            this.original = original;
            this.terminal = terminal;
            this.index = index;
        }

        public Mandelbrot build(int loop, int grid) {
            return build(loop, grid, 4.0, new complex(0.0d, 0.0d));
        }

        public Mandelbrot build(int loop, int grid, @NotNull complex center) {
            return build(loop, grid, 4.0, center);
        }

        public Mandelbrot build(int loop, int grid, double length, @NotNull complex center) {
            return new Mandelbrot(loop, grid, length, center).setColors(original, terminal).setIndex(index);
        }
    }

    private double length;
    private int loop, grid;
    private complex center;
    private int index;
    private Color original, terminal;
    final int MAX = 2;

    private Mandelbrot(int loop, int grid, double length, @NotNull complex center) {
        this.loop = loop;
        this.grid = grid;
        this.length = length;
        this.center = center;
    }

    private Mandelbrot setColors(Color original, Color terminal) {
        if (original == null || terminal == null)
            return this;

        this.original = original;
        this.terminal = terminal;
        return this;
    }

    private complex f(complex z, complex C) {
        if (index == 2)
            return z.square().add(C);
        complex tmp = new complex(z.real,z.imaginary);
        for (int i = 0; i < index-1; i++) {
            z = z.multiply(tmp);
        }
        return z.add(C);
    }

    private int calc(complex C) {
        complex z = new complex(0.0, 0.0);
        for (int i = 0; i < loop; i++) {
            z = f(z, C);
            if (z.absSquare() > MAX * MAX) {
                return i;
            }
        }
        return loop;
    }

    public void draw(GraphicsContext graphicsContext) {
//        System.out.println("draw: getCenter() = " + getCenter());
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                double cReal = (double) (i - grid / 2) / (double) grid * length + center.real;
                double cImaginary = (double) (j - grid / 2) / (double) grid * length + center.imaginary;
                final complex C = new complex(cReal, cImaginary);
                int res = calc(C);
                graphicsContext.setFill(calcColor(res));
                double xR = graphicsContext.getCanvas().getWidth() * (double) i / (double) grid;
                double yR = graphicsContext.getCanvas().getHeight() * (double) j / (double) grid;
                graphicsContext.fillRect(xR, yR, 1, 1);
            }
        }
    }

    private Color calcColor(int i) {
        if (original == null || terminal == null)
            return Color.color((double) (loop - i) / (double) loop, (double) (loop - i) / (double) loop, (double) (loop - i) / (double) loop);
        else {
            double red = (original.getRed() - terminal.getRed()) / (double) loop * (double) i + terminal.getRed();
            double green = (original.getGreen() - terminal.getGreen()) / (double) loop * (double) i + terminal.getGreen();
            double blue = (original.getBlue() - terminal.getBlue()) / (double) loop * (double) i + terminal.getBlue();
            return Color.color(red, green, blue);
        }
    }

    public complex getCenter() {
        return center;
    }

    public double getLength() {
        return length;
    }

    private Mandelbrot setIndex(int index) {
        this.index = index;
        return this;
    }
}
