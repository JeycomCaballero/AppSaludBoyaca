package co.sena.cimm.adso.saludboyaca.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.dto.Cita;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFGenerator {

    public static byte[] generarComprobanteCita(Cita cita) throws Exception {

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(doc, out);
        doc.open();

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
        Font sub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);

        Paragraph header = new Paragraph("COMPROBANTE OFICIAL DE CITA MÉDICA", titulo);
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);

        doc.add(new Paragraph(" "));
        doc.add(new LineSeparator());

        doc.add(new Paragraph(" "));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        doc.add(new Paragraph("DATOS DEL PACIENTE", sub));
        doc.add(new Paragraph("Paciente: "
                + (cita.getPaciente() != null ? cita.getPaciente().getNombres() : "N/A"), normal));

        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("DETALLE DE LA CITA", sub));

        doc.add(new Paragraph("Médico: "
                + (cita.getMedico() != null ? cita.getMedico().getNombres() : "N/A"), normal));

        doc.add(new Paragraph("Especialidad: "
                + (cita.getEspecialidad() != null ? cita.getEspecialidad().getNombre() : "N/A"), normal));

        doc.add(new Paragraph("Fecha: "
                + (cita.getFechaCita() != null ? sdf.format(cita.getFechaCita()) : "N/A"), normal));

        doc.add(new Paragraph("Hora: " + cita.getHoraCita(), normal));

        doc.add(new Paragraph("Motivo: " + cita.getMotivo(), normal));

        doc.add(new Paragraph("Estado: " + cita.getEstado(), normal));

        String obs = (cita.getObservaciones() == null || cita.getObservaciones().trim().isEmpty())
                ? "Sin observaciones"
                : cita.getObservaciones();

        doc.add(new Paragraph("Observaciones: " + obs, normal));

        doc.add(new Paragraph(" "));

        doc.add(new LineSeparator());
        doc.add(new Paragraph("Documento generado automáticamente por el sistema Salud Boyacá", normal));
        doc.add(new Paragraph("Este comprobante certifica la programación de la cita médica registrada.", normal));

        doc.close();

        return out.toByteArray();
    }

    public static byte[] generarListaCitasHoy(List<Cita> lista) throws Exception {

        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(doc, out);

        doc.open();

        Font titulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                18,
                BaseColor.BLUE
        );

        Font normal = FontFactory.getFont(
                FontFactory.HELVETICA,
                10
        );

        Font header = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                11,
                BaseColor.WHITE
        );

        Paragraph t = new Paragraph(
                "LISTA DE CITAS DEL DÍA",
                titulo
        );

        t.setAlignment(Element.ALIGN_CENTER);

        doc.add(t);

        doc.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(6);

        tabla.setWidthPercentage(100);

        tabla.setWidths(new float[]{1, 3, 2, 2, 2, 2});

        PdfPCell cell;

        String[] headers = {
            "ID",
            "Paciente",
            "Fecha",
            "Hora",
            "Especialidad",
            "Estado"
        };

        for (String h : headers) {

            cell = new PdfPCell(new Phrase(h, header));

            cell.setBackgroundColor(BaseColor.DARK_GRAY);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cell.setPadding(5);

            tabla.addCell(cell);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Cita c : lista) {

            tabla.addCell(String.valueOf(c.getId()));

            tabla.addCell(
                    c.getPaciente().getNombres()
                    + " "
                    + c.getPaciente().getApellidos()
            );

            tabla.addCell(sdf.format(c.getFechaCita()));

            tabla.addCell(c.getHoraCita());

            tabla.addCell(c.getEspecialidad().getNombre());

            tabla.addCell(c.getEstado());
        }

        doc.add(tabla);

        doc.add(new Paragraph(" "));

        Paragraph footer = new Paragraph(
                "Documento generado automáticamente por Salud Boyacá",
                normal
        );

        footer.setAlignment(Element.ALIGN_CENTER);

        doc.add(footer);

        doc.close();

        return out.toByteArray();
    }
}
