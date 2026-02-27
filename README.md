# ⚔️ War of Fates

<p align="center">
  <img src="https://img.shields.io/badge/STATUS-STABLE-22c55e?style=for-the-badge&labelColor=111827" />
  <img src="https://img.shields.io/badge/JAVA-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white&labelColor=111827" />
  <img src="https://img.shields.io/badge/PARADIGM-OOP-3b82f6?style=for-the-badge&labelColor=111827" />
  <img src="https://img.shields.io/badge/UI-SWING-6b7280?style=for-the-badge&labelColor=111827" />
  <img src="https://img.shields.io/badge/ARCHITECTURE-LAYERED-6366f1?style=for-the-badge&labelColor=111827" />
</p>

## 📖 Sobre o Projeto

**War of Fates** é um jogo de batalha por turnos com interface gráfica em Swing, desenvolvido como projeto acadêmico para demonstrar conceitos de Programação Orientada a Objetos (POO).

O projeto evidencia:

- Encapsulamento
- Herança
- Polimorfismo
- Separação de responsabilidades
- Organização em camadas (lógica vs interface)

A lógica principal da batalha é isolada da interface gráfica, garantindo melhor estrutura e manutenção.

---

## 🧠 Destaques Técnicos

- Sistema de batalha por prioridade de linha (ex: personagens frontais absorvem dano antes da linha traseira)
- Separação clara entre lógica da Arena e camada de UI
- Refatoração de módulos para melhoria de estabilidade
- Organização modular por pacotes

---

## 🛠 Tecnologias Utilizadas

- Java 11+
- Java Swing (UI)
- Programação Orientada a Objetos
- Estrutura modular por pacotes

---

## 🏗 Estrutura do Projeto

```
src/
 ├── characters/   # Classes base e especializações de personagens
 ├── battle/       # Lógica de combate
 ├── arena/        # Controle central da simulação
 ├── ui/           # Interface gráfica (Swing)
 └── Main.java     # Ponto de entrada da aplicação
```

---

## 📦 Requisitos

- Java JDK 11 ou superior
- Windows (scripts .bat disponíveis)
- Linux/macOS também suportado via terminal

---

## 🚀 Como Executar

### 🔹 Windows (rápido)

```bash
compile.bat
run.bat
```

### 🔹 Manual (qualquer sistema)

```bash
javac -d out src/characters/*.java src/battle/*.java src/arena/*.java src/ui/*.java src/Main.java
java -cp out Main
```

---

## 🎮 Como Jogar

- **Config Teams** → Defina número de personagens por time
- **Speed** → Ajusta velocidade da simulação
- **Start** → Inicia batalha
- **Reset** → Reinicia arena
- **Status** → Mostra vida/mana e atributos
- **Console lateral** → Logs da batalha

---

## 🎥 Apresentação

📺 Vídeo demonstrativo:  
https://youtu.be/yB04CjubFtw

---

## 📌 Objetivo Acadêmico

Este projeto foi desenvolvido como atividade avaliativa da disciplina de Programação Orientada a Objetos.

Critérios atendidos:

- ✔ Repositório público
- ✔ Commits rastreáveis
- ✔ Projeto compilável e executável
- ✔ Aplicação prática de conceitos de POO

---

## 📄 Histórico de Prompts

Os prompts utilizados durante o desenvolvimento estão documentados em:

`PROMPTS_USADOS.md`

---

---

## 👥 Contribuidores

Projeto desenvolvido em equipe:

- **Ywtoo (Gabriel Nascimento)**  
  - Implementação principal da interface Swing  
  - Sistema de turnos  
  - Sistema de prioridade por linha (Frontline / Backline targeting)  
  - Refatoração e correção de erros  
  - Expansão e aprimoramento dos personagens  

- **Margheo (Aurora)**  
  - Implementação da lógica dos personagens  

- **dsanttos12**  
  - Implementação inicial do sistema de equipes  
