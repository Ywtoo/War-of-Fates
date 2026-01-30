package ui;

import characters.Guerreiro;
import characters.Mago;
import characters.Personagem;
import characters.Ranger;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.*;

//Animação dos personagens na arena
public class ArenaView extends JPanel {
    // Equipes que a view desenha
    private List<Personagem> equipeA = new ArrayList<>();
    private List<Personagem> equipeB = new ArrayList<>();
    // Posição central desenhada por personagem (para animações)
    private final Map<Personagem, Point> posicaoCentral = new HashMap<>();

    // Configuração de desenho/animação (valores base)
    private static final int MARGEM = 40;
    private static final Color COR_MORTE = new Color(40, 40, 40);
    private int baseDuracaoAtaqueMs = 450;
    private int baseDuracaoPopupMs = 800;
    private static final int MIN_DESLOCAMENTO = 16;
    private static final double ESCALA_DESLOCAMENTO = 2.2;
    // fator de velocidade (1 = normal, >1 = mais rápido)
    private double velocidadeFator = 1.0;

    public ArenaView() {
        setBackground(Color.WHITE);
    }

    // Representa uma animacao de ataque entre dois personagens
    private static class AnimacaoAtaque {
        private final Personagem atacante;
        private final Personagem alvo;
        private final long inicioMs;
        private final int dano;
        private final int duracaoMs;

        AnimacaoAtaque(Personagem atacante, Personagem alvo, int dano, int duracaoMs) {
            this.atacante = atacante;
            this.alvo = alvo;
            this.dano = dano;
            this.duracaoMs = duracaoMs;
            this.inicioMs = System.currentTimeMillis();
        }

        double progresso() {
            return Math.min(1.0, (System.currentTimeMillis() - inicioMs) / (double) duracaoMs);
        }

        boolean terminado() { return progresso() >= 1.0; }

        Personagem getAtacante() { return atacante; }
        Personagem getAlvo() { return alvo; }
        int getDano() { return dano; }
    }

    // Texto flutuante de dano sobre um personagem
    private static class PopupDano {
        private final Personagem alvo;
        private final long inicioMs;
        private final int dano;
        private final int duracaoMs;
        private final boolean critico;

        PopupDano(Personagem alvo, int dano, int duracaoMs) {
            this.alvo = alvo;
            this.dano = dano;
            this.duracaoMs = duracaoMs;
            this.inicioMs = System.currentTimeMillis();
            this.critico = false;
        }

        PopupDano(Personagem alvo, int dano, int duracaoMs, boolean critico) {
            this.alvo = alvo;
            this.dano = dano;
            this.duracaoMs = duracaoMs;
            this.inicioMs = System.currentTimeMillis();
            this.critico = critico;
        }

        double progresso() {
            return Math.min(1.0, (System.currentTimeMillis() - inicioMs) / (double) duracaoMs);
        }

        boolean terminado() { return progresso() >= 1.0; }

        Personagem getAlvo() { return alvo; }
        int getDano() { return dano; }
        boolean isCritico() { return critico; }
    }

    private final java.util.List<AnimacaoAtaque> ataquesAtivos = new ArrayList<>();
    private final java.util.List<PopupDano> avisosDano = new ArrayList<>();

    public void definirEquipes(List<Personagem> a, List<Personagem> b) {
        this.equipeA = a == null ? new ArrayList<>() : a;
        this.equipeB = b == null ? new ArrayList<>() : b;
        repaint();
    }

    public void iniciarAnimacaoAtaque(Personagem atacante, Personagem alvo, int dano) {
        int duracaoAtaque = Math.max(60, (int) (baseDuracaoAtaqueMs / Math.max(0.1, velocidadeFator)));
        int duracaoPopup = Math.max(60, (int) (baseDuracaoPopupMs / Math.max(0.1, velocidadeFator)));
        ataquesAtivos.add(new AnimacaoAtaque(atacante, alvo, dano, duracaoAtaque));
        avisosDano.add(new PopupDano(alvo, dano, duracaoPopup));
        repaint();
    }

    public void iniciarAnimacaoAtaqueCritico(Personagem atacante, Personagem alvo, int dano) {
        int duracaoAtaque = Math.max(60, (int) (baseDuracaoAtaqueMs / Math.max(0.1, velocidadeFator)));
        int duracaoPopup = Math.max(80, (int) (baseDuracaoPopupMs * 1.2 / Math.max(0.1, velocidadeFator)));
        ataquesAtivos.add(new AnimacaoAtaque(atacante, alvo, dano, duracaoAtaque));
        avisosDano.add(new PopupDano(alvo, dano, duracaoPopup, true));
        repaint();
    }

