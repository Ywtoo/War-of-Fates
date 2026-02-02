package characters;

import java.util.Arrays;

public class Clerigo extends Personagem {

    public Clerigo(String nome) {
        super(
            nome,
            15 + statusAleatorio(-1, 3),     // Vida maior que Mago
            4 + statusAleatorio(-1, 2),      // Dano menor
            5 + statusAleatorio(0, 3),       // Defesa maior
            0,                               // Sem crítico inicial
            25 + statusAleatorio(5, 10),     // Mana alta
            4 + statusAleatorio(1, 3),       // Custo de mana para a cura
            4 + statusAleatorio(1, 4),       // Boa regeneração
            Arrays.asList(0, -1)            // Linhas de ataque (retaguarda)
        );
    }
    
    // Poder que cura até 10 aliados
    public void curar(Personagem[] aliados) {
        System.out.print(getNome() + " usou Cura em grupo: ");
        
        int curados = 0;
        for (int i = 0; i < aliados.length && curados < 10; i++) {
            if (aliados[i] != null) {
                System.out.print(aliados[i].getNome() + " ");
                curados++;
            }
        }
        
        System.out.println();
        System.out.println("Total de aliados curados: " + curados + "/10");
    }
}
