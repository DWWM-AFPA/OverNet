package swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditionVDS extends JDialog {
    private JPanel contentPane;
    private JButton valider;
    private JButton supprimer;
    private JTextField ipField;
    private JTextField adresseField;
    private JTextField destinationField;

    Object[][] donnees = {
            {"Swing", "Astral", "standard"},
            {"Swing", "Mistral", "standard"},
            {"Gin", "Oasis", "standard"},
            {"Gin", "boomerang", "compétition"},
            {"Advance", "Omega", "performance"},
    } ;
    String[] titreColonnes = {
            "marque","modèle", "homologation",
            "couleur", "vérifiée ?"};
    JTable jTable1 = new JTable(
            donnees, titreColonnes);

    public EditionVDS() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(valider);
        valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