    public void setAnimationSpeed(double speedFactor) {
        this.velocidadeFator = Math.max(0.1, speedFactor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int areaWidth = (w - 3 * MARGEM) / 2;

        desenharEquipe(g, equipeA, MARGEM, MARGEM, areaWidth, h - 2 * MARGEM);
        desenharEquipe(g, equipeB, MARGEM * 2 + areaWidth, MARGEM, areaWidth, h - 2 * MARGEM);

        // Desenha os popups de dano
        Iterator<PopupDano> itPopup = avisosDano.iterator();
        while (itPopup.hasNext()) {
            PopupDano popup = itPopup.next();
            // posição base do personagem alvo
            Point pos = posicaoCentral.get(popup.getAlvo());
            if (pos == null) { itPopup.remove(); continue; }

            // progresso da animação: 0.0 (início) .. 1.0 (fim)
            double prog = popup.progresso();
            // opacidade decai com o tempo
            int opacidade = (int) (255 * (1.0 - prog));
            // deslocamento vertical: sobe até 20 pixels
            int deslocY = (int) (-20 * prog);

            if (popup.isCritico()) {
                g.setColor(new Color(255, 200, 40, Math.max(50, opacidade)));
                g.setFont(g.getFont().deriveFont(Font.BOLD, 20f));
            } else {
                g.setColor(new Color(220, 40, 40, Math.max(30, opacidade)));
                g.setFont(g.getFont().deriveFont(Font.BOLD, 14f));
            }
            String texto = (popup.isCritico() ? "CRIT! -" : "-") + popup.getDano();
            FontMetrics fm = g.getFontMetrics();
            int larguraTexto = fm.stringWidth(texto);

            // centraliza horizontalmente em relação à posição do personagem
            int xTexto = pos.x - larguraTexto / 2;
            int yTexto = pos.y - 20 + deslocY; // 20px acima do centro por padrão
            g.drawString(texto, xTexto, yTexto);

            // remove popup quando a animação termina
            if (popup.terminado()) itPopup.remove();
        }
        // Remove animações de ataque já finalizadas para não acumular memória
        Iterator<AnimacaoAtaque> ita = ataquesAtivos.iterator();
        while (ita.hasNext()) {
            AnimacaoAtaque aa = ita.next();
            if (aa.terminado()) ita.remove();
        }
        // continue repainting while animations exist for smooth frames
        if (!ataquesAtivos.isEmpty() || !avisosDano.isEmpty()) {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    private void desenharEquipe(Graphics g, java.util.List<Personagem> equipe, int x, int y, int width, int height) {
        // segurança: lista nula ou vazia não desenha nada
        if (equipe == null || equipe.isEmpty()) return;

        // quantidade e grade (colunas x linhas) para posicionar personagens
        int quantidade = equipe.size();
        int colunas = Math.max(1, (int) Math.sqrt(quantidade));
        int linhas = (int) Math.ceil((double) quantidade / colunas);
        int larguraCelula = width / colunas;
        int alturaCelula = height / Math.max(1, linhas);

        int idx = 0;
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                if (idx >= quantidade) break;
                Personagem p = equipe.get(idx++);

                // centro da célula onde o personagem será desenhado
                int centroX = x + coluna * larguraCelula + larguraCelula / 2;
                int centroY = y + linha * alturaCelula + alturaCelula / 2;
                int raio = Math.min(larguraCelula, alturaCelula) / 3;

                // cor base: delega a definição ao próprio personagem (encapsulado nas classes)
                boolean ladoA = x < getWidth() / 2;
                Color corBase;
                if (p instanceof Guerreiro) {
                    corBase = Guerreiro.corPrincipal(ladoA);
                    if (!p.personagemVivo()) corBase = COR_MORTE;
                } else if (p instanceof Mago) {
                    corBase = Mago.corPrincipal(ladoA);
                    if (!p.personagemVivo()) corBase = COR_MORTE;
                } else if (p instanceof Ranger) {
                    corBase = Ranger.corPrincipal(ladoA);
                    if (!p.personagemVivo()) corBase = COR_MORTE;
                } else {
                    corBase = p.personagemVivo() ? new Color(80, 160, 220) : COR_MORTE;
                }

                // posição de desenho pode ser deslocada se o personagem estiver atacando (lunge)
                int drawX = centroX;
                int drawY = centroY;

                // se este personagem é atacante em uma animação ativa, calcula deslocamento em direção ao alvo
                AnimacaoAtaque animAtacante = acharAtaquePorAtacante(p);
                if (animAtacante != null) {
                    Point destino = posicaoCentral.get(animAtacante.getAlvo());
                    if (destino != null) {
                        int deltaX = destino.x - centroX;
                        int deltaY = destino.y - centroY;
                        double distancia = Math.max(1.0, Math.hypot(deltaX, deltaY));
                        double progressoAnim = animAtacante.progresso();
                        // Animação ida/volta: 0..0.5 vai até o alvo, 0.5..1.0 volta
                        double localProg;
                        double easing;
                        if (progressoAnim <= 0.5) {
                            localProg = progressoAnim * 2.0;
                            easing = localProg * (2 - localProg);
                        } else {
                            localProg = (progressoAnim - 0.5) * 2.0;
                            // easing invertido para voltar
                            easing = 1.0 - (localProg * (2 - localProg));
                        }
                        int desloc = (int) (Math.max(MIN_DESLOCAMENTO, raio * ESCALA_DESLOCAMENTO) * easing);
                        drawX = (int) (centroX + (deltaX / distancia) * desloc);
                        drawY = (int) (centroY + (deltaY / distancia) * desloc);
                    }
                }

                // se este personagem é alvo em uma animação ativa, aplicaremos um overlay de flash
                AnimacaoAtaque animAlvo = acharAtaquePorAlvo(p);

                // atualiza posição base (usada pelos popups de dano)
                posicaoCentral.put(p, new Point(centroX, centroY));

                // desenha o círculo do personagem
                g.setColor(corBase);
                g.fillOval(drawX - raio, drawY - raio, raio * 2, raio * 2);
                g.setColor(Color.BLACK);
                g.drawOval(drawX - raio, drawY - raio, raio * 2, raio * 2);

                // overlay vermelho para alvo (opacidade decai com o progresso)
                if (animAlvo != null) {
                    double prog = animAlvo.progresso();
                    int alpha = (int) (200 * (1.0 - prog));
                    g.setColor(new Color(220, 40, 40, Math.max(30, alpha)));
                    g.fillOval(drawX - raio, drawY - raio, raio * 2, raio * 2);
                }

                // nome acima do personagem
                try {
                    String nome = p.getNome();
                    FontMetrics fm = g.getFontMetrics();
                    int larguraTexto = fm.stringWidth(nome);
                    g.drawString(nome, drawX - larguraTexto / 2, drawY - raio - 6);
                } catch (Exception ignored) {}

                // barra de vida abaixo
                try {
                    java.util.List<Integer> st = p.mostrarStatus();
                    int vida = st.get(0);
                    int vidaMax = st.get(1);
                    int larguraBarra = raio * 2;
                    int preenchimento = Math.max(2, (int) ((larguraBarra) * ((double) vida / Math.max(1, vidaMax))));
                    // barra de vida
                    g.setColor(Color.RED);
                    g.fillRect(drawX - raio, drawY + raio + 4, larguraBarra, 6);
                    g.setColor(Color.GREEN);
                    g.fillRect(drawX - raio, drawY + raio + 4, preenchimento, 6);
                    g.setColor(Color.BLACK);
                    g.drawRect(drawX - raio, drawY + raio + 4, larguraBarra, 6);

                    // barra de mana (abaixo da vida)
                    int mana = st.get(2);
                    int manaMax = st.get(3);
                    int yMana = drawY + raio + 4 + 6 + 4;
                    int preenchimentoMana = Math.max(2, (int) ((larguraBarra) * ((double) mana / Math.max(1, manaMax))));
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(drawX - raio, yMana, larguraBarra, 6);
                    g.setColor(new Color(50, 120, 220));
                    g.fillRect(drawX - raio, yMana, preenchimentoMana, 6);
                    g.setColor(Color.BLACK);
                    g.drawRect(drawX - raio, yMana, larguraBarra, 6);
                } catch (Exception ignored) {}
            }
        }
    }

    // --- helpers para clarear a lógica de busca de animações ativas ---
    private AnimacaoAtaque acharAtaquePorAtacante(Personagem p) {
        for (AnimacaoAtaque aa : ataquesAtivos) {
            if (aa.getAtacante() == p && !aa.terminado()) return aa;
        }
        return null;
    }

    private AnimacaoAtaque acharAtaquePorAlvo(Personagem p) {
        for (AnimacaoAtaque aa : ataquesAtivos) {
            if (aa.getAlvo() == p && !aa.terminado()) return aa;
        }
        return null;
    }
}
