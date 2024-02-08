import javax.swing.JFrame;

public class MainFrame extends JFrame{
    MainFrame(){
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        
        this.add(new MainPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
