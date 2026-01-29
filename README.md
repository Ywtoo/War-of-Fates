# War of Fates — v0.1

Resumo  
Pequeno jogo de batalha por turnos com visualização em Swing. Projeto POO (Programação Orientada a Objetos) desenvolvido como trabalho da disciplina — demonstra encapsulamento, herança, polimorfismo e separação de responsabilidades (lógica em `Arena`, UI via listeners).

Requisitos  
- Java JDK 11 ou superior instalado (obrigatório).  
- Windows (scripts fornecidos); funciona também em Linux/macOS com `javac`/`java`.

Como obter o código  
- Clonar o repositório:
```bash
git clone https://github.com/Ywtoo/War-of-Fates/
cd War-of-Fates
```

- Ou baixar o ZIP ("Code → Download ZIP" no GitHub) e extrair.

Build e execução (Windows)  
- Uso rápido:
```bat
compile.bat
run.bat
```
- Manual:
```bat
javac -d out src\characters\*.java src\battle\*.java src\arena\*.java src\ui\*.java Main.java
java -cp out Main
```

Como jogar (UI)  
- `Config Teams`: abre `TeamBuilder` (defina quantos personagens por time).  
- `Speed`: ajusta velocidade da simulação e animações.  
- `Start`: inicia a Arena; `Reset` reinicia.  
- `Status`: abre `StatusDialog` com vida/mana atual/max e stats.  
- Console lateral: mostra logs `[ARENA]` / `[ATAQUE]`.

Requisitos do trabalho (importante)  
- Repositório público (ex.: GitHub).  
- Cada membro deve contribuir com commits identificáveis — o histórico será auditado.  
- Projetos que não compilarem/executarem no ambiente de teste receberão nota zero.
