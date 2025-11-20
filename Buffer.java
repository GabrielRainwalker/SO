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
    private final ReentrantLock mutex;
    private final Semaphore empty;
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
        empty.acquire();
        mutex.lock();
        try {
            buffer[posicaoEntrada] = item;
            posicaoEntrada = (posicaoEntrada + 1) % capacidade;
            contador++;

            int espacosDisponiveis = capacidade - contador;
            String mensagem = nomeProdutor + " - Inserido item " + item +
                    " no buffer - espaços disponíveis: " + espacosDisponiveis;

            System.out.println(mensagem);

            if (logWriter != null) {
                logWriter.println(mensagem);
                logWriter.flush();
            }
        } finally {
            mutex.unlock();
        }
        full.release();
    }

    public int consumir(String nomeConsumidor) throws InterruptedException {
        full.acquire();
        mutex.lock();
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
            mutex.unlock();
        }
        empty.release();
        return item;
    }

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