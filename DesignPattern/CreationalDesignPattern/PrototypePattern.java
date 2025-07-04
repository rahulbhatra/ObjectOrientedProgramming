package DesignPattern.CreationalDesignPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface Shape {
    Shape clone();
}

class Rectangle implements Shape {
    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public Shape clone() {
        return new Rectangle(this.width, this.height);
    }
}

class Square implements Shape {
    private int length;

    public Square(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    @Override
    public Shape clone() {
        return new Square(this.length);
    }
}

class Test {
    public List<Shape> cloneShapes(List<Shape> shapes) {
        List<Shape> clonedShapes = new ArrayList<>();
        for (Shape shape: shapes) {
            clonedShapes.add(shape.clone());
        }
        return clonedShapes;
    }
}


public class PrototypePattern {
    public static void main(String[] args) {
        Shape square = new Square(10); // 10 is the length
        Shape anotherSquare = square.clone(); // Clone with length 10
        assert square == anotherSquare; // false 

        Shape rectangle = new Rectangle(10, 20); // 10 is width, 20 is height
        Shape anotherRectangle = rectangle.clone(); // Clone with width 10 and height 20
        assert rectangle == anotherRectangle; // false 

        Test test = new Test();
        List<Shape> shapes = Arrays.asList(square, rectangle, anotherSquare, anotherRectangle);
        List<Shape> clonedShapes = test.cloneShapes(shapes);

        assert shapes == clonedShapes; // false
        assert shapes.size() == clonedShapes.size(); // true
        assert shapes.get(0) == clonedShapes.get(0); // false
    }
}
