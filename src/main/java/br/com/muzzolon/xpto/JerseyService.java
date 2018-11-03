package br.com.muzzolon.xpto;

import com.google.gson.Gson;
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

    /**
     * 2 - Retornar somente as cidades que s�o capitais ordenadas por nome
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
   * 6 - Retornar o nome das cidades baseado em um estado selecionado;;
   * @param uf
   * @return String
   */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/litarCidades/{uf}")
    public String getbuscaIBGE(@PathParam("uf") String uf) {

        EstadoDAO estado = new EstadoDAO();
       

        return estado.getListarCidades(uf);
    }
    
    
    
    
    /**
     * Método que busca um estado por sua sigla
     *
     * @param uf
     * @return um objeto JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado/buscaUF/{uf}")
    public String getEstadoUF(@PathParam("uf") String uf) {

        Estado estado = new Estado();
        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();
        estado.setVarUf(uf);
        estado = estadoDAO.buscarUF(estado);

        return gson.toJson(estado);
    }

    /**
     * Método que busca um estado por seu id
     *
     * @param id
     * @return um objeto JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("estado/buscaId/{id}")
    public String getEstadoId(@PathParam("id") int id) {

        Estado estado = new Estado();
        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();
        estado.setIdTbEstado(id);
        estado = estadoDAO.buscarId(estado);

        return gson.toJson(estado);
    }

    /**
     * Método que um estado por sua descricao
     *
     * @param desc
     * @return um objeto JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("estado/buscaDesc/{desc}")
    public String getEstadoDesc(@PathParam("desc") String desc) {

        Estado estado = new Estado();
        List<Estado> listaEstado;

        EstadoDAO estadoDAO = new EstadoDAO();
        Gson gson = new Gson();
        estado.setVarDesc(desc);
        listaEstado = estadoDAO.buscarDesc(estado);

        return gson.toJson(listaEstado);

    }

    /**
     * Método que retorna a quantidade de registos na tabela cidade
     *
     * @return
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

    @POST
    @Path("/cidade/uploadCSV")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
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
}
