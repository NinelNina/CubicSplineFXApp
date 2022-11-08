package cs.vsu.ru.kravtsova_n_e.cubicsplineapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class CubicSplineController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();
    CubicSpline spline = new CubicSpline();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS,
                2 * POINT_RADIUS, 2 * POINT_RADIUS);

        points.add(clickPoint);
        spline.addData(clickPoint.getX(), clickPoint.getY());

        if (points.size() > 1){
            spline.interpolate();
            List<Double> splineX = spline.getSplineXValues();
            List<Double> splineY = spline.getSplineYValues();

            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (Point2D point : points){
                graphicsContext.fillOval(
                        point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                        2 * POINT_RADIUS, 2 * POINT_RADIUS);
            }
            for (int i = 0; i < splineX.size(); i++) {
                graphicsContext.fillOval(
                        splineX.get(i), splineY.get(i),
                        1, 1);

            }
        }
    }
}