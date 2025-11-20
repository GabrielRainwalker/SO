public class Consumidor extends Thread {
    private final Buffer buffer;
    private final int maxItens = 12;
    private final String nome;

    public Consumidor(Buffer buffer, String nome) {
        this.buffer = buffer;
        this.nome = nome;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= maxItens; i++) {
                int item = buffer.consumir(nome);
                Thread.sleep((long) (Math.random() * 150));
            }
            String mensagem = nome + " - Finalizou consumo de " + maxItens + " itens";
            buffer.registrarMensagem(mensagem);

        } catch (InterruptedException e) {
            System.err.println(nome + " foi interrompido");
            Thread.currentThread().interrupt();
        }
    }

}
