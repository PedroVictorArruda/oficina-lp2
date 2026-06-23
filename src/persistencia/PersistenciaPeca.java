package persistencia;


import com.google.gson.*;
import model.Peca;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaPeca extends PersistenciaJson {

    private static final String ARQUIVO = "pecas.json";

    public static void salvar(List<Peca> pecas) {
        JsonArray array = new JsonArray();
        for (Peca p : pecas) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",      p.getId());
            obj.addProperty("codigo",  p.getCodigo());
            obj.addProperty("nome",    p.getNome());
            obj.addProperty("preco",   p.getPreco().toPlainString());
            obj.addProperty("estoque", p.getEstoque());
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    public static List<Peca> carregar() {
        List<Peca> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            int id = obj.get("id").getAsInt();
            String codigo = obj.get("codigo").getAsString();
            String nome = obj.get("nome").getAsString();
            String preco = obj.get("preco").getAsString();
            int estoque = obj.get("estoque").getAsInt();

            Peca p = new Peca(id, codigo, nome, new BigDecimal(preco), estoque);

            lista.add(p);
        }

        return lista;
    }
}
