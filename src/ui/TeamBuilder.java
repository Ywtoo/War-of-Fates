package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Diálogo bem simples: retorna duas listas de contagens [guerreiros, magos] para Team A e Team B
public class TeamBuilder extends JDialog {
    // order: 0=Tank, 1=Guerreiro, 2=Ranger, 3=Mago
    private final String[] classes = new String[] { "Tank", "Guerreiros", "Rangers", "Magos" };

    private final JSlider[] slidersA = new JSlider[4];
    private final JSpinner[] spinnersA = new JSpinner[4];
    private final JSlider[] slidersB = new JSlider[4];
    private final JSpinner[] spinnersB = new JSpinner[4];

    private boolean confirmed = false;

    public TeamBuilder(Window owner) {
        super(owner, "Configurar Times (Simples)", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel p = new JPanel(new GridLayout(classes.length + 1, 3, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        p.add(new JLabel("Classe"));
        p.add(new JLabel("Team A"));
        p.add(new JLabel("Team B"));

        int max = 200;
        for (int i = 0; i < classes.length; i++) {
            p.add(new JLabel(classes[i]));

            JSlider sA = new JSlider(0, max, 0);
            JSpinner spA = new JSpinner(new SpinnerNumberModel(0, 0, max, 1));
            synchronize(sA, spA);
            JPanel cellA = new JPanel(new BorderLayout(6, 0));
            cellA.add(sA, BorderLayout.CENTER);
            cellA.add(spA, BorderLayout.EAST);
            p.add(cellA);

            JSlider sB = new JSlider(0, max, 0);
            JSpinner spB = new JSpinner(new SpinnerNumberModel(0, 0, max, 1));
            synchronize(sB, spB);
            JPanel cellB = new JPanel(new BorderLayout(6, 0));
            cellB.add(sB, BorderLayout.CENTER);
            cellB.add(spB, BorderLayout.EAST);
            p.add(cellB);

            slidersA[i] = sA; spinnersA[i] = spA;
            slidersB[i] = sB; spinnersB[i] = spB;
        }

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

    private void synchronize(JSlider s, JSpinner sp) {
        s.addChangeListener(e -> {
            int v = s.getValue();
            Object cur = sp.getValue();
            if (cur instanceof Integer && ((Integer) cur) != v) sp.setValue(v);
        });
        sp.addChangeListener(e -> {
            Object cur = sp.getValue();
            if (cur instanceof Integer) {
                int v = (Integer) cur;
                if (s.getValue() != v) s.setValue(v);
            }
        });
    }

    public boolean isConfirmed() { return confirmed; }

    public List<Integer> getCountsForA() {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < slidersA.length; i++) out.add(slidersA[i].getValue());
        return out;
    }

    public List<Integer> getCountsForB() {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < slidersB.length; i++) out.add(slidersB[i].getValue());
        return out;
    }
}
