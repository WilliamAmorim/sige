package org.unifimes.gestaoescolar.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.unifimes.gestaoescolar.model.NotaTable;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PdfGenerator {

    public static void gerarBoletim(List<NotaTable> notas, String caminhoArquivo) {
        try (PDDocument document = new PDDocument()) {
            // Agrupar por aluno
            Map<String, List<NotaTable>> agrupadoPorAluno = notas.stream()
                    .collect(Collectors.groupingBy(NotaTable::getNome));

            for (Map.Entry<String, List<NotaTable>> entry : agrupadoPorAluno.entrySet()) {
                String nomeAluno = entry.getKey();
                List<NotaTable> notasAluno = entry.getValue();

                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream content = new PDPageContentStream(document, page);

                // Header Verde
                content.setNonStrokingColor(Color.decode("#009688")); // Verde
                content.addRect(0, 740, 612, 60);
                content.fill();

                // Título
                content.beginText();
                content.setNonStrokingColor(Color.WHITE);
                content.setFont(PDType1Font.HELVETICA_BOLD, 20);
                content.newLineAtOffset(230, 770);
                content.showText("BOLETIM");
                content.endText();

                // Nome da escola
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(220, 750);
                content.showText("Carlos Montessori");
                content.endText();

                // Informações do aluno
                int yInfo = 710;
                content.setNonStrokingColor(Color.BLACK);
                drawText(content, "Nome: " + nomeAluno, 50, yInfo);
                drawText(content, "Ano: ________", 300, yInfo);
                drawText(content, "Professor: ___________", 50, yInfo - 20);
                drawText(content, "Turma: ____________", 300, yInfo - 20);

                // Cabeçalhos da tabela
                int y = 650;
                drawTableHeader(content, y);

                // Linhas de disciplinas
                content.setFont(PDType1Font.HELVETICA, 11);
                int lineHeight = 25;
                y -= lineHeight;

                for (NotaTable nota : notasAluno) {
                    drawText(content, nota.getMatricula(), 50, y);
                    drawText(content, formatNota(nota.getNota01()), 200, y);
                    drawText(content, formatNota(nota.getNota02()), 280, y);
                    drawText(content, formatNota(nota.getNota03()), 360, y);
                    drawText(content, formatNota(nota.getNota04()), 440, y);
                    y -= lineHeight;
                }

                // Rodapé
                y -= 20;
                drawText(content, "N.º DE DIAS ESCOLARES: ___________", 50, y);
                drawText(content, "N.º DE AUSÊNCIAS: ___________", 50, y - 20);
                drawText(content, "N.º DE PRESENÇAS: ___________", 50, y - 40);

                // Sistema de notas
                drawText(content, "SISTEMA DE NOTAS:", 350, y);
                drawText(content, "A+ = 98-100   A = 95-97   B+ = 93-94   B = 90-92", 350, y - 20);
                drawText(content, "B- = 87-89   C+ = 83-86   C = 80-82   C- = 78-79", 350, y - 40);
                drawText(content, "D = 75-77   F = 74 OU MENOS", 350, y - 60);

                content.close();
            }

            document.save(caminhoArquivo);
            System.out.println("Boletins gerados em: " + caminhoArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawText(PDPageContentStream content, String text, int x, int y) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 11);
        content.setNonStrokingColor(Color.BLACK);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    private static void drawTableHeader(PDPageContentStream content, int y) throws IOException {
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.setNonStrokingColor(Color.decode("#009688"));
        drawText(content, "MATÉRIA", 50, y);
        drawText(content, "1º TRIMESTRE", 200, y);
        drawText(content, "2º TRIMESTRE", 280, y);
        drawText(content, "3º TRIMESTRE", 360, y);
        drawText(content, "4º TRIMESTRE", 440, y);
    }

    private static String formatNota(double nota) {
        return nota == 0.0 ? "-" : String.format("%.1f", nota);
    }
}
