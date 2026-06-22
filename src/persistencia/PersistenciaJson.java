package persistencia;

import com.google.gson.*;
import java.io.*;

/**
 * Utilitário base: fornece Gson e métodos de leitura/escrita de arquivo JSON.
 * Todas as classes de persistência herdam desta.
 */
public abstract class PersistenciaJson {

    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected static final String PASTA = "dados" + File.separator;

    static {
        new File(PASTA).mkdirs();
    }

    protected static void salvarArquivo(String nomeArquivo, JsonArray array) {
        try (Writer w = new FileWriter(PASTA + nomeArquivo)) {
            gson.toJson(array, w);
        } catch (IOException e) {
            System.err.println("[Persistência] Erro ao salvar " + nomeArquivo + ": " + e.getMessage());
        }
    }

    protected static JsonArray carregarArray(String nomeArquivo) {
        File f = new File(PASTA + nomeArquivo);
        if (!f.exists()) return new JsonArray();
        try (Reader r = new FileReader(f)) {
            JsonElement el = JsonParser.parseReader(r);
            return el.isJsonArray() ? el.getAsJsonArray() : new JsonArray();
        } catch (IOException e) {
            System.err.println("[Persistência] Erro ao carregar " + nomeArquivo + ": " + e.getMessage());
            return new JsonArray();
        }
    }
}
