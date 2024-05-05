package com.example.pingpong1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Pong extends Application {
    //declaring variables
    private static final int width= 700;
    private boolean gamestart;
    private double player1posX = 0;
    private double player2posX = width - pwidth;
    private static final int hight= 600;
    private static final int Phight= 150;
    private static final int pwidth= 17;
    private static final double ball_radius= 20;
    private static final int player_hight = 150;
    private static final int player_width = 17;
    private int ballSpeedY= 1;
    private int getBallSpeedX = 1;
    private int score1 =  0;
    private int score2 = 0;
    private double player1posY = hight /2;
    Image backgroundImage = new Image(new FileInputStream("E:\\one piece\\pingpong1\\src\\main\\java\\com\\example\\pingpong1\\Table-Tennis-Racket-vector-illustration-Graphics-61487143-1-580x387.jpg"));

    private double player2posY = hight /2;
    private double ballXpos = width /2;
    private double ballYpos = width /2;

    public Pong() throws FileNotFoundException {
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("PING PONG");
        Canvas canvas = new Canvas(width,hight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw background image
        gc.drawImage(backgroundImage, 0, 0, width, hight);

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10),e ->run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        //game control
        canvas.setOnMouseMoved(e->player1posY = e.getY());
        canvas.setOnMouseClicked(e -> gamestart = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }
    private void run(GraphicsContext gc){

        gc.drawImage(backgroundImage, 0, 0, width, hight);
        //setting the background color


        //text color
        gc.setFill(Color.DARKBLUE);
        gc.setFont(Font.font(25));

        if(gamestart == true){
            ballXpos+=getBallSpeedX;
            ballYpos += ballSpeedY;

            //scening
            if(ballXpos < width - width/4){
                player2posY = ballYpos - player_hight /2;
            }
            else{
                player2posY = ballYpos > player2posY + player_hight/2?player2posY += 1:player2posY-1;
            }
            //drawing ball
            gc.fillOval(ballXpos,ballYpos,ball_radius,ball_radius);

        }else{
            // setting the starting text
            gc.setStroke(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click To Play", width/2-10,hight /2);
            //reset the ball start position
            ballXpos = width/2;
            ballYpos = hight/2;

            // reset the speed of and direction of the ball
            getBallSpeedX = new Random().nextInt(2) ==0?1:-1;
            ballSpeedY = new Random().nextInt(2) ==0?1:-1;
        }
        //restrictionn of the ball
        if(ballYpos > hight || ballYpos < 0 ){
            ballSpeedY *= -1;
        }
        //computer gets point
        if(ballXpos < player1posX - player_width){
            score2++;
            gamestart = false;
        }
        // i get a point
        if(ballXpos > player2posX + player_width){
            score1++;
            gamestart = false;
        }
        //increasing the ball speed
        if(((ballXpos + ball_radius > player2posX) &&ballYpos >= player2posY && ballYpos <= player2posY + player_hight)||
                ((ballXpos < player_width + player1posX) &&ballYpos >= player1posY && ballYpos <= player1posY + player_hight)){
            ballSpeedY += 1 * Math.signum(ballSpeedY);
            getBallSpeedX += 1* Math.signum(getBallSpeedX);
            getBallSpeedX *= -1;
            ballSpeedY *= -1;
        }
    //drawing scores
        gc.fillText(score1+"\t\t\t\t\t\t\t"+score2,width/2,100);
        //drawing the player 1 and player 2
        gc.fillRect(player2posX,player2posY,player_width,player_hight);
        gc.fillRect(player1posX,player1posY,player_width,player_hight);

    }
}
