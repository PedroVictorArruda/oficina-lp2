package persistencia;

// ============================================================
//  RESPONSÁVEL: Integrante 3
//  TAREFA: implementar o método carregar() abaixo.
//  Atenção: Veiculo é abstrato — salvar e carregar usa o campo
//  "tipo" (CARRO, MOTO ou CAMINHAO) para distinguir subclasses.
// ============================================================

import com.google.gson.*;
import model.*;
import service.OficinaService;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaVeiculo extends PersistenciaJson {

    private static final String ARQUIVO = "veiculos.json";

    // ----- SALVAR (já implementado) -----
    public static void salvar(List<Veiculo> veiculos) {
        JsonArray array = new JsonArray();
        for (Veiculo v : veiculos) {
            JsonObject obj = new JsonObject();
            // campos comuns
            obj.addProperty("id",     v.getId());
            obj.addProperty("placa",  v.getPlaca());
            obj.addProperty("modelo", v.getModelo());
            obj.addProperty("ano",    v.getAno());
            obj.addProperty("donoId", v.getDono().getId());

            // campos específicos de cada subtipo
            if (v instanceof Carro) {
                Carro c = (Carro) v;
                obj.addProperty("tipo",           "CARRO");
                obj.addProperty("nrPortas",        c.getNrPortas());
                obj.addProperty("tipoCombustivel", c.getTipoCombustivel().name());
            } else if (v instanceof Moto) {
                Moto m = (Moto) v;
                obj.addProperty("tipo",       "MOTO");
                obj.addProperty("cilindrada",  m.getCilindrada());
                obj.addProperty("tipoMoto",    m.getTipoMoto().name());
            } else if (v instanceof Caminhao) {
                Caminhao c = (Caminhao) v;
                obj.addProperty("tipo",     "CAMINHAO");
                obj.addProperty("capCarga",  c.getCapCarga());
                obj.addProperty("nrEixos",   c.getNrEixos());
            }
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    // ----- CARREGAR (TODO: Integrante 3 deve implementar) -----
    public static List<Veiculo> carregar(OficinaService service) {
        List<Veiculo> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            // TODO: leia os campos comuns e o "tipo", depois crie a subclasse correta.
            //
            // Campos comuns: id, placa, modelo, ano, donoId
            // Para recuperar o Cliente dono: service.buscarClientePorId(donoId)
            //
            // Exemplo de estrutura:
            //   String tipo = obj.get("tipo").getAsString();
            //   if (tipo.equals("CARRO")) {
            //       int nrPortas = obj.get("nrPortas").getAsInt();
            //       TipoCombustivel comb = TipoCombustivel.valueOf(obj.get("tipoCombustivel").getAsString());
            //       lista.add(new Carro(id, placa, modelo, ano, dono, nrPortas, comb));
            //   } else if (tipo.equals("MOTO")) { ... }
            //     else { /* CAMINHAO */ ... }
        }

        return lista;
    }
}
