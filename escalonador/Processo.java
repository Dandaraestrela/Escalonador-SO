/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escalonador;

/**
 *
 * @author danda
 */
public class Processo {

    private int tempoChegada;
    private int duracao;
    private int tempRetorno;
    private int tempResposta;
    private int tempEspera;
    private int duracaoAtual;
    private boolean inicializado;

    Processo(int tempoChegada, int duracao) {
        this.tempoChegada = tempoChegada;
        this.duracao = duracao;
        duracaoAtual = duracao;
        tempRetorno = 0;
        tempResposta = 0;
        tempEspera = 0;
        inicializado = false;
    }

    public int getTempRetorno() {
        return tempRetorno;
    }

    public int getTempResposta() {
        return tempResposta;
    }

    public int getTempEspera() {
        return tempEspera;
    }

    public int getTempoChegada() {
        return tempoChegada;
    }

    public int getDuracao() {
        return duracao;
    }

    public int getDuracaoAtual() {
        return duracaoAtual;
    }

    public void setDuracaoAtual(int novaDuracao) {
        duracaoAtual = novaDuracao;
    }

    public void setTempRetorno(int tempRetorno) {
        this.tempRetorno = tempRetorno;
    }

    public void setTempResposta(int tempResposta) {
        this.tempResposta = tempResposta;
    }

    public void setTempEspera(int tempEspera) {
        this.tempEspera = tempEspera;
    }

    public boolean isInicializado() {
        return inicializado;
    }

    public void setInicializado(boolean inicializado) {
        this.inicializado = inicializado;
    }

}
