package characters;

import java.util.List;

public abstract class Personagem {

    private final String nome;
    private int vida, defesa, critico, dano; 
    private int mana, manaRegen, custoMana;
    private final int vidaMax, manaMax;

    public Personagem(String nome, int vida, int mana, int dano, int manaRegen, int defesa, int critico) {
        this.nome = nome;
        this.vida = vidaMax = vida;
        this.mana = manaMax = mana;
        this.dano = dano;
        this.manaRegen = manaRegen;
        this.defesa = defesa;
        this.critico = critico;
    }

    //Aqui tem os metodos de batalha ----------------------------
    public int receberDano(int dano) {
        int danoTotal = dano - defesa;
        if (danoTotal < 0) {
            danoTotal = 0;
        }

        vida = vida - danoTotal;
        if (vida < 0) {
            vida = 0;
        }

        return vida;
    }

    //overload
    public int receberDano(int dano, boolean isMagico) {
        if (isMagico) {
            //Dano Magico
            if (dano < 0) {
                dano = 0;
            }

            vida = vida - dano;
            if (vida < 0) {
                vida = 0;
            }

            return vida;
        } else {
            //Dano normal
            return receberDano(dano);
        }
    }

    public int atacar() {
        if (mana < manaMax) {
            recuperarMana();
        }
        //TODO: implementar logica de ataque com critico
        return dano;
    }

    //Magias --------------------------------------------
    public List<Integer> usarMagia() {
        if (gastarMana(custoMana)) {
            return List.of(dano, 1); // 1 = sucesso
        } else {
            return List.of(0, 0); // 0 = falha
        }
    }

    public boolean gastarMana(int custo) {
        if (mana >= custo) {
            mana -= custo;
            return true;
        } else {
            return false;
        }
    }

    public void recuperarMana() {
        mana += manaRegen;
        if (mana > manaMax) {
            mana = manaMax;
        }
    }

    //Status --------------------------------------------
    public List<Integer> mostrarStatus() {
        return List.of(vida, vidaMax, mana, manaMax, defesa, critico, dano, manaRegen);
    }

    public boolean personagemVivo() {
        return vida > 0;
    }
}
