import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Aluno {
    private String matricula;
    private String nome;
    private double nota;

    public Aluno(String matricula, String nome, double nota) {
        this.matricula = matricula;
        this.nome = nome;
        this.nota = nota;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public double getNota() {
        return nota;
    }
}

public class App {
    public static void main(String[] args) {
        try {
            String caminhoArquivo = "alunos.csv";

            // Verifique se o arquivo existe
            File file = new File(caminhoArquivo);
            if (!file.exists()) {
                System.out.println("Arquivo não encontrado: " + caminhoArquivo);
                return;
            }

            List<Aluno> alunos = lerAlunos(caminhoArquivo);
            gerarResumo(alunos, "resumo.csv");

            for (Aluno aluno : alunos) {
                System.out.println("Matricula: " + aluno.getMatricula() + ", Nome: " + aluno.getNome() + ", Nota: "
                        + aluno.getNota());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Aluno> lerAlunos(String arquivo) throws IOException {
        List<Aluno> alunos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine(); // Pular a linha do cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.trim().split(";");
                if (campos.length >= 3) {
                    String matricula = campos[0];
                    String nome = campos[1];
                    double nota = Double.parseDouble(campos[2].replace(",", "."));
                    Aluno aluno = new Aluno(matricula, nome, nota);
                    alunos.add(aluno);
                } else {
                    System.out.println("Linha inválida no arquivo de alunos: " + linha);
                }
            }
        }
        return alunos;
    }

    private static void gerarResumo(List<Aluno> alunos, String arquivo) throws IOException {
        int totalAlunos = alunos.size();
        int aprovados = 0;
        int reprovados = 0;
        double menorNota = Double.MAX_VALUE;
        double maiorNota = Double.MIN_VALUE;
        double somaNotas = 0.0;

        for (Aluno aluno : alunos) {
            double nota = aluno.getNota();
            if (nota >= 6.0) {
                aprovados++;
            } else {
                reprovados++;
            }
            if (nota < menorNota) {
                menorNota = nota;
            }
            if (nota > maiorNota) {
                maiorNota = nota;
            }
            somaNotas += nota;
        }

        double mediaNota = totalAlunos > 0 ? somaNotas / totalAlunos : 0.0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            bw.write("Total de Alunos, Aprovados, Reprovados, Menor Nota, Maior Nota, Média Geral");
            bw.newLine();
            bw.write(totalAlunos + "," + aprovados + "," + reprovados + "," + menorNota + "," + maiorNota + ","
                    + mediaNota);
            bw.newLine();
        }
    }
}
