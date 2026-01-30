package characters;

import java.awt.Color;

public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
        super(
                nome,
                16 + statusAleatorio(5),
                5 + statusAleatorio(2),  
                5 + statusAleatorio(4), 
                0,
                0,
                0,
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
