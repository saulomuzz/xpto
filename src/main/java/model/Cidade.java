/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Global;

/**
 *
 * @author SAULOD
 */
public class Cidade {

    private int idtb_cidade;
    private int idtb_estado;
    private int int_id_ibge;
    private String var_desc;
    private Boolean bool_capital;
    private String var_desc_no_accents;
    private String var_desc_alternativa;
    private String var_microregiao;
    private String var_mesoregiao;
    private Double dec_lon;
    private Double dec_lat;
    private Exception ex;

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
    
    
    public int getIdtb_cidade() {
        return idtb_cidade;
    }

    public void setIdtb_cidade(int idtb_cidade) {
        this.idtb_cidade = idtb_cidade;
    }

    public int getIdtb_estado() {
        return idtb_estado;
    }

    public void setIdtb_estado(int idtb_estado) {
        this.idtb_estado = idtb_estado;
    }

    public int getInt_id_ibge() {
        return int_id_ibge;
    }

    public void setInt_id_ibge(int int_id_ibge) {
        this.int_id_ibge = int_id_ibge;
    }

    public String getVar_desc() {
        return var_desc;
    }

    public void setVar_desc(String var_desc) {
        this.var_desc = var_desc;
    }

    public Boolean getBool_capital() {
        return bool_capital;
    }

    public void setBool_capital(Boolean bool_capital) {
        this.bool_capital = bool_capital;
    }

    public String getVar_desc_no_accents() {
        return var_desc_no_accents;
    }

    public void setVar_desc_no_accents(String var_desc_no_accents) {
        this.var_desc_no_accents = Global.removerCaracteresEspeciais(var_desc_no_accents);
    }

    public String getVar_desc_alternativa() {
        return var_desc_alternativa;
    }

    public void setVar_desc_alternativa(String var_desc_alternativa) {
        this.var_desc_alternativa = var_desc_alternativa;
    }

    public String getVar_microregiao() {
        return var_microregiao;
    }

    public void setVar_microregiao(String var_microregiao) {
        this.var_microregiao = var_microregiao;
    }

    public String getVar_mesoregiao() {
        return var_mesoregiao;
    }

    public void setVar_mesoregiao(String var_mesoregiao) {
        this.var_mesoregiao = var_mesoregiao;
    }

    public Double getDec_lon() {
        return dec_lon;
    }

    public void setDec_lon(Double dec_lon) {
        this.dec_lon = dec_lon;
    }

    public Double getDec_lat() {
        return dec_lat;
    }

    public void setDec_lat(Double dec_lat) {
        this.dec_lat = dec_lat;
    }
}
