/*
 * Se extende da classe Thread para permitir uma execução multitarefa
*/
public class Produtor extends Thread {
    private final Buffer buffer;
    private final int maxItens = 15;
    private final String nome;

    public Produtor(Buffer buffer, String nome) {
        this.buffer = buffer;
        this.nome = nome;
    }

    @Override
    public void run() {
        // Produzindo itens e inserindo no buffer
        try {
            for (int i = 1; i <= maxItens; i++) {
                int item = (int) (Math.random() * 100);
                Thread.sleep((long) (Math.random() * 100));
                buffer.produzir(item, nome);
            }
            String mensagem = nome + " - Finalizou produção de " + maxItens + " itens";
            buffer.registrarMensagem(mensagem);

        } catch (InterruptedException e) {
            System.err.println(nome + " foi interrompido");
            Thread.currentThread().interrupt();
        }
    }
}