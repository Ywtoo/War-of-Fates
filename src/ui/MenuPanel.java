package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeListener;

// Painel de menu com controles para iniciar/reiniciar a batalha e ajustar velocidade/modo
public class MenuPanel extends JPanel {
    // slider interno usa valores inteiros que representam metade (valor/2.0 = multiplicador)
    // range 1..40 -> representa 0.5x .. 20x
    private final JSlider speedSlider = new JSlider(1, 40, 2);
    private final JComboBox<String> modeCombo = new JComboBox<>(new String[]{"PVP","PVE","EVE"});

    private final JButton startBtn = new JButton("Start");
    private final JButton resetBtn = new JButton("Reset");
    private final JButton configBtn = new JButton("Config Teams");
    private final JButton statusBtn = new JButton("Status");

    public MenuPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        add(new JLabel("Mode:"));
        add(modeCombo);
        modeCombo.setSelectedIndex(2); // default EVE

        speedSlider.setMajorTickSpacing(2);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(400, 56));
        // rótulos personalizados: mapa inteiro -> label (valor interno / 2 = fator mostrado)
        java.util.Hashtable<Integer, JLabel> labels = new java.util.Hashtable<>();
        labels.put(1, new JLabel("0.5x"));
        labels.put(2, new JLabel("1x"));
        labels.put(4, new JLabel("2x"));
        labels.put(10, new JLabel("5x"));
        labels.put(20, new JLabel("10x"));
        labels.put(40, new JLabel("20x"));
        speedSlider.setLabelTable(labels);
        add(new JLabel("Speed:"));
        add(speedSlider);

        add(configBtn);
        add(statusBtn);
        add(startBtn);
        add(resetBtn);
    }
    public int getSelectedMode() { return modeCombo.getSelectedIndex() + 1; }
    // retorna o fator de velocidade como double (por exemplo 1.5, 2.0)
    public double getSpeed() { return speedSlider.getValue() / 2.0; }

    public void addStartListener(ActionListener l) { startBtn.addActionListener(l); }
    public void addResetListener(ActionListener l) { resetBtn.addActionListener(l); }
    public void addSpeedChangeListener(ChangeListener l) { speedSlider.addChangeListener(l); }
    public void addConfigListener(ActionListener l) { configBtn.addActionListener(l); }
    public void addStatusListener(ActionListener l) { statusBtn.addActionListener(l); }

    public void setControlsEnabled(boolean enabled) {
        modeCombo.setEnabled(enabled);
        startBtn.setEnabled(enabled);
        resetBtn.setEnabled(enabled);
    }
}
