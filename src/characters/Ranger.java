package characters;

import java.awt.Color;
import java.util.List;

public class Ranger extends Personagem {

    public Ranger(String nome) {
        // (String nome, int vida, int dano, int defesa, int crit, it mana, int custoMana, int manaRegen)
        super(
                nome,
                12 + statusAleatorio(-2, 2),
                7 + statusAleatorio(-2, 5),
                1 + statusAleatorio(0, 2),
                10 + statusAleatorio(0, 25),
                0,
                0,
                0,
                List.of(0, -1) // Linhas de ataque preferidas
        );
    }

    ;


public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(100, 180, 120) : new Color(140, 180, 120);
    }

    public static Color corAlternativa() {
        return new Color(180, 220, 200);
    }
}
