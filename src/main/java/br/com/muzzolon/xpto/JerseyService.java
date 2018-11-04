package br.com.muzzolon.xpto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.cidadeController;
import config.Config;
import dao.CidadeDAO;
import dao.EstadoDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Cidade;
import model.Estado;
import model.Retorno;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/ws")
public class JerseyService {

    /**
     * 1 - Ler o arquivo CSV das cidades para a base de dados
     *
     * @return String-
     */
    @POST
    @Path("/cidade/uploadCSV")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadPdfFile(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
        Gson gson = new Gson();
        cidadeController c = new cidadeController();
        Retorno retorno = new Retorno();
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(new File(Config.getUPLOAD_PATH() + fileMetaData.getFileName()));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            return c.processaCSV(Config.getUPLOAD_PATH() + fileMetaData.getFileName());

        } catch (IOException e) {
            throw new WebApplicationException("Error while uploading file. Please try again !!");

        }

    }

    /**
     * 2 - Retornar somente as cidades que são capitais ordenadas por nome
     *
     * @return String-
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/capitais")
    public String getCapitais() {

        CidadeDAO cidadeDAO = new CidadeDAO();
        Gson gson = new Gson();

        return gson.toJson(cidadeDAO.getCapitais());
    }

    /**
     * 3 - Retornar o nome do estado com a maior e menor quantidade de cidades e
     * a quantidade de cidades;
     *
     * @return String-
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/maxmin")
    public String getMaxMin() {

        EstadoDAO estadoDAO = new EstadoDAO();

        return estadoDAO.getMaxMin();
    }

    /**
     * 4 - Retornar a quantidade de cidades por estado
     *
     * @return String-
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/qtdCidades")
    public String getQtdCidades() {

        EstadoDAO estadoDAO = new EstadoDAO();

        return estadoDAO.getQtdCidades();
    }

    /**
     * 5 - Obter os dados da cidade informando o id do IBGE;
     *
     * @param idibge
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/busca/{idibge}")
    public String getbuscaIBGE(@PathParam("idibge") int idIbge) {

        Cidade cidade = new Cidade();
        CidadeDAO cidadeDAO = new CidadeDAO();
        Gson gson = new Gson();

        cidade = cidadeDAO.getCidadeIBGE(idIbge);

        return gson.toJson(cidade);
    }

    /**
     * 6 - Retornar o nome das cidades baseado em um estado selecionado;
     *
     * @param uf
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/litarCidades/{uf}")
    public String getListarCidades(@PathParam("uf") String uf) {

        EstadoDAO estado = new EstadoDAO();

        return estado.getListarCidades(uf);
    }

    /**
     * 7 - Permitir adicionar uma nova Cidade;
     *
     *
     * @return String
     */
    @POST
    @Path("/cidade/insert")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertCidade(@FormDataParam("ibge_id") String ibge_id,
            @FormDataParam("uf") String uf,
            @FormDataParam("name") String name,
            @FormDataParam("capital") String capital,
            @FormDataParam("lon") String lon,
            @FormDataParam("lat") String lat,
            @FormDataParam("alternative_names") String alternative_names,
            @FormDataParam("microregion") String microregion,
            @FormDataParam("mesoregion") String mesoregion
    ) {

        EstadoDAO estadoDAO = new EstadoDAO();
        CidadeDAO cidadeDAO = new CidadeDAO();
        Cidade c = new Cidade();
        JsonObject auxJSON = new JsonObject();
        JsonArray retornoJSON = new JsonArray();
        Gson gson = new Gson();
        c.setInt_id_ibge(Integer.valueOf(ibge_id));
        c.setIdtb_estado(estadoDAO.getIdEstado(uf));
        c.setVar_desc(name);
        c.setBool_capital(Boolean.valueOf(capital));
        c.setDec_lon(Double.valueOf(lon));
        c.setDec_lat(Double.valueOf(lat));
        c.setVar_desc_no_accents(name);
        c.setVar_desc_alternativa(alternative_names);
        c.setVar_microregiao(microregion);
        c.setVar_mesoregiao(mesoregion);

        if (cidadeDAO.inserir(c)) {
            auxJSON.addProperty("regOK", 1);
            auxJSON.addProperty("msg", "Registro(s) inserido(s) com sucesso");
            retornoJSON.add(auxJSON);
        } else {

            auxJSON.addProperty("regNOK", 1);
            auxJSON.addProperty("msg", "Registro(s) com Erro");
            retornoJSON.add(auxJSON);
            auxJSON = new JsonObject();
            auxJSON.addProperty("reg", 1);
            auxJSON.addProperty("varDesc", c.getVar_desc());
            auxJSON.addProperty("int_id_ibge", c.getInt_id_ibge());
            auxJSON.addProperty("exception", c.getEx().getMessage());
            retornoJSON.add(auxJSON);
        }
        return gson.toJson(retornoJSON);
    }

    /**
     * 8 - Permitir deletar uma cidade;
     *
     *
     * @return String
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/delete/{ibge}")
    public String deletarCidade(@PathParam("ibge") String ibge
    ) {

        Cidade cidade = new Cidade();
        CidadeDAO cidadeDAO = new CidadeDAO();
        JsonObject auxJSON = new JsonObject();
        JsonArray retornoJSON = new JsonArray();
        Gson gson = new Gson();
        try {

            cidade.setInt_id_ibge(Integer.parseInt(ibge));
            if (cidadeDAO.excluir(cidade)) {
                auxJSON.addProperty("regOK", 1);
                auxJSON.addProperty("msg", "Registro(s) Deletado(s) com sucesso");
                retornoJSON.add(auxJSON);
            } else {

                auxJSON.addProperty("regNOK", 1);
                auxJSON.addProperty("msg", "Registro(s) com Erro");
                retornoJSON.add(auxJSON);
                auxJSON = new JsonObject();
                auxJSON.addProperty("reg", 1);
                auxJSON.addProperty("varDesc", cidade.getVar_desc());
                auxJSON.addProperty("int_id_ibge", cidade.getInt_id_ibge());
                auxJSON.addProperty("exception", cidade.getEx().getMessage());
                retornoJSON.add(auxJSON);
            }
        } catch (Exception e) {
            auxJSON.addProperty("regNOK", 1);
            auxJSON.addProperty("msg", e.getMessage());
            retornoJSON.add(auxJSON);

        }
        return gson.toJson(retornoJSON);
    }

    /**
     * 9 - Permitir selecionar uma coluna (do CSV) e através dela entrar com uma
     * string para filtrar. retornar assim todos os objetos que contenham tal
     * string;
     *
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/filtrar/{coluna}/{filtro}/{operador}")
    public String filtrar(@PathParam("coluna") String coluna,
            @PathParam("filtro") String filtro,
            @PathParam("operador") String operador) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        Gson gson = new Gson();

        return gson.toJson(cidadeDAO.getFiltro(coluna, filtro, operador));
    }

    /**
     * 10 - Retornar a quantidade de registro baseado em uma coluna. Não deve
     * contar itens iguais; string;
     *
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/qtdReg/{coluna}")
    public String getQtdReg(@PathParam("coluna") String coluna) {

        CidadeDAO cidadeDAO = new CidadeDAO();

        return cidadeDAO.getQtdReg(coluna);
    }

    /**
     * 11 - Retornar a quantidade de registros total;
     *
     *
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/qtdRegistros")
    public String getQtdRegistrosCidade() {
        Retorno retorno = new Retorno();
        CidadeDAO cidadeDAO = new CidadeDAO();
        Gson gson = new Gson();

        retorno.setTipo("OK!");
        retorno.setMensagem("Total de Registros: " + cidadeDAO.getQtdTotalRegistros());

        return gson.toJson(retorno);
    }

    /**
     * 12 - Dentre todas as cidades, obter as duas cidades mais distantes uma da
     * outra com base na localização (distância em KM em linha reta);
     *
     *
     * @return String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cidade/distancia")
    public String getDistanciaCidades() {

        CidadeDAO cidadeDAO = new CidadeDAO();

        return cidadeDAO.getDistancia();
    }

    /**
     * MÃ©todo que busca um estado por seu id
     *
     * @param id
     * @return um objeto JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("estado/buscaId/{id}")
    public String getEstadoId(@PathParam("id") int id
    ) {

        Estado estado = new Estado();
        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();
        estado.setIdTbEstado(id);
        estado = estadoDAO.buscarId(estado);

        return gson.toJson(estado);
    }

    /**
     * MÃ©todo que um estado por sua descricao
     *
     * @param desc
     * @return um objeto JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("estado/buscaDesc/{desc}")
    public String getEstadoDesc(@PathParam("desc") String desc
    ) {

        Estado estado = new Estado();
        List<Estado> listaEstado;

        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();
        estado.setVarDesc(desc);
        listaEstado = estadoDAO.buscarDesc(estado);

        return gson.toJson(listaEstado);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/listar")
    public String getEstado() {
        List<Estado> listaEstado;
        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();

        listaEstado = estadoDAO.listar();

        return gson.toJson(listaEstado);
    }

}
