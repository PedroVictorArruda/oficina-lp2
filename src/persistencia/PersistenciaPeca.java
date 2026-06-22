package persistencia;

// ============================================================
//  RESPONSÁVEL: Integrante 3
//  TAREFA: implementar o método carregar() abaixo.
// ============================================================

import com.google.gson.*;
import model.Peca;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaPeca extends PersistenciaJson {

    private static final String ARQUIVO = "pecas.json";

    // ----- SALVAR (já implementado) -----
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

    // ----- CARREGAR (TODO: Integrante 3 deve implementar) -----
    public static List<Peca> carregar() {
        List<Peca> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            // TODO: leia os campos e crie a Peca
            // Campos: id, codigo, nome, preco, estoque
            //
            // Construtor:
            //   new Peca(id, codigo, nome, new BigDecimal(preco), estoque)
        }

        return lista;
    }
}
