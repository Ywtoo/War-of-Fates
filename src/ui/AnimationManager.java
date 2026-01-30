package ui;

import characters.Personagem;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gerenciador de animações para a arena de batalha.
 * Controla animações de ataque e popups de dano.
 */
public class AnimationManager {
    // Animações e popups ativos
    private final List<AnimacaoAtaque> ataquesAtivos = new ArrayList<>();
    private final List<PopupDano> avisosDano = new ArrayList<>();

    // Configurações de duração base
    private int baseDuracaoAtaqueMs = 450;
    private int baseDuracaoPopupMs = 800;
    private double velocidadeFator = 1.0;

    // Constantes de renderização
    private static final int MIN_DESLOCAMENTO = 16;
    private static final double ESCALA_DESLOCAMENTO = 2.2;

    /**
     * Representa uma animação de ataque entre dois personagens.
     */
    public static class AnimacaoAtaque {
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

        public double progresso() {
            return Math.min(1.0, (System.currentTimeMillis() - inicioMs) / (double) duracaoMs);
        }

        public boolean terminado() {
            return progresso() >= 1.0;
        }

        public Personagem getAtacante() {
            return atacante;
        }

        public Personagem getAlvo() {
            return alvo;
        }

        public int getDano() {
            return dano;
        }
    }

    /**
     * Texto flutuante de dano sobre um personagem.
     */
    public static class PopupDano {
        private final Personagem alvo;
        private final long inicioMs;
        private final int dano;
        private final int duracaoMs;
        private final boolean critico;

        PopupDano(Personagem alvo, int dano, int duracaoMs) {
            this(alvo, dano, duracaoMs, false);
        }

        PopupDano(Personagem alvo, int dano, int duracaoMs, boolean critico) {
            this.alvo = alvo;
            this.dano = dano;
            this.duracaoMs = duracaoMs;
            this.inicioMs = System.currentTimeMillis();
            this.critico = critico;
        }

        public double progresso() {
            return Math.min(1.0, (System.currentTimeMillis() - inicioMs) / (double) duracaoMs);
        }

        public boolean terminado() {
            return progresso() >= 1.0;
        }

        public Personagem getAlvo() {
            return alvo;
        }

        public int getDano() {
            return dano;
        }

        public boolean isCritico() {
            return critico;
        }
    }

    /**
     * Inicia uma animação de ataque normal.
     */
    public void iniciarAnimacaoAtaque(Personagem atacante, Personagem alvo, int dano) {
        int duracaoAtaque = Math.max(60, (int) (baseDuracaoAtaqueMs / Math.max(0.1, velocidadeFator)));
        int duracaoPopup = Math.max(60, (int) (baseDuracaoPopupMs / Math.max(0.1, velocidadeFator)));
        ataquesAtivos.add(new AnimacaoAtaque(atacante, alvo, dano, duracaoAtaque));
        avisosDano.add(new PopupDano(alvo, dano, duracaoPopup));
    }

    /**
     * Inicia uma animação de ataque crítico.
     */
    public void iniciarAnimacaoAtaqueCritico(Personagem atacante, Personagem alvo, int dano) {
        int duracaoAtaque = Math.max(60, (int) (baseDuracaoAtaqueMs / Math.max(0.1, velocidadeFator)));
        int duracaoPopup = Math.max(80, (int) (baseDuracaoPopupMs * 1.2 / Math.max(0.1, velocidadeFator)));
        ataquesAtivos.add(new AnimacaoAtaque(atacante, alvo, dano, duracaoAtaque));
        avisosDano.add(new PopupDano(alvo, dano, duracaoPopup, true));
    }

    /**
     * Define a velocidade das animações.
     *
     * @param speedFactor Fator de velocidade (1 = normal, >1 = mais rápido)
     */
    public void setAnimationSpeed(double speedFactor) {
        this.velocidadeFator = Math.max(0.1, speedFactor);
    }

    /**
     * Remove animações já finalizadas.
     */
    public void limparAnimacoesFinalizadas() {
        Iterator<AnimacaoAtaque> ita = ataquesAtivos.iterator();
        while (ita.hasNext()) {
            if (ita.next().terminado()) {
                ita.remove();
            }
        }

        Iterator<PopupDano> itPopup = avisosDano.iterator();
        while (itPopup.hasNext()) {
            if (itPopup.next().terminado()) {
                itPopup.remove();
            }
        }
    }

