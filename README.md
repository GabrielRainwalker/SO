# Sistema de Concorrência: Produtor-Consumidor em Java ☕

Um projeto focado em Sistemas Operacionais e processamento paralelo, implementando a clássica arquitetura de "Oferta e Procura" (Produtor-Consumidor) utilizando `ReentrantLock` e Semáforos nativos do Java.

## 📌 Sobre o Projeto

Este projeto foi desenvolvido para demonstrar o controle de concorrência e a sincronização de múltiplas *threads* em um ambiente compartilhado (Buffer). O objetivo é garantir que os Produtores não insiram dados em um buffer cheio e que os Consumidores não tentem ler dados de um buffer vazio, evitando problemas de *Deadlock* e *Race Conditions*.

### Parâmetros do Sistema:
- **Capacidade do Buffer:** 7 posições
- **Carga de Produção:** Produtores geram até 15 itens cada
- **Carga de Consumo:** Consumidores processam até 12 itens cada

## ⚙️ Tecnologias e Conceitos Aplicados

- **Linguagem:** Java
- **Multithreading:** Gerenciamento do ciclo de vida das Threads.
- **Sincronização:** 
  - `java.util.concurrent.Semaphore` (Controle de permissões do Buffer).
  - `java.util.concurrent.locks.ReentrantLock` (Garantia de exclusão mútua / Mutex durante o acesso ao recurso compartilhado).
- **Tratamento de Exceções:** Gerenciamento de interrupções de threads (`InterruptedException`).

## 🚀 Como Executar

Certifique-se de ter o [Java JDK](https://www.oracle.com/java/technologies/downloads/) instalado na sua máquina.

1. Clone este repositório:
```bash
git clone https://github.com/SEU-USUARIO/SO.git
