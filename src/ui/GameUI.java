package ui;

import arena.Arena;
import arena.ArenaListener;
import battle.Equipe;
import characters.Guerreiro;
import characters.Mago;
import characters.Personagem;
import characters.Ranger;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeListener;

// Main UI controi a janlea principal atualiza a visualizacao
public class GameUI {
    private final JFrame janelaPrincipal = new JFrame("War of Fates - Demo");
    private final MenuPanel painelControles = new MenuPanel();
    private final ArenaView visualizadorArena = new ArenaView();
    private final ConsolePanel painelConsole = new ConsolePanel();

    private Equipe equipeTimeA;
    private Equipe equipeTimeB;
    private List<Personagem> personagensTimeA;
    private List<Personagem> personagensTimeB;
    // configuração simples: lista de inteiros onde index 0 = Guerreiro, index 1 = Mago
    private java.util.List<Integer> configSimplesTimeA = null;
    private java.util.List<Integer> configSimplesTimeB = null;

    private Timer uiTimer;
    private Thread arenaThread;
    private Arena arena;
    private final int intervaloBaseMs = 400;

    public GameUI() {
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaPrincipal.setSize(1000, 700);
        janelaPrincipal.setLayout(new BorderLayout());

        janelaPrincipal.add(painelControles, BorderLayout.NORTH);
        janelaPrincipal.add(visualizadorArena, BorderLayout.CENTER);
        janelaPrincipal.add(painelConsole, BorderLayout.EAST);

        // Redirecionar System.out para o console da UI
        painelConsole.redirectSystemOut();

        // Listeners do menu
        painelControles.addStartListener(e -> iniciarSimulacao());
        painelControles.addResetListener(e -> reiniciar());
        painelControles.addSpeedChangeListener((ChangeListener) ev -> velocidadeAlterada());
        painelControles.addConfigListener(e -> abrirConfiguradorTimes());
        painelControles.addStatusListener(e -> abrirJanelaStatus());

        // inicializa velocidade da animação na visualização
        visualizadorArena.setAnimationSpeed(painelControles.getSpeed());

        // Temporizador para atualizar a UI periodicamente (calcula delay a partir do fator de velocidade)
        double initSpeed = painelControles.getSpeed();
        int initDelay = Math.max(5, (int) (intervaloBaseMs / initSpeed));
        uiTimer = new Timer(initDelay, ev -> atualizar());

        janelaPrincipal.setVisible(true);
    }

    // Menu actions
    private void iniciarSimulacao() {
        if (equipeTimeA != null || equipeTimeB != null) return;
        criarEquipes();
        painelControles.setControlsEnabled(false);

        int modo = painelControles.getSelectedMode();
        double speed = painelControles.getSpeed();
        int delay = Math.max(5, (int) (intervaloBaseMs / speed));
        arena = new Arena(modo, delay);
        // registra listener para animações e mensagens na UI
        arena.setListener(new ArenaListener() {
            @Override
            public void onAttack(Personagem att, Personagem tgt, int dmg) {
                SwingUtilities.invokeLater(() -> {
                    String extra = "";
                    try {
                        extra = " (atk=" + att.getDano() + " def=" + (tgt!=null?tgt.getDefesa():0) + ")";
                    } catch (Exception ignored) {}
                    painelConsole.append("[ATAQUE] " + att.getNome() + " -> " + (tgt!=null?tgt.getNome():"?") + " dmg=" + dmg + extra);
                    visualizadorArena.iniciarAnimacaoAtaque(att, tgt, dmg);
                });
            }

            @Override
            public void onCriticalAttack(Personagem att, Personagem tgt, int dmg) {
                SwingUtilities.invokeLater(() -> {
                    String extra = "";
                    try {
                        extra = " (atk=" + att.getDano() + " def=" + (tgt!=null?tgt.getDefesa():0) + ")";
                    } catch (Exception ignored) {}
                    painelConsole.append("[CRÍTICO] " + att.getNome() + " -> " + (tgt!=null?tgt.getNome():"?") + " dmg=" + dmg + extra);
                    visualizadorArena.iniciarAnimacaoAtaqueCritico(att, tgt, dmg);
                });
            }

            @Override
            public void onRoundStart(int round, int gameMode) {
                SwingUtilities.invokeLater(() -> painelConsole.append("\n=== [ARENA] Começando round " + round + " (modo=" + gameMode + ") ==="));
            }

            @Override
            public void onDelayChanged(int delayMs) {
                SwingUtilities.invokeLater(() -> painelConsole.append("[ARENA] Delay de ataques " + delayMs + " ms"));
            }

            @Override
            public void onFinish(Equipe winner) {
                SwingUtilities.invokeLater(() -> {
                    uiTimer.stop();
                    painelControles.setControlsEnabled(true);
                    String vencedor;
                    if (winner == null) {
                        vencedor = "Empate";
                    } else if (winner == equipeTimeA) {
                        vencedor = "Equipe A";
                    } else if (winner == equipeTimeB) {
                        vencedor = "Equipe B";
                    } else {
                        vencedor = "Desconhecido";
                    }
                    painelConsole.append("\n=== [ARENA] Vitória: " + vencedor + " ===");
                    JOptionPane.showMessageDialog(janelaPrincipal, "Batalha finalizada. Vencedor: " + vencedor);
                });
            }
        });
        arenaThread = new Thread(() -> arena.iniciar(equipeTimeA, equipeTimeB), "Arena-Thread");
        arenaThread.setDaemon(true);
        arenaThread.start();

        uiTimer.start();
        painelConsole.append("[Arena] Arena iniciada (modo=" + modo + ", speed=" + speed + "x)");
    }

