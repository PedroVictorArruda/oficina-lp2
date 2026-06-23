package persistencia;

import com.google.gson.*;
import model.*;
import service.OficinaService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaServico extends PersistenciaJson {

    private static final String ARQUIVO = "servicos.json";

    public static void salvar(List<Servico> servicos) {
        JsonArray array = new JsonArray();
        for (Servico s : servicos) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",        s.getId());
            obj.addProperty("descricao", s.getDescricao());
            obj.addProperty("precoBase", s.getPrecoBase().toPlainString());

            // salva IDs das peças associadas ao serviço
            JsonArray pecaIds = new JsonArray();
            for (Peca p : s.getPecas()) pecaIds.add(p.getId());
            obj.add("pecaIds", pecaIds);

            if (s instanceof ServicoPreventivo) {
                ServicoPreventivo sp = (ServicoPreventivo) s;
                obj.addProperty("tipo",            "PREVENTIVO");
                obj.addProperty("intervaloKm",     sp.getIntervaloKm());
                obj.addProperty("ultimaRevisaoKm", sp.getUltimaRevisaoKm());
                obj.addProperty("kmAtual",         sp.getKmAtual());
            } else if (s instanceof ServicoCorretivo) {
                ServicoCorretivo sc = (ServicoCorretivo) s;
                obj.addProperty("tipo",         "CORRETIVO");
                obj.addProperty("nivelUrgencia", sc.getNivelUrgencia().name());
                obj.addProperty("categoria",     sc.getCategoria().name());
                if (sc.getPecaSubstituida() != null) {
                    obj.addProperty("pecaSubstituidaId", sc.getPecaSubstituida().getId());
                }
            }
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    public static List<Servico> carregar(OficinaService service) {
        List<Servico> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            ServicoPreventivo sp = new ServicoPreventivo(id, descricao, new BigDecimal(precoBase),
                                   intervaloKm, ultimaRevisaoKm, kmAtual);
            ServicoCorretivo sc = new ServicoCorretivo(id, descricao, new BigDecimal(precoBase),
                                     NivelUrgencia.valueOf(...), Especialidade.valueOf(...));
            if (obj.has("pecaSubstituidaId")) {
                sc.setPecaSubstituida(service.buscarPecaPorId(obj.get("pecaSubstituidaId").getAsInt()));
            }
            
            for (JsonElement pEl : obj.get("pecaIds").getAsJsonArray()) {
                sc.adicionarPeca(service.buscarPecaPorId(pEl.getAsInt()));
            }
        }

        return lista;
    }
}
