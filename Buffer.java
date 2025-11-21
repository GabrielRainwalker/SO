import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int[] buffer;
    private final int capacidade = 7;
    private int contador = 0;
    private int posicaoEntrada = 0;
    private int posicaoSaida = 0;


    /*  Ele garante que apenas uma thread por vez acesse a região critica
     *  Atua como um mecanismo de exclusão mutua para proteger o acesso ao buffer compartilhado.
    */ 

    private final ReentrantLock mutex;

    /*
     *  Semáforo que controla os espaços vazios
     *  "empty" é inicializado com o valor da capacidade do buffer
     *  Cada vez que um produtor insere um item no buffer, ele decrementa "empty"
     *  Cada vez que um consumidor remove um item do buffer, ele incrementa "empty"
     *  Isso garante que os produtores só possam inserir itens quando houver espaço
    */

    private final Semaphore empty;

    /* "full" é inicializado com 0
     *  Cada vez que um produtor insere um item no buffer, ele incrementa "full"
     *  Cada vez que um consumidor remove um item do buffer, ele decrementa "full
     * Isso garante que os consumidores só possam remover itens quando houver itens
    */
    private final Semaphore full;

    private PrintWriter logWriter;

    public Buffer() {
        this.buffer = new int[capacidade];
        this.mutex = new ReentrantLock();
        this.empty = new Semaphore(capacidade);
        this.full = new Semaphore(0);

        try {
            this.logWriter = new PrintWriter(new FileWriter("log_buffer.txt", false));
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    public void produzir(int item, String nomeProdutor) throws InterruptedException {
        /*
         *  O produtor tenta adquirir o semáforo "empty" antes de inserir um item no buffer
         *  Se "empty" for maior que 0, o produtor pode e inserir um item
         *  Se não, ele fica bloqueado até que um consumidor remova o item e libere espaço
        */
        empty.acquire();
        mutex.lock(); // Entrando na seção crítica e bloquei qualquer outra thread evitando a condição de corrida
        try {
            buffer[posicaoEntrada] = item;
            posicaoEntrada = (posicaoEntrada + 1) % capacidade;
            contador++;

            int espacosDisponiveis = capacidade - contador;
            String mensagem = nomeProdutor + " - Inserido item " + item +
                    " no buffer - espaços disponíveis: " + espacosDisponiveis;

            System.out.println(mensagem);

            // Log da produção
            if (logWriter != null) {
                logWriter.println(mensagem);
                logWriter.flush();
            }
        } finally {
            mutex.unlock(); // Saindo da seção crítica e desbloqueando para outras threads isso evita deadlock
        }
        full.release();
    }

    public int consumir(String nomeConsumidor) throws InterruptedException {
        /*
         * O consumidor tenta incrementar o "full" antes de remover um item do buffer
         *  Se "full" for maior que 0, o consumidor pode remover um item
         *  Se não, ele fica bloqueado até que um produtor insira um item e incrementar a variavel "full"
        */
        full.acquire();
        mutex.lock(); // Entrando na seção crítica
        int item;
        try {
            item = buffer[posicaoSaida];
            posicaoSaida = (posicaoSaida + 1) % capacidade;
            contador--;

            int espacosDisponiveis = capacidade - contador;
            String mensagem = nomeConsumidor + " - Consumido item " + item +
                    " do buffer - espaços disponíveis: " + espacosDisponiveis;

            System.out.println(mensagem);

            if (logWriter != null) {
                logWriter.println(mensagem);
                logWriter.flush();
            }
        } finally {
            mutex.unlock(); // Saindo da seção crítica
        }
        empty.release();
        return item;
    }

    /*
     * Funções para fechar e registrar Logs
    */
    public void fecharLog() {
        if (logWriter != null) {
            logWriter.flush();
            logWriter.close();
        }
    }

    public void registrarMensagem(String mensagem) {
        mutex.lock();
        try {
            System.out.println(mensagem);
            if (logWriter != null) {
                logWriter.println(mensagem);
                logWriter.flush();
            }
        } finally {
            mutex.unlock();
        }
    }

}