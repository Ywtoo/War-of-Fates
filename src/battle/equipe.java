package battle;

import characters.Personagem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Equipe {

    private final List<List<Personagem>> personagens;
    private static final Random random = new Random();

    public Equipe() {
        this.personagens = new ArrayList<>();
    }

    public void adicionarClasse(List<Personagem> grupo, int indice) {
        if (indice < 0) {
            return;
        }

        List<Personagem> copia;
        if (grupo == null || grupo.isEmpty()) {
            copia = new ArrayList<>();
        } else {
            copia = new ArrayList<>(grupo);
        }

        while (this.personagens.size() <= indice) {
            this.personagens.add(new ArrayList<>());
        }
        this.personagens.set(indice, copia);
    }

    public List<List<Personagem>> getVivos() {
        List<List<Personagem>> vivos = new ArrayList<>();

        for (List<Personagem> linha : this.personagens) {
            if (linha == null) {
                continue;
            }
            List<Personagem> linhaVivos = new ArrayList<>();

            for (Personagem p : linha) {
                if (p != null && p.personagemVivo()) {
                    linhaVivos.add(p);
                }
            }

            if (!linhaVivos.isEmpty()) {
                vivos.add(linhaVivos);
            }
        }
        return vivos;
    }

    public Personagem escolherAlvoAleatorio(int linha) {
        List<List<Personagem>> linhasVivas = getVivos();
        if (linhasVivas == null || linha < 0 || linha >= linhasVivas.size()) {
            return null;
        }
        List<Personagem> alvosDisponiveis = linhasVivas.get(linha);
        if (alvosDisponiveis == null || alvosDisponiveis.isEmpty()) {
            return null;
        }
        int indiceSorteado = random.nextInt(alvosDisponiveis.size());
        return alvosDisponiveis.get(indiceSorteado);
    }

    //Auxiliares ---------------------------------------
    public boolean temVivos() {
        return !getVivos().isEmpty();
    }

    // Desativado -----------------------------------
    public Personagem escolherAlvoJogador(int linha) {
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
        return escolherAlvoAleatorio(linha);
    }

}
