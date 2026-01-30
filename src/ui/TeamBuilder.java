package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Diálogo bem simples: retorna duas listas de contagens [guerreiros, magos] para Team A e Team B
public class TeamBuilder extends JDialog {
    private final JSpinner guerreiroA = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private final JSpinner magoA = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private final JSpinner rangerA = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private final JSpinner guerreiroB = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private final JSpinner magoB = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private final JSpinner rangerB = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
    private boolean confirmed = false;

    public TeamBuilder(Window owner) {
        super(owner, "Configurar Times (Simples)", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel p = new JPanel(new GridLayout(4, 3, 6, 6));
        p.add(new JLabel("Classe"));
        p.add(new JLabel("Team A"));
        p.add(new JLabel("Team B"));

        p.add(new JLabel("Guerreiros"));
        p.add(guerreiroA);
        p.add(guerreiroB);

        p.add(new JLabel("Magos"));
        p.add(magoA);
        p.add(magoB);

        p.add(new JLabel("Rangers"));
        p.add(rangerA);
        p.add(rangerB);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> { confirmed = true; setVisible(false); });
        cancel.addActionListener(e -> { confirmed = false; setVisible(false); });
        buttons.add(ok); buttons.add(cancel);

        Container cc = getContentPane();
        cc.setLayout(new BorderLayout(8,8));
        cc.add(p, BorderLayout.CENTER);
        cc.add(buttons, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() { return confirmed; }

    public List<Integer> getCountsForA() {
        List<Integer> out = new ArrayList<>();
        out.add((Integer) guerreiroA.getValue());
        out.add((Integer) magoA.getValue());
        out.add((Integer) rangerA.getValue());
        return out;
    }

    public List<Integer> getCountsForB() {
        List<Integer> out = new ArrayList<>();
        out.add((Integer) guerreiroB.getValue());
        out.add((Integer) magoB.getValue());
        out.add((Integer) rangerB.getValue());
        return out;
    }
}
