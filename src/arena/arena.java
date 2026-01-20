package arena;

import battle.Batalha;
import battle.Equipe;
import characters.Personagem;


public class Arena {

    private int round;
    private int gameMode;
    private int attackmode;  // 1 - PLayer | 2 - Aleatorio

    public Arena(int gameMode) {
        this.round = 1; 
        this.gameMode = gameMode; // 1 - PVP | 2 - PVE | 3 - EVE
    }

    public void iniciar(Equipe equipeA, Equipe equipeB) {
        while (equipeA.getVivos().size() != 0 && equipeB.getVivos().size() != 0) {
            attackmode = 0;

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
        if (equipeA.getVivos().size() == 0) {
            //Equipe B venceu
        } else if (equipeB.getVivos().size() == 0) {
            //Equipe A venceu
        } else {
            //Empate
        }
    }

    private void executarAtaques(Equipe atacantes, Equipe defensores, int attackmode) {

        for (Personagem membro : atacantes.getVivos()) {
            if (defensores.getVivos().size() == 0) break;

            Personagem alvo;

            if (attackmode == 1) {
                alvo = defensores.escolherAlvoJogador();
            } else {
                alvo = defensores.escolherAlvoAleatorio();
            }
            Batalha.atacar1v1(membro, alvo);
        }
    }
}
