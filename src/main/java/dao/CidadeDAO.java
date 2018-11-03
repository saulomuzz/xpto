/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cidade;
import model.Estado;

/**
 *
 * @author SAULOD
 */
public class CidadeDAO {

    public boolean inserir(Cidade cidade) {
        String sql = "INSERT INTO tb_cidade(idtb_estado,"
                + " int_id_ibge, "
                + "var_desc, "
                + "bool_capital, "
                + "var_desc_no_accents, "
                + "var_desc_alternativa, "
                + "var_microregiao, "
                + "var_mesoregiao, "
                + "dec_lon, "
                + "dec_lat) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";

        Boolean retorno = false;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {
            pst.setInt(1, cidade.getIdtb_estado());
            pst.setInt(2, cidade.getInt_id_ibge());
            pst.setString(3, cidade.getVar_desc());
            pst.setBoolean(4, cidade.getBool_capital());
            pst.setString(5, cidade.getVar_desc_no_accents());
            pst.setString(6, cidade.getVar_desc_alternativa());
            pst.setString(7, cidade.getVar_microregiao());
            pst.setString(8, cidade.getVar_mesoregiao());
            pst.setDouble(9, cidade.getDec_lon());
            pst.setDouble(10, cidade.getDec_lat());
            if (pst.executeUpdate() > 0) {
                retorno = true;
                System.out.println("ADD - " + cidade.getVar_desc());
            }

        } catch (MySQLIntegrityConstraintViolationException ex) {
            System.out.println("Duplicado - " + ex.getMessage());
            cidade.setEx(ex);
            retorno = false;

        } catch (SQLException ex) {
            //Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            cidade.setEx(ex);
            retorno = false;
        }

        return retorno;

    }

    public int getQtdTotalRegistros() {
        String sql = "SELECT COUNT(*) AS registros FROM tb_cidade";
        int retorno = 0;
        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            ResultSet res = pst.executeQuery();

            if (res.next()) {

                retorno = res.getInt("registros");
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
        return retorno;
    }

    public List<Cidade> getCapitais() {
        String sql = "SELECT * FROM tb_cidade WHERE bool_capital = 1 ORDER BY var_desc";

        List<Cidade> retorno = new ArrayList<Cidade>();

        PreparedStatement pst = Conexao.getPreparedStatement(sql);

        try {

            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Cidade item = new Cidade();
                item.setIdtb_cidade(res.getInt("idtb_cidade"));
                item.setIdtb_estado(res.getInt("idtb_estado"));
                item.setInt_id_ibge(res.getInt("int_id_ibge"));
                item.setVar_desc("var_desc");
                item.setBool_capital(res.getBoolean("bool_capital"));
                item.setVar_desc_no_accents(res.getString("var_desc_no_accents"));
                item.setVar_desc_alternativa(res.getString("var_desc_alternativa"));
                item.setVar_microregiao(res.getString("var_microregiao"));
                item.setVar_mesoregiao(res.getString("var_mesoregiao"));
                item.setDec_lon(res.getDouble("dec_lon"));
                item.setDec_lat(res.getDouble("dec_lat"));

                retorno.add(item);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;
    }

    public Cidade getCidadeIBGE(int idIBGE) {
        String sql = "SELECT * FROM tb_cidade where  int_id_ibge = ?";
        Cidade retorno = null;

        PreparedStatement pst = Conexao.getPreparedStatement(sql);
        try {

            pst.setInt(1, idIBGE);
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                 retorno = new Cidade();
                retorno.setIdtb_cidade(res.getInt("idtb_cidade"));
                retorno.setIdtb_estado(res.getInt("idtb_estado"));
                retorno.setInt_id_ibge(res.getInt("int_id_ibge"));
                retorno.setVar_desc("var_desc");
                retorno.setBool_capital(res.getBoolean("bool_capital"));
                retorno.setVar_desc_no_accents(res.getString("var_desc_no_accents"));
                retorno.setVar_desc_alternativa(res.getString("var_desc_alternativa"));
                retorno.setVar_microregiao(res.getString("var_microregiao"));
                retorno.setVar_mesoregiao(res.getString("var_mesoregiao"));
                retorno.setDec_lon(res.getDouble("dec_lon"));
                retorno.setDec_lat(res.getDouble("dec_lat"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstadoDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return retorno;
    }
}
