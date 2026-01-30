package characters;

import java.awt.Color;
import java.util.List;

public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
        super(
                nome,
                16 + statusAleatorio(0, 5),
                5 + statusAleatorio(0, 2),  
                5 + statusAleatorio(0, 4), 
                10 + statusAleatorio(-10, 5),
                0,
                0,
                0,
                List.of(0) // Linhas de ataque preferidas
        );
    }

    // Cores padrão para desenho (encapsulado na classe)
    public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(200, 200, 200) : new Color(200, 150, 150);
    }

    public static Color corAlternativa() {
        return new Color(80, 160, 220);
    }
}
