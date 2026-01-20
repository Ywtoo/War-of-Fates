
import arena.Arena;
import battle.Equipe;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //TODO: UI temporaria para configurar a batalha-----------------
        System.out.println("\nModo de jogo:\n1 - PVP\n2 - PVE\n3 - EVE");
        int gameMode = scanner.nextInt();

        System.err.println("Numero de personagens na Equipe A:");
        int qtdEquipeA = scanner.nextInt();

        System.err.println("Numero de personagens na Equipe B:");
        int qtdEquipeB = scanner.nextInt();

        //Criar equipes-------------------------------------------
        Equipe equipeA = new Equipe();
        Equipe equipeB = new Equipe();

        adicionarPersonagens(equipeA, List.of(qtdEquipeA));
        adicionarPersonagens(equipeB, List.of(qtdEquipeB));

        //Iniciar arena(Inicio da batalha)-------------------------
        Arena arena = new Arena(gameMode);
        arena.iniciar(equipeA, equipeB);

        scanner.close();
    }

    public static void adicionarPersonagens(Equipe equipe, List<Integer> quantidades) {

        for (int i = 0; i < quantidades.size(); i++) {
            int quantidade = quantidades.get(i);

            for (int j = 0; j < quantidade; j++) {

                switch (i) {
                    // TODO: Futuras implementacoes de classes de personagens basta adicionar novos cases aqui
                    //case 0 -> equipe.adicionar(new Personagem("NULL" + (j + 1)));
                }
            }
        }
    }
}
