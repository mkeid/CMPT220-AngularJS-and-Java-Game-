/**
 * Created by Mohamed on 5/6/14.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PuzzleGame extends JFrame implements ActionListener {

    private JPanel centerPanel;
    private JButton button;
    private JLabel label;
    private Image source;
    private Image image;
    int[][] position;
    int width, height;

    public PuzzleGame() {

        position = new int[][] {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {9, 10, 11}
        };


        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 4, 0, 0));

        ImageIcon sid = new ImageIcon(PuzzleGame.class.getResource("puzzle.png"));
        source = sid.getImage();

        width = sid.getIconWidth();
        height = sid.getIconHeight();


        add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);


        for ( int i = 0; i < 4; i++) {
            for ( int j = 0; j < 3; j++) {
                if ( j == 2 && i == 3) {
                    label = new JLabel("");
                    centerPanel.add(label);
                } else {
                    button = new JButton();
                    button.addActionListener(this);
                    centerPanel.add(button);
                    image = createImage(new FilteredImageSource(source.getSource(),
                            new CropImageFilter(j*width/3, i*height/4,
                                    (width/3)+1, height/4)));
                    button.setIcon(new ImageIcon(image));
                }
            }
        }

        setSize(1248, 832);
        setTitle("Puzzle Game");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        Dimension size = button.getSize();

        int labelX = label.getX();
        int labelY = label.getY();
        int buttonX = button.getX();
        int buttonY = button.getY();
        int buttonPosX = buttonX / size.width;
        int buttonPosY = buttonY / size.height;
        int buttonIndex = position[buttonPosY][buttonPosX];



        if (labelX == buttonX && (labelY - buttonY) == size.height ) {

            int labelIndex = buttonIndex + 3;

            centerPanel.remove(buttonIndex);
            centerPanel.add(label, buttonIndex);
            centerPanel.add(button,labelIndex);
            centerPanel.validate();
        }

        if (labelX == buttonX && (labelY - buttonY) == -size.height ) {

            int labelIndex = buttonIndex - 3;
            centerPanel.remove(labelIndex);
            centerPanel.add(button,labelIndex);
            centerPanel.add(label, buttonIndex);
            centerPanel.validate();
        }

        if (labelY == buttonY && (labelX - buttonX) == size.width ) {

            int labelIndex = buttonIndex + 1;

            centerPanel.remove(buttonIndex);
            centerPanel.add(label, buttonIndex);
            centerPanel.add(button,labelIndex);
            centerPanel.validate();
        }

        if (labelY == buttonY && (labelX - buttonX) == -size.width ) {

            int labelIndex = buttonIndex - 1;

            centerPanel.remove(buttonIndex);
            centerPanel.add(label, labelIndex);
            centerPanel.add(button,labelIndex);
            centerPanel.validate();
        }
    }
}