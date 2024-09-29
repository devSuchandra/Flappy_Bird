import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);//this will be going to place the window at the center of our screen
        frame.setResizable(false);//so that no one can resize the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//when the user click on the X button of the window it will terminate the program

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        flappyBird.requestFocus();
        //frame.pack();
        frame.setVisible(true);
    }
}
