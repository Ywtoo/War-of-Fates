package characters;

import java.util.List;
import java.util.Random;

public abstract class Personagem {

    protected final String NOME;
    protected int vida, defesa, critico, dano; 
    protected int mana, manaRegen, custoMana;
    protected final int VIDA_MAX, MANA_MAX;
    protected final List<Integer> PREF_DE_ATAQUE;
    
    protected static final Random random = new Random();
    private boolean ultimoAtaqueCritico = false;

    protected static int statusAleatorio(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public Personagem(String nome, int vida,  int dano, int defesa, int critico, int mana, int custoMana, int manaRegen, List<Integer> prefDeAtaque) {
        this.NOME = nome;
        this.vida = VIDA_MAX = vida;
        this.mana = MANA_MAX = mana;
        this.dano = dano;
        this.custoMana = custoMana;
        this.manaRegen = manaRegen;
        this.defesa = defesa;
        this.critico = critico;
        this.PREF_DE_ATAQUE = prefDeAtaque;
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
        if (mana < MANA_MAX) {
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
        if (mana > MANA_MAX) {
            mana = MANA_MAX;
        }
    }

    //Metodo de ataque --------------------------------------------
        //Escolhe linha alvo baseado nas preferencias do atacante caso tenha erro ele ataca linha da frente
    public int escolherIndiceLinhaAlvo(List<List<Personagem>> inimigo) {
        List<Integer> preferencias = this.getPREF_DE_ATAQUE();

        if (preferencias.size() >= 2) {
            int linhaAleatoria = preferencias.get(random.nextInt(preferencias.size()));
            if (linhaAleatoria < 0) {
                linhaAleatoria = inimigo.size() + linhaAleatoria;
            }
            return linhaAleatoria;
        } else {
            try {
                int indicePreferido = preferencias.get(0);
                return indicePreferido;
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
        }

    }

    //Status --------------------------------------------
    public List<Integer> mostrarStatus() {
        // Retorna uma lista com os status na ordem:
        // [vida, VIDA_MAX, mana, MANA_MAX, defesa, critico, dano, manaRegen]
        return List.of(vida, VIDA_MAX, mana, MANA_MAX, defesa, critico, dano, manaRegen);
    }

    public List<Integer> getPREF_DE_ATAQUE() { return PREF_DE_ATAQUE; }
    public String getNOME() { return NOME; }
    public String getNome() { return NOME; }
    public boolean personagemVivo() { return vida > 0; }
    public int getDefesa() { return defesa; }
    public int getVida() { return vida; }
    public int getVIDA_MAX() { return VIDA_MAX; }
    public int getDano() { return dano; }
    public boolean ultimoAtaqueCritico() { return ultimoAtaqueCritico; }
}