    /**
     * Busca uma animação de ataque ativa onde o personagem é o atacante.
     */
    public AnimacaoAtaque acharAtaquePorAtacante(Personagem p) {
        for (AnimacaoAtaque aa : ataquesAtivos) {
            if (aa.getAtacante() == p && !aa.terminado()) {
                return aa;
            }
        }
        return null;
    }

    /**
     * Busca uma animação de ataque ativa onde o personagem é o alvo.
     */
    public AnimacaoAtaque acharAtaquePorAlvo(Personagem p) {
        for (AnimacaoAtaque aa : ataquesAtivos) {
            if (aa.getAlvo() == p && !aa.terminado()) {
                return aa;
            }
        }
        return null;
    }

    /**
     * Verifica se há animações ativas.
     */
    public boolean temAnimacoesAtivas() {
        return !ataquesAtivos.isEmpty() || !avisosDano.isEmpty();
    }

    /**
     * Calcula o deslocamento de um personagem atacante em direção ao alvo.
     *
     * @param posAtacante Posição central do atacante
     * @param posAlvo Posição central do alvo
     * @param raio Raio do personagem (para calcular deslocamento proporcional)
     * @param progressoAnim Progresso da animação (0.0 a 1.0)
     * @return Point com as coordenadas deslocadas
     */
    public Point calcularDeslocamentoAtaque(Point posAtacante, Point posAlvo, int raio, double progressoAnim) {
        int deltaX = posAlvo.x - posAtacante.x;
        int deltaY = posAlvo.y - posAtacante.y;
        double distancia = Math.max(1.0, Math.hypot(deltaX, deltaY));

        // Animação ida/volta: 0..0.5 vai até o alvo, 0.5..1.0 volta
        double localProg;
        double easing;
        if (progressoAnim <= 0.5) {
            localProg = progressoAnim * 2.0;
            easing = localProg * (2 - localProg);
        } else {
            localProg = (progressoAnim - 0.5) * 2.0;
            easing = 1.0 - (localProg * (2 - localProg));
        }

        int desloc = (int) (Math.max(MIN_DESLOCAMENTO, raio * ESCALA_DESLOCAMENTO) * easing);
        int drawX = (int) (posAtacante.x + (deltaX / distancia) * desloc);
        int drawY = (int) (posAtacante.y + (deltaY / distancia) * desloc);

        return new Point(drawX, drawY);
    }

    /**
     * Renderiza os popups de dano ativos.
     *
     * @param g Contexto gráfico
     * @param posicaoCentral Mapa com as posições centrais dos personagens
     */
    public void renderizarPopupsDano(Graphics g, java.util.Map<Personagem, Point> posicaoCentral) {
        Iterator<PopupDano> itPopup = avisosDano.iterator();
        while (itPopup.hasNext()) {
            PopupDano popup = itPopup.next();
            Point pos = posicaoCentral.get(popup.getAlvo());
            if (pos == null) {
                itPopup.remove();
                continue;
            }

            double prog = popup.progresso();
            int opacidade = (int) (255 * (1.0 - prog));
            int deslocY = (int) (-20 * prog);

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                if (popup.isCritico()) {
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
                } else {
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
                }

                String texto = (popup.isCritico() ? "CRIT! -" : "-") + popup.getDano();
                FontMetrics fm = g2.getFontMetrics();
                int larguraTexto = fm.stringWidth(texto);
                int xTexto = pos.x - larguraTexto / 2;
                int yTexto = pos.y - 20 + deslocY;

                java.awt.font.GlyphVector gv = g2.getFont().createGlyphVector(g2.getFontRenderContext(), texto);
                Shape textoShape = gv.getOutline(xTexto, yTexto);

                Color fillColor = popup.isCritico()
                        ? new Color(255, 200, 40, Math.max(50, opacidade))
                        : new Color(220, 40, 40, Math.max(30, opacidade));
                Color borderColor = new Color(0, 0, 0, Math.max(100, opacidade));

                float strokeWidth = popup.isCritico() ? 3f : 2f;
                g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(borderColor);
                g2.draw(textoShape);
                g2.setColor(fillColor);
                g2.fill(textoShape);
            } finally {
                g2.dispose();
            }

            if (popup.terminado()) {
                itPopup.remove();
            }
        }
    }
}
