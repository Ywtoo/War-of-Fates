package characters;

import java.awt.Color;

public class Mago extends Personagem {
                public Mago(String nome) {
                // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
                super(
                                nome,
                                10 + statusAleatorio(3),
                                3 + statusAleatorio(1),  
                                2 + statusAleatorio(1), 
                                0,
                                20 + statusAleatorio(5),
                                5 + statusAleatorio(2),
                                3 + statusAleatorio(3)
                );
        }

        public static Color corPrincipal(boolean ladoA) {
                return ladoA ? new Color(160, 120, 220) : new Color(220, 160, 120);
        }

        public static Color corAlternativa() {
                return new Color(200, 160, 220);
        }
}