    private void reiniciar() {
        if (arenaThread != null && arenaThread.isAlive()) {
            painelConsole.append("Não é possível reiniciar enquanto a Arena estiver em execução.");
            return;
        }
        uiTimer.stop();
        equipeTimeA = null; equipeTimeB = null;
        personagensTimeA = null; personagensTimeB = null;
        visualizadorArena.definirEquipes(new ArrayList<>(), new ArrayList<>());
        painelControles.setControlsEnabled(true);
        painelConsole.append("Simulação reiniciada");
    }

    private void velocidadeAlterada() {
        double velocidade = painelControles.getSpeed();
        int delay = Math.max(5, (int) (intervaloBaseMs / velocidade));

        if (uiTimer != null) uiTimer.setDelay(delay);
        if (arena != null) arena.setDelayMs(delay);

        // ajusta velocidade das animações para ficar em sincronia com o slider
        visualizadorArena.setAnimationSpeed(velocidade);
        painelConsole.append("[VELOCIDADE] " + velocidade + "x (" + delay + " ms)");
    }

    private void criarEquipes() {
        equipeTimeA = new Equipe();
        equipeTimeB = new Equipe();
        personagensTimeA = new ArrayList<>();
        personagensTimeB = new ArrayList<>();

        if (configSimplesTimeA != null && configSimplesTimeB != null) {
            //índice: 0 = Guerreiro, 1 = Mago, 2 = Ranger
            int guerreiroACount = configSimplesTimeA.size() > 0 ? configSimplesTimeA.get(0) : 0;
            int magoACount = configSimplesTimeA.size() > 1 ? configSimplesTimeA.get(1) : 0;
            int rangerACount = configSimplesTimeA.size() > 2 ? configSimplesTimeA.get(2) : 0;
            int guerreiroBCount = configSimplesTimeB.size() > 0 ? configSimplesTimeB.get(0) : 0;
            int magoBCount = configSimplesTimeB.size() > 1 ? configSimplesTimeB.get(1) : 0;
            int rangerBCount = configSimplesTimeB.size() > 2 ? configSimplesTimeB.get(2) : 0;

            for (int i = 0; i < guerreiroACount; i++) { Guerreiro g = new Guerreiro("A-Guerreiro-" + (i+1)); equipeTimeA.adicionar(g); personagensTimeA.add(g); }
            for (int i = 0; i < magoACount; i++) { Mago m = new Mago("A-Mago-" + (i+1)); equipeTimeA.adicionar(m); personagensTimeA.add(m); }
            for (int i = 0; i < rangerACount; i++) { Ranger r = new Ranger("A-Ranger-" + (i+1)); equipeTimeA.adicionar(r); personagensTimeA.add(r); }
            for (int i = 0; i < guerreiroBCount; i++) { Guerreiro g = new Guerreiro("B-Guerreiro-" + (i+1)); equipeTimeB.adicionar(g); personagensTimeB.add(g); }
            for (int i = 0; i < magoBCount; i++) { Mago m = new Mago("B-Mago-" + (i+1)); equipeTimeB.adicionar(m); personagensTimeB.add(m); }
            for (int i = 0; i < rangerBCount; i++) { Ranger r = new Ranger("B-Ranger-" + (i+1)); equipeTimeB.adicionar(r); personagensTimeB.add(r); }
        } else {
            // fallback: sem configuração, times ficam vazios (use Config Teams)
        }

        visualizadorArena.definirEquipes(personagensTimeA, personagensTimeB);
        painelConsole.append("Equipes criadas: A=" + personagensTimeA.size() + " B=" + personagensTimeB.size());
    }

    private void abrirConfiguradorTimes() {
        // diálogo simples (Guerreiro / Mago)
        TeamBuilder dlg = new TeamBuilder(janelaPrincipal);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            configSimplesTimeA = dlg.getCountsForA();
            configSimplesTimeB = dlg.getCountsForB();
            painelConsole.append("Configuração simples aplicada (Guerreiro, Mago, Ranger)");
        } else {
            painelConsole.append("Configuração de times cancelada");
        }
    }

    private void abrirJanelaStatus() {
        StatusDialog dlg = new StatusDialog(janelaPrincipal, personagensTimeA, personagensTimeB);
        dlg.setVisible(true);
    }

    private void atualizar() {
        if (equipeTimeA == null || equipeTimeB == null) {
            return;
        }
        // Atualiza visual com todos os personagens (vivos e mortos)
        if (personagensTimeA != null && personagensTimeB != null) {
            visualizadorArena.definirEquipes(personagensTimeA, personagensTimeB);
        }

        // Se a Arena ainda estiver executando, continuar polling
        if (arenaThread != null && arenaThread.isAlive()) {
            return;
        }

        // Fim: atualização final é tratada em onFinish do listener
    }
}