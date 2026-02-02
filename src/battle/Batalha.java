package battle;

import characters.Personagem;
import java.util.List;

public class Batalha {

    public static int atacar1v1(Personagem atacante, Personagem alvo) {
        List<Integer> resultadoMagia = atacante.usarMagia();

        if (resultadoMagia != null && resultadoMagia.get(1) == 1) {
            int danoMagico = resultadoMagia.get(0);
            // receberDano retorna o dano efetivamente aplicado
            return alvo.receberDano(danoMagico, true);
        } else {
            int dano = atacante.atacar();
            return alvo.receberDano(dano);
        }
    }
}
