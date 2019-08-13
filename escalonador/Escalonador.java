
package escalonador;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author danda
 */

class FCFS {

    private List<Processo> listaProc;
    private int numProcessos;
    private float tempRet;
    private float tempResp;
    private float tempEsp;
    //ideia: quem chegou primeiro faz primeiro até o fim

    FCFS(List<Processo> listaProc, int numProcessos) {
        this.listaProc = listaProc;
        this.numProcessos = numProcessos;
        tempRet = atribuirRetorno();
        tempResp = atribuirResposta();
        tempEsp = atribuirEspera();

        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("FCFS " + df.format(tempRet) + " " + df.format(tempResp) + " " + df.format(tempEsp));

    }

    public float atribuirRetorno() {
        int tempoTotal = 0;
        int duracoes = 0;
        Processo anterior = listaProc.get(0);
        tempoTotal = anterior.getDuracao() - anterior.getTempoChegada();
        duracoes = anterior.getDuracao();
        for (int i = 1; i < numProcessos; i++) {
            Processo proximo = listaProc.get(i);
            duracoes = duracoes + proximo.getDuracao();
            tempoTotal = tempoTotal + duracoes - proximo.getTempoChegada();
        }
        return (float) tempoTotal / numProcessos;
    }

    public float atribuirResposta() {
        int tempoTotal = 0;
        int tempoComecou = 0;
        int tempoEsperou = 0;
        Processo anterior = listaProc.get(0);
        tempoComecou = anterior.getDuracao();
        for (int i = 1; i < numProcessos; i++) {
            Processo proximo = listaProc.get(i);
            tempoEsperou = tempoEsperou + tempoComecou - proximo.getTempoChegada();
            // o tempo comecou p cada um muda, pois tem q ser sempre a soma das duracoes dos anteriores
            tempoComecou = tempoComecou + proximo.getDuracao();
        }
        return (float) tempoEsperou / numProcessos;
    }

    public float atribuirEspera() {
        int tempoTotal = 0;
        int tempoAux = 0;
        Processo anterior = listaProc.get(0);
        int tempoAuxTotal = anterior.getDuracao();
        for (int i = 1; i < numProcessos; i++) {
            Processo proximo = listaProc.get(i);
            tempoAux = tempoAuxTotal - proximo.getTempoChegada();
            tempoTotal = tempoTotal + tempoAux;
            tempoAuxTotal = tempoAuxTotal + proximo.getDuracao();
        }
        return (float) tempoTotal / numProcessos;
    }

}

class SJF {

    private int numProcessos;
    private List<Processo> listaProc;
    private float tempRet;
    private float tempResp;
    private float tempEsp;

    //ideia: quem chegou primeiro faz inteiro, mas o próximo é o com menor pico dos que chegaram até tal tempo
    SJF(List<Processo> listaProc, int numProcessos) {
        this.listaProc = listaProc;
        this.numProcessos = numProcessos;
        tempRet = 0;
        tempResp = 0;
        tempEsp = 0;
        reOrdena();
        execucaoSJF();
        tempRet = tempRet/numProcessos;
        tempResp = tempResp/numProcessos;
        tempEsp = tempEsp/numProcessos;
        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("SJF " + df.format(tempRet) + " " + df.format(tempResp) + " " + df.format(tempEsp));
    }

    public void execucaoSJF() {
        List<Processo> auxLista = listaProc;
        auxLista.get(0).setInicializado(true);
        Processo atual = auxLista.get(0);
        Processo proximo = auxLista.get(1);
        Processo troca = new Processo(0, 0);
        int tamanho = numProcessos;
        int j, tempo = 0;
        boolean encontrou = false;
        
        do {
            tempo++;
            //já começa diminuindo a duracao do que está primeiro na fila 
            auxLista.get(0).setDuracaoAtual(auxLista.get(0).getDuracaoAtual() - 1);
            //se o processo tiver terminado, tira ele da lista e atualiza tempo de retorno
            if (auxLista.get(0).getDuracaoAtual() == 0) {
                tempRet = tempRet + tempo - auxLista.get(0).getTempoChegada();
                auxLista.remove(auxLista.get(0));
                tamanho--;
            }
            //verifica se após a retirada do processo passado ainda há elementos na lista
            if (!auxLista.isEmpty()) {
                atual = auxLista.get(0);
                troca = atual;
                for (j = 1; j < tamanho; j++) {
                    proximo = auxLista.get(j);
                    if ((proximo.getTempoChegada() == tempo || proximo.getTempoChegada() < tempo) && proximo.getDuracaoAtual() < troca.getDuracaoAtual()) {
                        encontrou = true;
                        troca = proximo;
                    }
                }
                if (encontrou) {
                    auxLista.remove(troca);
                    auxLista.set(0, troca);
                    auxLista.add(atual);
                    encontrou = false;
                }
                if (!auxLista.get(0).isInicializado()) {
                    auxLista.get(0).setInicializado(true);
                    tempResp = tempResp + tempo - auxLista.get(0).getTempoChegada();
                    //como é sem preempsao, significa que se esse foi inicializado ele vai terminar com certeza 
                    // e tem a garantia de que no momento q começar ele é o com menor pico
                    tempEsp = tempEsp + tempo - auxLista.get(0).getTempoChegada();
                }
                atual = auxLista.get(0);

            }
        } while (!auxLista.isEmpty());

    }

