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
    private JLabel totalLabelA;
    private JLabel totalLabelB;
    private JLabel warningLabel;

    private boolean confirmed = false;

    public TeamBuilder(Window owner) {
        super(owner, "Configurar Times (Simples)", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel p = new JPanel(new GridLayout(classes.length + 2, 3, 8, 8));
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

        // Totals row
        p.add(new JLabel("Total"));
        totalLabelA = new JLabel("0");
        totalLabelB = new JLabel("0");
        p.add(totalLabelA);
        p.add(totalLabelB);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> { confirmed = true; setVisible(false); });
        cancel.addActionListener(e -> { confirmed = false; setVisible(false); });
        buttons.add(ok); buttons.add(cancel);

        Container cc = getContentPane();
        cc.setLayout(new BorderLayout(8,8));
        cc.add(p, BorderLayout.CENTER);
        // warning label below grid, above buttons
        warningLabel = new JLabel("");
        warningLabel.setForeground(Color.RED);
        warningLabel.setVisible(false);
        // allow wrapping via HTML
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(warningLabel, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.SOUTH);
        cc.add(bottom, BorderLayout.SOUTH);

        // Inicializa contadores
        updateCounts();
    }

    private void synchronize(JSlider s, JSpinner sp) {
        s.addChangeListener(e -> {
            int v = s.getValue();
            Object cur = sp.getValue();
            if (cur instanceof Integer && ((Integer) cur) != v) sp.setValue(v);
            updateCounts();
        });
        sp.addChangeListener(e -> {
            Object cur = sp.getValue();
            if (cur instanceof Integer) {
                int v = (Integer) cur;
                if (s.getValue() != v) s.setValue(v);
                updateCounts();
            }
        });
    }

    private void updateCounts() {
        int sumA = 0;
        int sumB = 0;
        for (int i = 0; i < slidersA.length; i++) {
            sumA += slidersA[i].getValue();
            sumB += slidersB[i].getValue();
        }
        // Atualiza labels de total (apenas números)
        totalLabelA.setText(Integer.toString(sumA));
        totalLabelB.setText(Integer.toString(sumB));
        totalLabelA.setForeground(Color.BLACK);
        totalLabelB.setForeground(Color.BLACK);

        // Mostrar aviso se a soma combinada das duas equipes ultrapassar 600
        int combinado = sumA + sumB;
        if (combinado > 600) {
            String msg = "<html><div style='text-align:center;'>Total combinado: " + combinado + ". ";
            msg += "Tropas demais: pode travar em computadores mais fracos e pode haver bugs visuais.";
            msg += "</div></html>";
            warningLabel.setText(msg);
            warningLabel.setVisible(true);
        } else {
            warningLabel.setVisible(false);
        }
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
