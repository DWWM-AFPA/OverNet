package swing;

import Entity.DNSRowEntity;
import Repository.ConnectionRepository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Admin_VDNS extends JFrame {

    private final String[] header = {"IP", "URL", "Destination"};
    private static Object[][] dns;
    private static JButton create;
    private static JButton edit;
    private static JButton delete;


    public static void main(String[] args) {
        createAndShow();
    }
    public Admin_VDNS(String name) {
        super(name);
        setDns();
        setButton();
    }

    public static Object[][] setDns(){
        if (dns==null) {
            ArrayList<DNSRowEntity> conRepo = ConnectionRepository.readAll();
            dns = new Object[conRepo.size()][];
            int it = 0;
            for (DNSRowEntity entry : conRepo) {
                dns[it] = new String[]{entry.getIp().getHostAddress(), entry.getUrl().getHost(), entry.getDestination()};
                it++;
            }
        }
        return dns;
    }

    public static void setButton(){

        create = new JButton("Ajouter");
        edit = new JButton("Modifier");
        delete = new JButton("Supprimer");

    }


    public void addComponentsToPanel(Container pane){

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        JPanel boutonBox = new JPanel();
        boutonBox.setLayout(new GridLayout(1, 3));

        JTable dnsList = new JTable(dns, header) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        panel.add(new JScrollPane(dnsList));

        boutonBox.add(create);
        boutonBox.add(edit);
        boutonBox.add(delete);

        pane.add(panel, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(boutonBox, BorderLayout.SOUTH);

    }

    public static void createAndShow(){
        Admin_VDNS frame = new Admin_VDNS("Interface VDNS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentsToPanel(frame.getContentPane());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
