package characters;

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
}
