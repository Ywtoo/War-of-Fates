package arena;

import battle.Equipe;
import characters.Personagem;

public interface ArenaListener {
    void onAttack(Personagem atacante, Personagem alvo, int dano);

    // Notifica ataque crítico (opcional)
    default void onCriticalAttack(Personagem atacante, Personagem alvo, int dano) {}

    // Notifica fim da batalha (pode ser null em caso de empate)
    default void onFinish(Equipe ganhador) {}

    // Notifica inicio de round para a UI apresentar mensagem
    default void onRoundStart(int round, int gameMode) {}

    // Notifica quando o delay de ataques é alterado
    default void onDelayChanged(int delayMs) {}
}
