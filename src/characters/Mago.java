package characters;

import java.awt.Color;
import java.util.List;

public class Mago extends Personagem {

    public Mago(String nome) {
        // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
        super(
                nome,
                10 + statusAleatorio(-1, 3),
                8 + statusAleatorio(-2, 4),
                2 + statusAleatorio(0, 1),
                0,
                20 + statusAleatorio(5, 10),
                5 + statusAleatorio(2, 2),
                3 + statusAleatorio(0, 5),
                List.of(0, -1) // Linhas de ataque preferidas
        );
    }

    //Magia --------------------------------------------
    @Override
    public List<Integer> usarMagia() {
        boolean ok = gastarMana(custoMana);
        if (ok) {
            return List.of(dano, 1); // 1 = sucesso
        } else {
            return List.of(0, 0); // 0 = falha
        }
    }

    // Cores padrão para desenho
    public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(100, 150, 220) : new Color(160, 120, 220);
    }

    public static Color corAlternativa() {
        return new Color(180, 190, 230);
    }
}
