package tp1;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Affiche extends  JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel content = new JPanel();
    private JLabel txt = new JLabel();
    private JPanel pan = new JPanel();
    private GridBagConstraints gbc = new GridBagConstraints();

    Affiche(String result) {
        txt.setText(result);
        content.setLayout(new GridBagLayout());
        pan.setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        content.add(txt, gbc);
        

        this.setTitle("Simplex Application");
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setContentPane(content);

        this.setVisible(true);

    }
}