    public void reOrdena() {
        List<Processo> auxList1 = new ArrayList<>();
        List<Processo> auxList2 = new ArrayList<>();
        Processo primeiro = listaProc.get(0);
        int tempoPrimeiro = listaProc.get(0).getTempoChegada();
        int tempo = tempoPrimeiro;
        int contador = 0;

        //conta quantos tem com o mesmo tempo no inicio
        while (tempo == tempoPrimeiro && contador < numProcessos) {
            contador++;
            tempo = listaProc.get(contador).getTempoChegada();
        }
        // se tiver mais de um numero com tempo igual
        int i, j;

        for (j = 0; j < contador; j++) {
            auxList1.add(listaProc.get(j));
        }
        auxList1.add(new Processo(-10, -10));

        int tamanho = auxList1.size() - 1;
        int auxTam = tamanho;
        for (j = 0; j < tamanho; j++) {
            primeiro = auxList1.get(0);
            // se nao tiver chegado ao fim da lista
            if (auxList1.get(1).getTempoChegada() > -10) {
                for (i = 0; i < auxTam; i++) {
                    if (primeiro.getDuracao() > auxList1.get(i).getDuracao()) {
                        primeiro = auxList1.get(i);
                    }
                }
            } else {
                primeiro = auxList1.get(0);
            }
            auxTam--;
            auxList1.remove(primeiro);
            auxList2.add(primeiro);
        }
        for (i = contador; i < numProcessos; i++) {
            auxList2.add(listaProc.get(i));
        }
        listaProc = auxList2;
    }

}

class RR {

    private int numProcessos;
    private List<Processo> listaProc;
    private float tempRet;
    private float tempResp;
    private float tempEsp;
    private int quantum;

    RR(List<Processo> listaProc, int numProcessos) {
        this.listaProc = listaProc;
        this.numProcessos = numProcessos;
        tempRet = 0;
        tempResp = 0;
        tempEsp = 0;
        quantum = 2;
        execucao();
        tempRet = tempRet/numProcessos;
        tempResp = tempResp/numProcessos;
        tempEsp = tempEsp/numProcessos;
        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("RR " + df.format(tempRet) + " " + df.format(tempResp) + " " + df.format(tempEsp));
    }

    public void execucao() {
        List<Processo> auxList = new ArrayList<>();
        List<Processo> naoExec = listaProc;
        auxList.add(naoExec.get(0));
        naoExec.remove(0);
        Processo atual = auxList.get(0);
        Processo auxP = new Processo(0, 0);
        int tempo = 0, tamanho = 1;
        int numAtual = 1, compensadorImpar = 0;
        auxList.get(0).setInicializado(true);

        //coloca os c tempo inicial na fila p ser executado
        boolean tempoIgual = true;
        while (tempoIgual) {
            if (!naoExec.isEmpty() && (naoExec.get(0).getTempoChegada() == auxList.get(0).getTempoChegada())) {
                auxList.add(naoExec.get(0));
                naoExec.remove(naoExec.get(0));
                tamanho++;
                numAtual++;
            } else {
                tempoIgual = false;
            }
        }

        while (!auxList.isEmpty()) {
            atual = auxList.get(0);
            
            //verifica se o pico do primeiro da lista - quantum vai ser 0 ou menor do que zero, entao ele precisa ser removido
            // além de precisar ajustar os tempos
            if ((atual.getDuracaoAtual() - quantum) <= 0) {
                if (atual.getDuracao() - quantum < 0) {
                    compensadorImpar++;
                }
                if (numAtual - 1 > 0) {
                    tempEsp = tempEsp + ((numAtual - 1) * quantum);
                }
                //isso é para garantir os casos em que na primeira vez q ele vai ser executado, como o pico é igual ao quantum
                //o processo já morre, aí tem que atualizar tempo de resposta
                if (auxList.get(0).isInicializado() == false) {
                    auxList.get(0).setInicializado(true);
                    tempResp = tempResp + tempo - auxList.get(0).getTempoChegada();
                }
                tempo = tempo + atual.getDuracaoAtual();

                tempRet = tempRet + tempo + -auxList.get(0).getTempoChegada();
                auxList.remove(atual);
                numAtual--;
                tamanho--;
                //coloca na lista quem já está em tempo o suficiente
                tempoIgual = true;
                while (tempoIgual) {
                    if (!naoExec.isEmpty() && (naoExec.get(0).getTempoChegada() == tempo || naoExec.get(0).getTempoChegada() < tempo)) {
                        auxList.add(naoExec.get(0));
                        naoExec.remove(naoExec.get(0));
                        tamanho++;
                        numAtual++;
                    } else {
                        tempoIgual = false;
                    }
                }
            } else {
                //vê se ele já foi inicializado
                if (auxList.get(0).isInicializado() == false) {
                    auxList.get(0).setInicializado(true);
                    tempResp = tempResp + tempo - auxList.get(0).getTempoChegada();
                }
                if (numAtual - 1 > 0) {
                    tempEsp = tempEsp + ((numAtual - 1) * quantum);
                }
                auxList.get(0).setDuracaoAtual(auxList.get(0).getDuracaoAtual() - quantum);
                tempo = tempo + quantum;

                tempoIgual = true;
                while (tempoIgual) {
                    if (!naoExec.isEmpty() && (naoExec.get(0).getTempoChegada() == tempo || naoExec.get(0).getTempoChegada() < tempo)) {
                        auxList.add(naoExec.get(0));
                        naoExec.remove(naoExec.get(0));
                        tamanho++;
                        numAtual++;
                    } else {
                        tempoIgual = false;
                    }
                }

                if (tamanho > 1) {
                    //muda a que esta executando para a proxima, se ela estiver no tempo
                    if (auxList.get(1).getTempoChegada() == tempo || auxList.get(1).getTempoChegada() < tempo) {
                        auxP = auxList.get(0);
                        Processo fimFila = auxP;
                        auxList.add(fimFila);
                        auxList.remove(auxP);
                        atual = auxList.get(0);
                    }
                } else {
                    auxP = auxList.get(0);
                    Processo fimFila = auxP;
                    auxList.add(fimFila);
                    auxList.remove(auxP);
                    atual = auxList.get(0);

                }
            }
        }
        tempEsp = tempEsp - compensadorImpar;
    }
}

