package ui;

import characters.Guerreiro;
import characters.Mago;
import characters.Personagem;
import characters.Ranger;
import characters.Tank;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    // Gerenciador de animações
    private final AnimationManager animationManager = new AnimationManager();

    // Configuração de desenho
    private static final int MARGEM = 40;
    private static final Color COR_MORTE = new Color(40, 40, 40);

    public ArenaView() {
        setBackground(Color.WHITE);
    }

    public void definirEquipes(List<Personagem> a, List<Personagem> b) {
        this.equipeA = a == null ? new ArrayList<>() : a;
        this.equipeB = b == null ? new ArrayList<>() : b;
        repaint();
    }

    public void iniciarAnimacaoAtaque(Personagem atacante, Personagem alvo, int dano) {
        animationManager.iniciarAnimacaoAtaque(atacante, alvo, dano);
        repaint();
    }

    public void iniciarAnimacaoAtaqueCritico(Personagem atacante, Personagem alvo, int dano) {
        animationManager.iniciarAnimacaoAtaqueCritico(atacante, alvo, dano);
        repaint();
    }

    public void setAnimationSpeed(double speedFactor) {
        animationManager.setAnimationSpeed(speedFactor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int centroX = w / 2;

        // Desenha equipes partindo do centro
        desenharEquipeDesdeoCentro(g, equipeA, centroX, h, true);  // true = lado esquerdo
        desenharEquipeDesdeoCentro(g, equipeB, centroX, h, false); // false = lado direito

        // Desenha os popups de dano usando o gerenciador de animações
        animationManager.renderizarPopupsDano(g, posicaoCentral);

        // Limpa animações finalizadas
        animationManager.limparAnimacoesFinalizadas();

        // continue repainting while animations exist for smooth frames
        if (animationManager.temAnimacoesAtivas()) {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    private void desenharEquipeDesdeoCentro(Graphics g, java.util.List<Personagem> equipe, int centroArenaX, int alturaTotal, boolean ladoEsquerdo) {
        // segurança: lista nula ou vazia não desenha nada
        if (equipe == null || equipe.isEmpty()) {
            return;
        }
        int quantidade = equipe.size();

        // espaço disponível em largura para cada bloco (metade da tela menos margens)
        int larguraDisponivel = centroArenaX - MARGEM;
        int alturaDisponivel = alturaTotal - MARGEM * 2;

        // Tenta várias configurações de colunas/linhas para encontrar a que melhor usa o espaço
        final int MIN_CELL = 12;
        final int BASE_GAP = 8;
        int bestColunas = 1;
        int bestLinhas = quantidade;
        double bestCell = 0;
        int maxTryCols = Math.min(quantidade, Math.max(1, larguraDisponivel / MIN_CELL));
        for (int c = 1; c <= maxTryCols; c++) {
            int l = (int) Math.ceil((double) quantidade / c);
            double maxCellW = (double) (larguraDisponivel - (c - 1) * BASE_GAP) / c;
            double maxCellH = (double) (alturaDisponivel - (l - 1) * BASE_GAP) / l;
            double cell = Math.min(maxCellW, maxCellH);
            if (cell > bestCell) {
                bestCell = cell;
                bestColunas = c;
                bestLinhas = l;
            }
        }

        int colunas = Math.max(1, bestColunas);
        int linhas = Math.max(1, bestLinhas);

        int tamanhoCelula = Math.max(MIN_CELL, (int) Math.floor(bestCell) - 4);
        int interCellGap = Math.max(BASE_GAP, tamanhoCelula / 4);

        // largura do bloco e posição inicial (encostado ao centro, depois se expande)
        int blocoWidth = colunas * tamanhoCelula + (colunas - 1) * interCellGap;
        int gapCentro = 12; // espaço entre centro da arena e bloco

        // calcula limites para não ultrapassar margens
        int totalWidth = centroArenaX * 2;
        int minStartX = MARGEM;
        int maxStartX = Math.max(MARGEM, totalWidth - MARGEM - blocoWidth);

        int startX;
        if (ladoEsquerdo) {
            startX = centroArenaX - gapCentro - blocoWidth; // bloco encostado ao centro
            if (startX < minStartX) startX = minStartX;
        } else {
            startX = centroArenaX + gapCentro; // blocos à direita do centro
            if (startX > maxStartX) startX = maxStartX;
        }

        // vertical: centraliza o bloco na arena e clampa para não sair das margens
        int blocoHeight = linhas * tamanhoCelula + (linhas - 1) * interCellGap;
        int startY = (alturaTotal - blocoHeight) / 2;
        int minStartY = MARGEM;
        int maxStartY = Math.max(MARGEM, alturaTotal - MARGEM - blocoHeight);
        if (startY < minStartY) startY = minStartY;
        if (startY > maxStartY) startY = maxStartY;

        // ordena: coloca Tanks nas primeiras posições
        java.util.List<Personagem> ordenada = new ArrayList<>();
        for (Personagem p : equipe) {
            if (p instanceof Tank) {
                ordenada.add(p);
            }
        }
        for (Personagem p : equipe) {
            if (!(p instanceof Tank)) {
                ordenada.add(p);
            }
        }

        int idx = 0;
        // preenche por colunas (column-major).
        // Para o time esquerdo queremos que as primeiras colunas ocupadas fiquem mais próximas do centro,
        // então iteramos as colunas de trás para frente; para o direito iteramos normalmente.
        if (ladoEsquerdo) {
            for (int c = colunas - 1; c >= 0 && idx < quantidade; c--) {
                for (int r = 0; r < linhas && idx < quantidade; r++) {
                    Personagem p = ordenada.get(idx++);

                    int passo = tamanhoCelula + interCellGap;
                    int centroXpos = startX + c * passo + tamanhoCelula / 2;
                    int centroYpos = startY + r * passo + tamanhoCelula / 2;
                    int raio = tamanhoCelula / 2;

                    // cor base: delega a definição ao próprio personagem (encapsulado nas classes)
                    Color corBase;
                    if (p instanceof Guerreiro) {
                        corBase = Guerreiro.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Mago) {
                        corBase = Mago.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Ranger) {
                        corBase = Ranger.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Tank) {
                        corBase = Tank.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else {
                        corBase = p.personagemVivo() ? new Color(80, 160, 220) : COR_MORTE;
                    }

                    // posição de desenho pode ser deslocada se o personagem estiver atacando (lunge)
                    int drawX = centroXpos;
                    int drawY = centroYpos;

                    // se este personagem é atacante em uma animação ativa, calcula deslocamento em direção ao alvo
                    AnimationManager.AnimacaoAtaque animAtacante = animationManager.acharAtaquePorAtacante(p);
                    if (animAtacante != null) {
                        Point destino = posicaoCentral.get(animAtacante.getAlvo());
                        if (destino != null) {
                            Point deslocado = animationManager.calcularDeslocamentoAtaque(
                                    new Point(centroXpos, centroYpos), destino, raio, animAtacante.progresso()
                            );
                            drawX = deslocado.x;
                            drawY = deslocado.y;
                        }
                    }

                    // se este personagem é alvo em uma animação ativa, aplicaremos um overlay de flash
                    AnimationManager.AnimacaoAtaque animAlvo = animationManager.acharAtaquePorAlvo(p);

                    // atualiza posição base (usada pelos popups de dano)
                    posicaoCentral.put(p, new Point(centroXpos, centroYpos));

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
                    } catch (Exception ignored) {
                    }

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
                    } catch (Exception ignored) {
                    }
                }
            }
        } else {
            for (int c = 0; c < colunas && idx < quantidade; c++) {
                for (int r = 0; r < linhas && idx < quantidade; r++) {
                    Personagem p = ordenada.get(idx++);

                    int passo = tamanhoCelula + interCellGap;
                    int centroXpos = startX + c * passo + tamanhoCelula / 2;
                    int centroYpos = startY + r * passo + tamanhoCelula / 2;
                    int raio = tamanhoCelula / 2;

                    // cor base: delega a definição ao próprio personagem (encapsulado nas classes)
                    Color corBase;
                    if (p instanceof Guerreiro) {
                        corBase = Guerreiro.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Mago) {
                        corBase = Mago.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Ranger) {
                        corBase = Ranger.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else if (p instanceof Tank) {
                        corBase = Tank.corPrincipal(ladoEsquerdo);
                        if (!p.personagemVivo()) {
                            corBase = COR_MORTE;
                        }
                    } else {
                        corBase = p.personagemVivo() ? new Color(80, 160, 220) : COR_MORTE;
                    }

                    // posição de desenho pode ser deslocada se o personagem estiver atacando (lunge)
                    int drawX = centroXpos;
                    int drawY = centroYpos;

                    // se este personagem é atacante em uma animação ativa, calcula deslocamento em direção ao alvo
                    AnimationManager.AnimacaoAtaque animAtacante = animationManager.acharAtaquePorAtacante(p);
                    if (animAtacante != null) {
                        Point destino = posicaoCentral.get(animAtacante.getAlvo());
                        if (destino != null) {
                            Point deslocado = animationManager.calcularDeslocamentoAtaque(
                                    new Point(centroXpos, centroYpos), destino, raio, animAtacante.progresso()
                            );
                            drawX = deslocado.x;
                            drawY = deslocado.y;
                        }
                    }

                    // se este personagem é alvo em uma animação ativa, aplicaremos um overlay de flash
                    AnimationManager.AnimacaoAtaque animAlvo = animationManager.acharAtaquePorAlvo(p);

                    // atualiza posição base (usada pelos popups de dano)
                    posicaoCentral.put(p, new Point(centroXpos, centroYpos));

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
                    } catch (Exception ignored) {
                    }

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
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }
}
