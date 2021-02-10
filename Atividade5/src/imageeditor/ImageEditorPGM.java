
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Jediael
 */
public class ImageEditorPGM {

    int altura, largura, maxEscala;
    String numeroMagico;
    int[][] matriz, matrizResultado;

    public ImageEditorPGM(String numeroMagico, int altura, int largura, int maxEscala) {
        this.numeroMagico = numeroMagico;
        this.altura = altura;
        this.largura = largura;
        this.maxEscala = maxEscala;
        this.matriz = new int[altura][largura];
    }

    public String getNumeroMagico() {
        return numeroMagico;
    }

    public int getAltura() {
        return altura;
    }

    public int getLargura() {
        return largura;
    }

    public int getMaxEscala() {
        return maxEscala;
    }

    /*public ArrayList<Integer> getMatriz() {
        return matriz;
    }*/
    public void setNumeroMagico(String numeroMagico) {
        this.numeroMagico = numeroMagico;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public void setMaxEscala(int maxEscala) {
        this.maxEscala = maxEscala;
    }

    public void setValorMatriz(int i, int j, int valor) {
        this.matriz[i][j] = valor;
    }

    /*public void setMatriz(ArrayList<Integer> matriz) {
        this.matriz = matriz;
    }*/
    private void escurecerImagem(int valor) {
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.matriz[i][j] -= valor;
                if (this.matriz[i][j] < 0) {
                    this.matriz[i][j] = 0;
                }
            }
        }
    }

    private void clarearImagem(int valor) {
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.matriz[i][j] += valor;
                if (this.matriz[i][j] > this.maxEscala) {
                    this.matriz[i][j] = this.maxEscala;
                }
            }
        }
    }

    private void escrita() throws IOException {
        try {
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter("imagemEditada.pgm"));
            String linha = "", aux;
            buffWrite.append(this.numeroMagico + "\n");
            buffWrite.append(this.altura + " " + this.largura + "\n");
            buffWrite.append(this.maxEscala + "\n");
            for (int i = 0; i < this.altura; i++) {
                for (int j = 0; j < this.largura; j++) {
                    aux = Integer.toString(this.matrizResultado[i][j]) + " ";
                    linha += aux;
                }
                linha = linha.substring(0, linha.length() - 1);
                buffWrite.append(linha + "\n");
                linha = "";
            }

            buffWrite.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private static ImageEditorPGM leitura() {
        try {
            ImageEditorPGM img = null;
            String nome, linha2[], numMag = "";
            int alt = 0, larg = 0, esc = 0;
            nome = JOptionPane.showInputDialog(null, "Entre com o nome do arquivo");
            File file = new File(nome);
            String path = file.getAbsolutePath();
            System.out.println(path);

            int contLinha = 0, contElem = 0, numLin = 0, numCol = 0;
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (br.ready()) {
                String linha = br.readLine();
                linha2 = linha.split(" ");
                if (!linha2[0].equals("#")) {
                    switch (contLinha) {
                        case 0:
                            numMag = linha2[0];
                            break;
                        case 1:
                            alt = Integer.parseInt(linha2[0]);
                            larg = Integer.parseInt(linha2[1]);
                            break;
                        case 2:
                            esc = Integer.parseInt(linha2[0]);
                            break;
                        case 3:
                            img = new ImageEditorPGM(numMag, alt, larg, esc);

                            for (int j = 0; j < linha2.length; j++) {
                                img.setValorMatriz(numLin, numCol, Integer.parseInt(linha2[j]));
                                contElem++;
                                numCol++;
                                if (contElem % larg == 0) {
                                    numLin++;
                                    numCol = 0;
                                }

                            }
                            break;
                        default:
                            for (int j = 0; j < linha2.length; j++) {
                                img.setValorMatriz(numLin, numCol, Integer.parseInt(linha2[j]));
                                contElem++;
                                numCol++;
                                if (contElem % larg == 0) {
                                    numLin++;
                                    numCol = 0;
                                }

                            }
                    }
                    contLinha++;
                }
            }
            br.close();
            return img;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    private void fatiamento1(int a, int b, int nivelIntervalo, int nivelRestante) {
        this.matrizResultado = new int[this.altura][this.largura];
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                if (this.matriz[i][j] >= a && this.matriz[i][j] <= b) {
                    this.matrizResultado[i][j] = nivelIntervalo;
                } else {
                    this.matrizResultado[i][j] = nivelRestante;
                }
            }
        }
    }

    private void fatiamento2(int a, int b, int nivelIntervalo) {
        this.matrizResultado = new int[this.altura][this.largura];
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                if (this.matriz[i][j] >= a && this.matriz[i][j] <= b) {
                    this.matrizResultado[i][j] = nivelIntervalo;
                } else {
                    this.matrizResultado[i][j] = this.matriz[i][j];
                }
            }
        }
    }

    private double normalizacao(int valor) {
        return ((1.0 / this.maxEscala) * valor);
    }

    private int normalizacaoInersa(double valor) {
        return (int) (valor / (1.0 / this.maxEscala));
    }

    private void transformacaoGama(double c, double gama) {
        this.matrizResultado = new int[this.altura][this.largura];
        double temp;
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                temp = c * Math.pow(this.normalizacao(this.matriz[i][j]), gama);
                this.matrizResultado[i][j] = this.normalizacaoInersa(temp);
            }
        }
    }

    private void flipHorizontal() {
        this.matrizResultado = new int[this.altura][this.largura];
        int aux = this.largura - 1;
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.matrizResultado[i][j] = this.matriz[i][aux - j];
            }
        }
    }

    private double probabilidade(int x) {
        int cont = 0;
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                if (this.matriz[i][j] == x) {
                    cont++;
                }
            }
        }
        return (((double) cont) / (this.largura * this.altura));
    }

    private int arredondar(double x) {
        double aux = Math.floor(x), x_e_meio;
        x_e_meio = aux + 0.5;
        if (x <= x_e_meio) {
            return (int) aux;
        } else {
            if (aux == 255.0) {
                return (int) aux;
            } else {
                return (int) (aux + 1.0);
            }

        }
    }

    private void equalizacaoHistograma() {
        int[] vetorNiveis = new int[this.maxEscala + 1];
        double somatoriaProb = 0.0;
        this.matrizResultado = new int[this.altura][this.largura];
        for (int i = 0; i <= this.maxEscala; i++) {
            somatoriaProb = 0.0;
            for (int j = 0; j <= i; j++) {
                somatoriaProb += this.probabilidade(j);
            }
            vetorNiveis[i] = this.arredondar(this.maxEscala * somatoriaProb);

        }
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.matrizResultado[i][j] = vetorNiveis[this.matriz[i][j]];
            }
        }
    }

    private void filtroMedia() {
        this.matrizResultado = new int[this.altura][this.largura];
        String str;
        int larg, alt, mascara[][], cont = 0, a, b;
        double aux;
        str = JOptionPane.showInputDialog(null, "Entre com a largura do filtro");
        larg = Integer.parseInt(str);
        str = JOptionPane.showInputDialog(null, "Entre com a altura do filtro");
        alt = Integer.parseInt(str);
        mascara = new int[alt][larg];
        for (int i = 0; i < alt; i++) {
            for (int j = 0; j < larg; j++) {
                //str =  JOptionPane.showInputDialog(null, "Deseja usar filtro de média simples ou de média ponderada?");
                //str =  JOptionPane.showInputDialog(null, "Entre com o coeficiente da posição ("+i+", "+j+")");
                //mascara[i][j] = Integer.parseInt(str);
                mascara[i][j] = 1;
                cont += mascara[i][j];
            }
        }
        a = alt / 2;
        b = larg / 2;

        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                aux = 0;
                for (int s = -a; s <= a; s++) {
                    for (int t = -b; t <= b; t++) {
                        if ((i + s) >= 0 && (j + t) >= 0 && (i + s) < this.altura && (j + t) < this.largura) {
                            aux += this.matriz[i + s][j + t] * mascara[s + a][t + b];
                        }
                    }
                }
                aux /= cont;
                this.matrizResultado[i][j] = this.arredondar(aux);
                if (this.matrizResultado[i][j] > 255) {
                    this.matrizResultado[i][j] = 255;
                }
            }
        }

    }

    private void filtroMediana() {
        this.matrizResultado = new int[this.altura][this.largura];
        String str;
        int larg, alt, vetor[], cont = 0, a, b;

        str = JOptionPane.showInputDialog(null, "Entre com a largura do filtro");
        larg = Integer.parseInt(str);
        str = JOptionPane.showInputDialog(null, "Entre com a altura do filtro");
        alt = Integer.parseInt(str);

        vetor = new int[larg * alt];

        a = alt / 2;
        b = larg / 2;
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                cont = 0;
                for (int s = -a; s <= a; s++) {
                    for (int t = -b; t <= b; t++) {
                        if ((i + s) >= 0 && (j + t) >= 0 && (i + s) < this.altura && (j + t) < this.largura) {
                            vetor[cont] = this.matriz[i + s][j + t];
                        }
                        cont++;
                    }
                }
                Arrays.sort(vetor);
                this.matrizResultado[i][j] = vetor[(vetor.length) / 2];
            }
        }
    }

    private void binarizacao() {
        this.matrizResultado = new int[this.altura][this.largura];
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                if (this.matriz[i][j] < this.maxEscala / 2) {
                    this.matrizResultado[i][j] = 0;
                } else {
                    this.matrizResultado[i][j] = 255;
                }
            }
        }
    }

    private void efetuarProcessamento() {
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.matriz[i][j] = this.matrizResultado[i][j];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ImageEditorPGM img = leitura();
        //img.escurecerImagem(50);
        //img.fatiamento2(0, 100, 50); 
        //img.transformacaoGama(1, 0.1);
        //img.flipHorizontal();
        //img.equalizacaoHistograma();
        img.filtroMedia();
        img.efetuarProcessamento();
        img.binarizacao();
        //img.filtroMediana();
        img.escrita();
        System.out.println("Terminado");
    }

}
