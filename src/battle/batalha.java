package battle;

import characters.Personagem;

public class Batalha {

    public static int atacar1v1(Personagem atacante, Personagem alvo) {
        List<Integer> resultadoMagia = atacante.usarMagia();
        //Se personagem tem mana ele usará magia
        if (resultadoMagia.get(1) == 1) {
            int danoMagico = resultadoMagia.get(0);
            alvo.receberDano(danoMagico, true);
            return danoMagico;
        } else {
            int dano = atacante.atacar();
            alvo.receberDano(dano);
            return dano;
        }
    }
}
