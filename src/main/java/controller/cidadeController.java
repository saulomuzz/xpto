/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.CidadeDAO;
import dao.EstadoDAO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Cidade;

/**
 *
 * @author SAULOD
 */
public class cidadeController {

    public String processaCSV(String dir) {
        List<Cidade> cidadeOK = new ArrayList<Cidade>();
        List<Cidade> cidadeErro = new ArrayList<Cidade>();
        JsonArray retornoJSON = new JsonArray();
        Gson gson = new Gson();
        JsonObject auxJSON = new JsonObject();
        BufferedReader br = null;
        String linha = "";
        String csvDivisor = ",";
        int cont = 0;
        try {

            br = new BufferedReader(new FileReader(dir));
            while ((linha = br.readLine()) != null) {
                if (cont != 0) {//ignorar cabeçalhos
                    String[] cidade = linha.split(csvDivisor);
                    Cidade c = new Cidade();
                    EstadoDAO estado = new EstadoDAO();
                    CidadeDAO cidadeDAO = new CidadeDAO();
                    c.setInt_id_ibge(Integer.valueOf(cidade[cidade.length - 10]));
                    c.setIdtb_estado(estado.getIdEstado(cidade[cidade.length - 9]));
                    c.setVar_desc(cidade[cidade.length - 8]);
                    c.setBool_capital(Boolean.valueOf(cidade[cidade.length - 7]));
                    c.setDec_lon(Double.valueOf(cidade[cidade.length - 6]));
                    c.setDec_lat(Double.valueOf(cidade[cidade.length - 5]));
                    c.setVar_desc_no_accents(cidade[cidade.length - 4]);
                    c.setVar_desc_alternativa(cidade[cidade.length - 3]);
                    c.setVar_microregiao(cidade[cidade.length - 2]);
                    c.setVar_mesoregiao(cidade[cidade.length - 1]);

                    if (cidadeDAO.inserir(c)) {
                        cidadeOK.add(c);
                    } else {
                        cidadeErro.add(c);
                    }
                }
                cont++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        auxJSON.addProperty("regOK", cidadeOK.size());
        auxJSON.addProperty("msgOK", "Registro(s) inserido(s) com sucesso");
        retornoJSON.add(auxJSON);
        auxJSON = new JsonObject();
        auxJSON.addProperty("regNOK", cidadeErro.size());
        auxJSON.addProperty("msgNOK", "Registro(s) com Erro");
        retornoJSON.add(auxJSON);

        int tmp = 1;

        for (Cidade e : cidadeErro) {
            auxJSON = new JsonObject();
            auxJSON.addProperty("reg", tmp);
            auxJSON.addProperty("varDesc", e.getVar_desc());
            auxJSON.addProperty("int_id_ibge", e.getInt_id_ibge());
            auxJSON.addProperty("exception", e.getEx().getMessage());
            retornoJSON.add(auxJSON);
            tmp++;

        }

        return gson.toJson(auxJSON);
    }

}
