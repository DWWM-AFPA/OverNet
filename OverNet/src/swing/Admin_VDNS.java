package swing;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Admin_VDNS extends JDialog {
    private JPanel contentPane;
    private JButton Edition;
    //private JTable table1;

    public Admin_VDNS() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane();
        Edition.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }

}
