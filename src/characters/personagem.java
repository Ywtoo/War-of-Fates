package characters;

import java.util.List;
import java.util.Random;

public abstract class Personagem {

    protected final String nome;
    protected int vida, defesa, critico, dano; 
    protected int mana, manaRegen, custoMana;
    protected final int vidaMax, manaMax;
    
    protected static final Random random = new Random();
    private boolean ultimoAtaqueCritico = false;

    protected static int statusAleatorio(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public Personagem(String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen) {
        this.nome = nome;
        this.vida = vidaMax = vida;
        this.mana = manaMax = mana;
        this.dano = dano;
        this.custoMana = custoMana;
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

        return danoTotal;
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

            return dano;
        } else {
            //Dano normal
            return receberDano(dano);
        }
    }

    public int atacar() {
        if (mana < manaMax) {
            recuperarMana();
        }
        int ataque = dano;
        if ((random.nextInt(101)) < critico) {
            ataque = ataque * 2;
            ultimoAtaqueCritico = true;
        } else {
            ultimoAtaqueCritico = false;
        }
        return ataque;
    }

    //Magias --------------------------------------------
    public List<Integer> usarMagia() {
        //Personagens sem ataque magico sempre retornam false
            return List.of(0, 0); 
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
        // Retorna uma lista com os status na ordem previsível:
        // [vida, vidaMax, mana, manaMax, defesa, critico, dano, manaRegen]
        return List.of(vida, vidaMax, mana, manaMax, defesa, critico, dano, manaRegen);
    }

    public String getNome() { return nome; }
    public boolean personagemVivo() { return vida > 0; }
    public int getDefesa() { return defesa; }
    public int getVida() { return vida; }
    public int getVidaMax() { return vidaMax; }
    public int getDano() { return dano; }
    public boolean ultimoAtaqueCritico() { return ultimoAtaqueCritico; }
}
