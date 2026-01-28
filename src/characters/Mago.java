package characters;

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
}

