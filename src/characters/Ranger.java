package characters;

import java.awt.Color;

public class Ranger extends Personagem {

    public Ranger(String nome) {
        // (String nome, int vida, int dano, int defesa, int crit, it mana, int custoMana, int manaRegen)
        super(
                nome,
                12 + statusAleatorio(-2, 2),
                7 + statusAleatorio(-2, 7),
                1 + statusAleatorio(0, 2),
                15 + statusAleatorio(0, 30),
                0,
                0,
                0
        );
    }

    ;


public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(100, 180, 120) : new Color(180, 120, 120);
    }

    public static Color corAlternativa() {
        return new Color(180, 220, 200);
    }
}
