package arena;

import battle.Batalha;
import battle.Equipe;
import characters.Personagem;

public class Arena {

    private int round;
    private final int gameMode;
    private int attackmode;  // 1 - PLayer | 2 - Aleatorio
    private int delayMs;
    private ArenaListener listener;

    public Arena(int gameMode, int delayMs) {
        this.round = 1;
        this.gameMode = gameMode; // 1 - PVP | 2 - PVE | 3 - EVE
        this.delayMs = delayMs;
    }

    public void iniciar(Equipe equipeA, Equipe equipeB) {
        while (equipeA.temVivos() && equipeB.temVivos()) {
            if (listener != null) {
                try { listener.onRoundStart(round, gameMode); } catch (Exception ignored) {}
            }

            //Logica de turnos --------------------
            if (round % 2 == 1) {
                attackmode = (gameMode == 1 || gameMode == 2) ? 1 : 2;
                executarAtaques(equipeA, equipeB, attackmode);
            } else {
                attackmode = (gameMode == 1) ? 1 : 2;
                executarAtaques(equipeB, equipeA, attackmode);
            }
            round++;
        }

        //Logica de vitoria --------------------
        Equipe ganhador = null;
        boolean aVazio = !equipeA.temVivos();
        boolean bVazio = !equipeB.temVivos();

        if (aVazio && bVazio) {
            ganhador = null;
        } else if (bVazio) {
            ganhador = equipeA;
        } else if (aVazio) {
            ganhador = equipeB;
        }

        if (listener != null) {
            try {
                listener.onFinish(ganhador);
            } catch (Exception ex) {
                System.err.println("[Arena] Listener Exception onFinish: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void executarAtaques(Equipe atacantes, Equipe defensores, int attackmode) {
        for (Personagem membro : atacantes.getVivos()) {
            if (!defensores.temVivos()) break;

            Personagem alvo;

            if (attackmode == 1) {
                alvo = defensores.escolherAlvoJogador();
            } else {
                alvo = defensores.escolherAlvoAleatorio();
            }

            int dano = Batalha.atacar1v1(membro, alvo);
            listener.onAttack(membro, alvo, dano);
            sleepMs(delayMs);
        }
        
    }

    //Auxiliares ---------------------------------------
    public void setDelayMs(int delayMs) {
        this.delayMs = delayMs;
        if (listener != null) {
            try { listener.onDelayChanged(this.delayMs); } catch (Exception ignored) {}
        }
    }

    public void setListener(ArenaListener l) {
        this.listener = l;
    }

    private void sleepMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
