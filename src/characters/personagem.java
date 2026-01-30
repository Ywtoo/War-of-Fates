package characters;

import java.util.List;
import java.util.Random;

public abstract class Personagem {

    private final String nome;
    private int vida, defesa, critico, dano; 
    private int mana, manaRegen, custoMana;
    private final int vidaMax, manaMax;
    
    protected static final Random random = new Random();

    protected static int statusAleatorio(int valor) {
        return random.nextInt(valor * 2 + 1) - valor;
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
        int vidaAntes = vida;
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
            int vidaAntes = vida;
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
        if ((MathRandom(100)) < critico){
            dano = dano * 2;
        }
        //TODO: implementar logica de ataque com critico
        return dano;
    }

    //Magias --------------------------------------------
    public List<Integer> usarMagia() {
        boolean ok = gastarMana(custoMana);
        if (ok) {
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
        // Retorna uma lista com os status na ordem previsível:
        // [vida, vidaMax, mana, manaMax, defesa, critico, dano, manaRegen]
        return List.of(vida, vidaMax, mana, manaMax, defesa, critico, dano, manaRegen);
    }

    public String getNome() {
        return nome;
    }

    public boolean personagemVivo() {
        return vida > 0;
    }

    public int getDefesa() { return defesa; }
    public int getVida() { return vida; }
    public int getVidaMax() { return vidaMax; }
    public int getDano() { return dano; }
}
