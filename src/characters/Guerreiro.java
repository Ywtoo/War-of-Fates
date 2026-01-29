package characters;

import java.awt.Color;

public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
        super(
                nome,
                20 + statusAleatorio(3),
                4 + statusAleatorio(1),  
                3 + statusAleatorio(1), 
                0,
                0,
                10,
                0
        );
    }

    // Cores padrão para desenho (encapsulado na classe)
    public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(120, 200, 120) : new Color(200, 120, 120);
    }

    public static Color corAlternativa() {
        return new Color(80, 160, 220);
    }
}
