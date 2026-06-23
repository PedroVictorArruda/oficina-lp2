# Sistema de Gestão de Oficina Mecânica

Aplicação de console desenvolvida em Java para gerenciar o fluxo de trabalho de uma oficina mecânica, incluindo cadastro de clientes, mecânicos, veículos, peças, serviços e ordens de serviço com controle de estados.

Projeto prático da disciplina **Linguagem de Programação 2 (POO)** — Atividade Unidade 03.

---

## Integrantes

| Nome | Matrícula |
|------|-----------|
| José Carlos Oliveira de Lima | 20230055731 |
| Aramys Andreew Tavares de Paiva | 20220075051 |
| Raul Furtado Costa | 20240018179 |
| Pedro Victor Arruda Câmara Vilela Cid | 20220043414 |

---

## Requisitos

- **Java 17+** (recomendado: [Eclipse Temurin JDK](https://adoptium.net/))
- **Apache Maven 3.6+** (para compilar com a dependência Gson)

---

## Como executar

### Opção 1 — Maven (recomendado)

```bash
# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass="app.Main"
```

### Opção 2 — javac manual (Windows, com JDK já instalado)

```powershell
# Defina os caminhos
$javac = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot\bin\javac.exe"
$java  = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot\bin\java.exe"
$gson  = "$env:USERPROFILE\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar"

# Compilar (excluindo placeholders)
$files = Get-ChildItem -Recurse -Filter "*.java" src |
         Where-Object { $_.FullName -notmatch "\\model\\exception\\" } |
         ForEach-Object { $_.FullName }
& $javac --release 17 -cp $gson -d out $files

# Executar
& $java -cp "out;$gson" app.Main
```

> O Gson é baixado automaticamente pelo Maven na primeira execução do `mvn compile`.

---

## Funcionalidades

- Cadastro de **clientes**, **mecânicos**, **veículos** (Carro, Moto, Caminhão), **peças** e **serviços** (Preventivo, Corretivo)
- Abertura e controle de **Ordens de Serviço** com máquina de estados:
  `ABERTA → DIAGNOSTICO → APROVADA → EM_EXECUCAO → CONCLUIDA` (ou `CANCELADA`)
- **6 regras de negócio** implementadas (RN-01 a RN-06)
- **Persistência automática** em JSON na pasta `dados/` — carregada ao iniciar o programa
- Validação de CPF pelo algoritmo oficial dos 2 dígitos verificadores
- Menu interativo com 20 opções via terminal

---

## Estrutura do Projeto

```
src/
├── app/           → Main.java (menu interativo)
├── model/         → Entidades, enums e exceções personalizadas
├── service/       → OficinaService (regras de negócio)
├── persistencia/  → Leitura e gravação em JSON (Gson 2.10.1)
└── util/          → ConsoleUtils
dados/             → Arquivos JSON gerados em tempo de execução
```

---

## Diagrama de Classes

O arquivo `diagrama.puml` na raiz do projeto contém o diagrama UML completo com todas as classes, relações e multiplicidades.

Para visualizar: acesse [planttext.com](https://planttext.com) e cole o conteúdo do arquivo.
