package battle;

import characters.Personagem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Equipe {

    private final List<Personagem> personagens;

    public Equipe() {
        this.personagens = new ArrayList<>();
    }

    public void adicionar(Personagem personagem) {
        this.personagens.add(personagem);
    }

    public List<Personagem> getVivos() {
        List<Personagem> vivos = new ArrayList<>();

        for (Personagem p : this.personagens) {
            if (p.personagemVivo()) {
                vivos.add(p);
            }
        }
        return vivos;
    }

    public Personagem escolherAlvoAleatorio() {
        List<Personagem> alvosDisponiveis = getVivos();

        if (alvosDisponiveis.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int indiceSorteado = random.nextInt(alvosDisponiveis.size());

        return alvosDisponiveis.get(indiceSorteado);
    }

    //Auxiliares ---------------------------------------
    public boolean temVivos() {
        return !getVivos().isEmpty();
    }

    // Desativado -----------------------------------
    public Personagem escolherAlvoJogador() {
        /*List<Personagem> alvosDisponiveis = getVivos();

        if (alvosDisponiveis.isEmpty()) {
            return null;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha um alvo:");

        // Mostra a lista numerada para o jogador
        for (int i = 0; i < alvosDisponiveis.size(); i++) {
            Personagem alvo = alvosDisponiveis.get(i);
            System.out.println((i + 1) + ". " + alvo.getNome());
        }

        int escolha = -1;
        while (escolha < 1 || escolha > alvosDisponiveis.size()) {
            System.out.print("Digite o número do alvo: ");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        return alvosDisponiveis.get(escolha - 1);*/
        return escolherAlvoAleatorio();
    }

}
