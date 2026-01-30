package characters;

import java.awt.Color;

public class Tank extends Personagem {

    public Tank(String nome) {
        // (String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen)
        super(
                nome,
                25 + statusAleatorio(-5, 10),
                3 + statusAleatorio(0, 5),
                6 + statusAleatorio(0, 4),
                10 + statusAleatorio(-10, 15),
                10,
                10,
                3 + statusAleatorio(-2, 1)
        );
    }

    //Habilidade especial --------------------------------------------
    @Override
    public int receberDano(int dano) {
        boolean ok = gastarMana(custoMana);
        int defesaEspecial = defesa;

        if (ok) {
            defesaEspecial += 99; // Aumenta a defesa temporariamente
        }

        int danoTotal = dano - defesaEspecial;
        if (danoTotal < 0) {
            danoTotal = 0;
        }

        vida = vida - danoTotal;
        if (vida < 0) {
            vida = 0;
        }

        return danoTotal;
    }

    //overload
    @Override
    public int receberDano(int dano, boolean isMagico) {
        if (isMagico) {
            //Dano Magico
            boolean ok = gastarMana(custoMana);

            if (ok) {
                dano = dano / 2; // Reduz o dano magico pela metade
            }
            if (dano < 0) {
                dano = 0;
            }

            vida = vida - dano;
            if (vida < 0) {
                vida = 0;
            }

            return dano;
        } else {
            //Dano normal
            return receberDano(dano);
        }
    }

    // Cores padrão para desenho (encapsulado na classe)
    public static Color corPrincipal(boolean ladoA) {
        return ladoA ? new Color(200, 200, 200) : new Color(200, 170, 170);
    }

    public static Color corAlternativa() {
        return new Color(80, 160, 220);
    }
}
