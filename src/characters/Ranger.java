public class Ranger extends Personagem {
            public Ranger(String nome) {
            // (String nome, int vida, int dano, int defesa, int crit, it mana, int custoMana, int manaRegen)
            super(
                nome,
                12 + statusAleatorio(2),
                7 + statusAleatorio(7),
                1 + statusAleatorio(2),
                15 + statusAleatorio(30),
                0,
                0,
                0, 0, 0)
            };
}

public static Color corPrincipal(boolean ladoA) {
    return ladoA ? new Color(0, 277, 107) : new Color(107, 0, 277);
}

public static Color corAlternativa() {
    return new Color(240, 55, 55);
}
