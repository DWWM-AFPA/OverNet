package swing;

import javax.swing.*;
import java.awt.*;

public class EditionVDS extends JFrame {
    private static JButton valid;
    private static JButton cancel;

    public static void main(String[] args) {
        createAndShow();
    }


    public EditionVDS(String name) {
        super(name);
        setButton();
    }

    public static void setButton(){
        valid = new JButton("Valider");
        cancel = new JButton("Annuler");
    }


    public static void addComponentsToPanel(Container pane){
        String[] labels = {"IP : ", "URL : ", "Destination : "};
        JPanel form = new JPanel(new SpringLayout());
        JPanel boutonBox = new JPanel();
        boutonBox.setLayout(new GridLayout(1, 2));

        for (String labelName:labels) {
            JLabel lab = new JLabel(labelName, JLabel.TRAILING);
            form.add(lab);
            JTextField textField = new JTextField(10);
            lab.setLabelFor(textField);
            form.add(textField);
        }

        makeCompactGrid(form, labels.length, 2, 6, 6, 6, 6);

        boutonBox.add(valid);
        boutonBox.add(cancel);

        pane.add(form, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(boutonBox, BorderLayout.SOUTH);

    }
    public static void createAndShow(){
        EditionVDS frame = new EditionVDS("Gestion des données dans le VDNS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPanel(frame.getContentPane());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

}
