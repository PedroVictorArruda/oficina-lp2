package persistencia;

import com.google.gson.*;
import model.*;
import service.OficinaService;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaVeiculo extends PersistenciaJson {

    private static final String ARQUIVO = "veiculos.json";

    public static void salvar(List<Veiculo> veiculos) {
        JsonArray array = new JsonArray();
        for (Veiculo v : veiculos) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",     v.getId());
            obj.addProperty("placa",  v.getPlaca());
            obj.addProperty("modelo", v.getModelo());
            obj.addProperty("ano",    v.getAno());
            obj.addProperty("donoId", v.getDono().getId());

            if (v instanceof Carro) {
                Carro c = (Carro) v;
                obj.addProperty("tipo",           "CARRO");
                obj.addProperty("nrPortas",        c.getNrPortas());
                obj.addProperty("tipoCombustivel", c.getTipoCombustivel().name());
            } else if (v instanceof Moto) {
                Moto m = (Moto) v;
                obj.addProperty("tipo",      "MOTO");
                obj.addProperty("cilindrada", m.getCilindrada());
                obj.addProperty("tipoMoto",   m.getTipoMoto().name());
            } else if (v instanceof Caminhao) {
                Caminhao c = (Caminhao) v;
                obj.addProperty("tipo",    "CAMINHAO");
                obj.addProperty("capCarga", c.getCapCarga());
                obj.addProperty("nrEixos",  c.getNrEixos());
            }
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    public static List<Veiculo> carregar(OficinaService service) {
        List<Veiculo> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            int id        = obj.get("id").getAsInt();
            String placa  = obj.get("placa").getAsString();
            String modelo = obj.get("modelo").getAsString();
            int ano       = obj.get("ano").getAsInt();
            int donoId    = obj.get("donoId").getAsInt();
            Cliente dono  = service.buscarClientePorId(donoId);
            String tipo   = obj.get("tipo").getAsString();

            if (tipo.equals("CARRO")) {
                int nrPortas         = obj.get("nrPortas").getAsInt();
                TipoCombustivel comb = TipoCombustivel.valueOf(obj.get("tipoCombustivel").getAsString());
                lista.add(new Carro(id, placa, modelo, ano, dono, nrPortas, comb));
            } else if (tipo.equals("MOTO")) {
                int cilindrada    = obj.get("cilindrada").getAsInt();
                TipoMoto tipoMoto = TipoMoto.valueOf(obj.get("tipoMoto").getAsString());
                lista.add(new Moto(id, placa, modelo, ano, dono, cilindrada, tipoMoto));
            } else {
                double capCarga = obj.get("capCarga").getAsDouble();
                int nrEixos     = obj.get("nrEixos").getAsInt();
                lista.add(new Caminhao(id, placa, modelo, ano, dono, capCarga, nrEixos));
            }
        }

        return lista;
    }
}
