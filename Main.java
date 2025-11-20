public static void main(String[] args) {
    System.out.println("=== PROBLEMA PRODUTOR-CONSUMIDOR ===");
    System.out.println("Buffer com capacidade: 7 posições");
    System.out.println("Produtores: produzem até 15 itens cada");
    System.out.println("Consumidores: consomem até 12 itens cada");
    System.out.println("=====================================\n");
    
    Buffer buffer = new Buffer();
    
    Produtor produtor1 = new Produtor(buffer, "Produtor-1");
    Produtor produtor2 = new Produtor(buffer, "Produtor-2");
    Consumidor consumidor1 = new Consumidor(buffer, "Consumidor-1");
    Consumidor consumidor2 = new Consumidor(buffer, "Consumidor-2");
    
    produtor1.start();
    produtor2.start();
    consumidor1.start();
    consumidor2.start();
    
    try {
        produtor1.join();
        produtor2.join();
        consumidor1.join();
        consumidor2.join();
        
        System.out.println("\n=====================================");
        System.out.println("Todas as threads finalizaram!");
        
        buffer.fecharLog();
        
        java.io.File arquivo = new java.io.File("log_buffer.txt");
        if (arquivo.exists()) {
            long tamanho = arquivo.length();
            System.out.println("Log salvo em: log_buffer.txt");
            System.out.println("Tamanho do arquivo: " + tamanho + " bytes");
        } else {
            System.out.println("ERRO: Arquivo não foi criado!");
        }
        
        System.out.println("=====================================");
    } catch (InterruptedException e) {
        System.err.println("Thread principal foi interrompida");
        Thread.currentThread().interrupt();
    }
}