public class Escalonador {

    private static List<Integer> entrada;
    private static List<Processo> listaProc;
    private static List<Processo> listaProcFCFS;
    private static List<Processo> listaProcSJF;
    private static List<Processo> listaProcRR;
    private static int numProcessos;

    public static void main(String[] args) {
        entrada = new ArrayList<>();
        entrada = recebeEntrada();
        numProcessos = entrada.size() / 2;
        listaProc = new ArrayList<>();
        
        listaProcFCFS = new ArrayList<>();
        listaProcSJF = new ArrayList<>();
        listaProcRR = new ArrayList<>();
        
        preencheProc();
        ordenaProc();
        for(Processo p: listaProc){
        listaProcFCFS.add(new Processo(p.getTempoChegada(), p.getDuracao()));
        listaProcSJF.add(new Processo(p.getTempoChegada(), p.getDuracao()));
        listaProcRR.add(new Processo(p.getTempoChegada(), p.getDuracao()));
        }
        //confere se os dados recebidos foram 100% certos
        if (entrada == null) {
            //dados errados
        } else {
            FCFS fcfs = new FCFS(listaProcFCFS, numProcessos);
            SJF sjf = new SJF(listaProcSJF, numProcessos);
            RR rr = new RR(listaProcRR, numProcessos);
        }

    }

    public static List<Integer> recebeEntrada() {
        List<Integer> entradaAux = new ArrayList<>();
        String[] aux = null;
        Scanner input = new Scanner(System.in);
        String eof = "";

        do {
            String in = input.nextLine();
            aux = in.split(" ");
            //verificação de dados de entrada
            // se aux.lenght for menor do que dois só recebeu um numero e um espaço
            if (aux.length < 2) {
                System.out.println("Dados errados");
                eof = "erro";
                return null;
            } else {
                entradaAux.add(Integer.parseInt(aux[0]));
                entradaAux.add(Integer.parseInt(aux[1]));
                // determina o final da entrada ao receber 
                eof = input.findInLine("xx");
            }

        } while (eof == null);

        return entradaAux;
    }

    public static void preencheProc() {
        int i;
        int j = 0;
        int k = 1;
        for (i = 0; i < numProcessos; i++) {
            listaProc.add(new Processo(entrada.get(j), entrada.get(k)));
            j = j + 2;
            k = k + 2;
        }
    }

    public static void ordenaProc() {
        List<Processo> auxProc = new ArrayList<>();
        for (Processo aux : listaProc) {
            auxProc.add(aux);
        }
        int k;
        auxProc.add(new Processo(-10, -10));
        listaProc.clear();
        int i, j;
        int auxTam = numProcessos;

        Processo primeiro = auxProc.get(0);

        for (j = 0; j < numProcessos; j++) {
            primeiro = auxProc.get(0);
            // se nao tiver chegado ao fim da lista
            if (auxProc.get(1).getTempoChegada() > -10) {
                for (i = 0; i < auxTam; i++) {
                    if (primeiro.getTempoChegada() > auxProc.get(i).getTempoChegada()) {
                        primeiro = auxProc.get(i);
                    }
                }
            } else {
                primeiro = auxProc.get(0);
            }
            auxTam--;
            auxProc.remove(primeiro);
            listaProc.add(primeiro);
        }
    }
}
