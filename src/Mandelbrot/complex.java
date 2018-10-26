package Mandelbrot;

public class complex {
    public double real;
    public double imaginary;

    public complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public complex square() {
        double r = real * real - imaginary * imaginary;
        double i = 2 * real * imaginary;
        real = r;
        imaginary = i;
        return this;
    }

    public complex multiply(complex c) {
        double r = real * c.real - imaginary * c.imaginary;
        double i = real * c.imaginary + c.real * imaginary;
        return new complex(r, i);
    }

    public complex add(complex c) {
        real += c.real;
        imaginary += c.imaginary;
        return new complex((real + c.real), (imaginary + c.imaginary));
    }

    public double absSquare() {
        return real * real + imaginary * imaginary;
    }

    @Override
    public String toString() {
        return real + " + " + imaginary + "i";
    }
}