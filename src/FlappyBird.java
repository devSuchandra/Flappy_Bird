import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;//this is going to store all the pipes in the game
import java.util.Random;
import java.util.random.*;//this is going to use for placing pipes at random position
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image image;

        Bird(Image image){
            this.image = image;
        }
    }
    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image image;
        boolean passed = false;

        Pipe(Image image){
            this.image = image;
        }
    }
    //game logic
    Bird bird;
    int velocityX = -4;// move pipes to the left speed(simulates bird moving right)
    int velocityY = 0;//move bird up/down speed
    int gravity = 1;

    ArrayList<Pipe> pipes;//because we have many pipes in our game so we need to store it in arraylist
    Random random = new Random();

    Timer gameLoop;
    Timer placePipTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardHeight, boardWidth));
        //setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("/images/flappybirdbg1_files/flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("/images/flappybird1_files/flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("/images/toppipe1_files/toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("/images/bottompipe1_files/bottompipe.png")).getImage();

        //Bird
        bird = new Bird(birdImage);

        pipes = new ArrayList<Pipe>();

        //Place pipes timer
        placePipTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipTimer.start();

        //Game Timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();//If we don't start the timer then it will start it only for once not in loop
    


    }
    public void placePipes(){
        //(0 - 1) * pipeHeight(512)/2 -> it will give a random number from 0 to 256
        //512/4 = 128
        //0 - 128 - (0 - 256) --> 1/4 pipeHeight -> 3/4 pipeHeight

        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImage,0,0,boardWidth,boardHeight,null);

        //Bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        //Pipes
        for(int i = 0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        if(bird.y > boardHeight){
            gameOver = true;
        }
        //score
        g.setColor(Color.WHITE);
        g.setFont((new Font("Arial", Font.PLAIN, 32)));
        if(gameOver){
            g.drawString("Game Over: "+ String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width && //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x && //a's top right corner passes b's top left corner
               a.y < b.y + b.height && //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;   //a's bottom left corner passes b's top left corner
    }
    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);//if we don't set the limit then the bird will go up and exceed the frame

        //Pipes
        for(int i = 0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;// 0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
            }

            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipTimer.stop();
            gameLoop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(gameOver){
                //restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipTimer.start();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
