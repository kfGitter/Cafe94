package cafe.ninetyfour.controller;
// Changed to match FXML expectation

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class TestController {
    @FXML
    private Circle myCircle;

    private double x = 150;  // Initial X position
    private double y = 150;  // Initial Y position

    public void up(ActionEvent e) {
        myCircle.setCenterY(y -= 10);
        System.out.println("up");
    }

    public void down(ActionEvent e) {
        myCircle.setCenterY(y += 10);
        System.out.println("down");
    }

    public void left(ActionEvent e) {
        myCircle.setCenterX(x -= 10);
        System.out.println("left");
    }

    public void right(ActionEvent e) {
        myCircle.setCenterX(x += 10);
        System.out.println("right");
    }
}