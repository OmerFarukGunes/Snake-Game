package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.*;
import java.util.Random;
/**
 *
 * @author OmerFG
 */
public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH =600;
    static final int SCREEN_HEIGHT =600;
    static final int UNIT_SIZE = 25; //alani belirleyecek.
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodySize= 6;
    int applessEaten;
    int appleX;
    int appleY;
    int boostX;
    int boostY;
    char navigation = 'R';
    //r -> right 
    boolean running=false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
        
    }
    public void startGame(){
        newApple();
        newBoost();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            for(int i = 0;i < SCREEN_HEIGHT/UNIT_SIZE;i++){
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //dikine grid cizmek icin bunu yaptik
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            //yatay olarak cizdik
            }
        
            g.setColor(Color.BLUE);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.YELLOW);
            g.fillOval(boostX, boostY, UNIT_SIZE, UNIT_SIZE);
       
            //yilani cizdik
            for(int i = 0;i<bodySize;i++){
                if(i == 0){
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                g.setColor(new Color(45,180,0));
                 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
             g.setFont(new Font("Calibri",Font.BOLD,20));
            g.setColor(Color.WHITE);
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+applessEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applessEaten)), g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
       
    }
    public void newApple(){
        appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodySize; i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(navigation){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
    }
    public void newBoost(){
        boostX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        boostY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void checkBoost(){
        if((x[0]==boostX) && (y[0]==boostY) ){
            bodySize+=3;
            applessEaten+=5;
            newBoost();
        }
    }
    public void checkApple(){
        if((x[0]==appleX) && (y[0]==appleY) ){
            bodySize++;
            applessEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
         //yilan kendine carparsa
        for(int i = bodySize;i>0;i--){
            if((x[0] ==x[i])&&(y[0] ==y[i])){
            running=false;
            }
        }
        //yilan sola carparsa
        if(x[0]<0){
            running=false;
        }
        //yilan saga carparsa
         if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //yilan yukari carparsa
        if(y[0]<0){
            running=false;
        }
        //yilan asagi carparsa
         if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        
         if(!running){
             timer.stop();
         }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Calibri",Font.BOLD,50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkBoost();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(navigation != 'R'){
                        navigation = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(navigation != 'L'){
                        navigation = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(navigation != 'D'){
                        navigation = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(navigation != 'U'){
                        navigation = 'D';
                    }
                    break;   
            }
        }
    
    }
    
}
