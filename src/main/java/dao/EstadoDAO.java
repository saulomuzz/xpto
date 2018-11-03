/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Estado;

/**
 *
 * @author SAULOD
 */
public class EstadoDAO {

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
     */
    public EstadoDAO() {

    }

    public boolean inserir(Estado estado) {
        String sql = "INSERT INTO tb_estado(var_desc, var_uf) VALUES (?,?)";

        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
            pst.setString(1, estado.getVarDesc());
            pst.setString(2, estado.getVarUf());

            if (pst.executeUpdate() > 0) {
                retorno = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }

        return retorno;

    }

    public boolean atualizar(Estado estado) {
        String sql = "UPDATE estado set var_desc=?,var_uf=? where idtb_estado=?";
        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setString(1, estado.getVarDesc());
            pst.setString(2, estado.getVarUf());
            pst.setInt(3, estado.getIdTbEstado());

            if (pst.executeUpdate() > 0) {
                retorno = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }

        return retorno;

    }

    public boolean excluir(Estado estado) {
        String sql = "DELETE FROM tb_estado where idtb_estado=?";
        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setInt(1, estado.getIdTbEstado());
            if (pst.executeUpdate() > 0) {
                retorno = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }

        return retorno;

    }

    public List<Estado> listar() {
        String sql = "SELECT * FROM tb_estado";
        List<Estado> retorno = new ArrayList<Estado>();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Estado item = new Estado();
                item.setIdTbEstado(res.getInt("idtb_estado"));
                item.setVarDesc(res.getString("var_desc"));
                item.setVarUf(res.getString("var_uf"));

                retorno.add(item);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;

    }

    public String getQtdCidades() {
        String sql = "SELECT e.var_desc, COUNT(c.var_desc) AS cidades FROM tb_estado e, tb_cidade c WHERE e.idtb_estado = c.idtb_estado GROUP BY e.var_desc ORDER BY cidades DESC";
        Gson gson = new Gson();
        JsonArray retorno = new JsonArray();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            ResultSet res = pst.executeQuery();
            while (res.next()) {
                JsonObject aux = new JsonObject();

                aux.addProperty("var_desc", res.getString("var_desc"));
                aux.addProperty("qtd", res.getString("cidades"));
                retorno.add(aux);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return gson.toJson(retorno);

    }

    public String getMaxMin() {
        String sql = "SELECT e.idtb_estado, e.var_desc, COUNT(c.var_desc) AS cidades FROM tb_estado e, tb_cidade c WHERE e.idtb_estado = c.idtb_estado GROUP BY e.var_desc ORDER BY cidades ASC";
        Gson gson = new Gson();
        int min = 999;
        int max = 0;
        String descEstadoMin = "";
        String descEstadoMax = "";
        int estadoMin = 0;
        int estadoMax = 0;
        JsonArray retorno = new JsonArray();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            ResultSet res = pst.executeQuery();
            while (res.next()) {
                if (Integer.valueOf(res.getString("cidades")) < min) {
                    estadoMin = Integer.valueOf(res.getString("idtb_estado"));
                    descEstadoMin = res.getString("var_desc");
                    min = Integer.valueOf(res.getString("cidades"));
                }
                if (Integer.valueOf(res.getString("cidades")) > max) {
                    estadoMax = Integer.valueOf(res.getString("idtb_estado"));
                    descEstadoMax = res.getString("var_desc");
                    max = Integer.valueOf(res.getString("cidades"));
                }

            }
            JsonObject aux = new JsonObject();
            aux.addProperty("var_desc", descEstadoMax);
            aux.addProperty("qtd", max);
            retorno.add(aux);
            aux = new JsonObject();
            aux.addProperty("var_desc", descEstadoMin);
            aux.addProperty("qtd", min);
            retorno.add(aux);

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return gson.toJson(retorno);

    }

    public Estado buscarUF(Estado estado) {
        String sql = "SELECT * FROM tb_estado where var_uf like ?";
        Estado retorno = null;

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setString(1, estado.getVarUf());
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                retorno = new Estado();
                retorno.setIdTbEstado(res.getInt("idtb_estado"));
                retorno.setVarDesc(res.getString("var_desc"));
                retorno.setVarUf(res.getString("var_uf"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;

    }

    public String getListarCidades(String uf) {
        String sql = "SELECT e.var_uf, e.var_desc AS descEstado,c.var_desc AS descCidade\n"
                + "FROM tb_estado e, tb_cidade c \n"
                + "WHERE e.idtb_estado = c.idtb_estado AND e.var_uf like ?\n"
                + "ORDER BY c.var_desc ASC";

        JsonArray retorno = new JsonArray();
        Gson gson = new Gson();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
            pst.setString(1, uf);
            ResultSet res = pst.executeQuery();

            while (res.next()) {
                JsonObject aux = new JsonObject();
                aux.addProperty("uf", res.getString("var_uf"));
                aux.addProperty("var_desc_uf", res.getString("descEstado"));
                aux.addProperty("var_desc", res.getString("descCidade"));
                retorno.add(aux);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
        return gson.toJson(retorno);
    }

    public List<Estado> buscarDesc(Estado estado) {
        String sql = "SELECT * FROM tb_estado where var_desc like ?";

        List<Estado> retorno = new ArrayList<Estado>();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);

        try {
            pst.setString(1, '%' + estado.getVarDesc() + '%');
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Estado item = new Estado();
                item.setIdTbEstado(res.getInt("idtb_estado"));
                item.setVarDesc(res.getString("var_desc"));
                item.setVarUf(res.getString("var_uf"));

                retorno.add(item);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;

    }

    public Estado buscarId(Estado estado) {
        String sql = "SELECT * FROM tb_estado where idtb_estado = ?";
        Estado retorno = null;

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setInt(1, estado.getIdTbEstado());
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                retorno = new Estado();
                retorno.setIdTbEstado(res.getInt("idtb_estado"));
                retorno.setVarDesc(res.getString("var_desc"));
                retorno.setVarUf(res.getString("var_uf"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;

    }

    public int getIdEstado(String uf) {

        String sql = "SELECT * FROM tb_estado where var_uf like ?";
        int cont = 0;
        int idEstado = 0;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setString(1, uf);
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                cont++;
                idEstado = res.getInt("idtb_estado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
        if (cont == 0) {
            Estado estado = new Estado();
            estado.setVarDesc(uf);
            estado.setVarUf(uf);
            inserir(estado);
            idEstado = getIdEstado(uf);
        }
        return idEstado;
    }

}
