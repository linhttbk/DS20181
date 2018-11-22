package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.IO;

public class PdfExportFragment extends BaseFragment {

    @BindView(R.id.edt_film_name)
    EditText edtFilmName;
    @BindView(R.id.radio_type_table)
    RadioButton rdbTable;
    @BindView(R.id.radio_type_line)
    RadioButton rdbLine;
    @BindView(R.id.rdg_template)
    RadioGroup rdgTemplate;
    @BindView(R.id.btn_export_pdf)
    Button btnExport;

    private String filmName = EMPTY;
    private int templateType = 0;
    private List<FileRecord> fileRecords;

    public static PdfExportFragment newInstance(List<FileRecord> fileRecords) {
        Bundle args = new Bundle();
        PdfExportFragment fragment = new PdfExportFragment();
        fragment.fileRecords = fileRecords;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_pdf_export;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        handleClickEvent();
    }

    @Override
    public void initData() {
        super.initData();
    }


    public void handleClickEvent() {
        rdgTemplate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_type_table:
                        templateType = 0;
                        break;
                    case R.id.radio_type_line:
                        templateType = 1;
                        break;
                    default:
                        templateType = -1;
                        break;
                }
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmName = edtFilmName.getText().toString();

                if (filmName.equals(EMPTY)) {
                    Toast.makeText(getContext(), "Hãy nhập tên phim", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (templateType == -1) {
                    Toast.makeText(getContext(), "Hãy chọn template", Toast.LENGTH_SHORT).show();
                    return;
                }

                createFile(fileRecords, templateType, filmName);
                Toast.makeText(getContext(), "Xuất file thành công", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).showListFile();

            }
        });
    }

    /**
     * @param recordList
     * @param templateType
     * @param filmName     type = 0 or 1
     *                     0 is list record with table
     *                     1 is list record without table
     * @throws IOException
     * @throws DocumentException
     */
    private void createFile(List<FileRecord> recordList, int templateType, String filmName) {
        //get path

        Document document = new Document();
        try {
            String folderPath = getActivity().getApplication().getFilesDir().getAbsolutePath() + File.separator + "PdfFile";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String fileName = StringUtils.getTodayTime() + "_" + app.getCurrentUser().getName() + "_" + filmName + ".pdf";
            String path = folderPath + File.separator + fileName;

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            PdfWriter.getInstance(document, fileOutputStream);

            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("DSD_08");
            document.addCreator("Backdoor Team");

            // Adding Title....
            BaseFont font = BaseFont.createFont("res/font/times.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(font, 18.0f, Font.NORMAL, BaseColor.BLACK);

            //Creating Chunk
            Chunk headerChunk = new Chunk("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", headerFont);
            Paragraph paragraph = new Paragraph(headerChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            //Creating Chunk
            Font headerFont2 = new Font(font, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk headerChunk2 = new Chunk("Độc lập - Tự do - Hạnh Phúc", headerFont2);
            Paragraph paragraph2 = new Paragraph(headerChunk2);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph2);


            //Line seperator
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(BaseColor.WHITE);
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            //Header title
            Font headerFont3 = new Font(font, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk title3 = new Chunk("BIÊN BẢN CUỘC HỌP", headerFont3);
            Paragraph titleParagraph3 = new Paragraph(title3);
            titleParagraph3.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph3);

            //title
            Font titleFont = new Font(font, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk title = new Chunk(filmName, titleFont);
            Paragraph titleParagraph = new Paragraph(title);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);

            //Line seperator
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            //Content file
            Font contentFont = new Font(font, 14.0f, Font.NORMAL, BaseColor.BLACK);
            switch (templateType) {
                //table
                case 0:
                    float[] columnWidths = {3, 2, 5};
                    PdfPTable table = new PdfPTable(columnWidths);
                    table.setWidthPercentage(100);
                    table.getDefaultCell().setUseAscender(true);
                    table.getDefaultCell().setUseDescender(true);

                    table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell("Time");
                    table.addCell("Speaker");
                    table.addCell("Content");
                    for (FileRecord record : recordList) {
                        table.addCell(getNormalCell(StringUtils.formatLongToDate(record.getTime()), contentFont));
                        table.addCell(getNormalCell(record.getSpeaker(), contentFont));
                        table.addCell(getNormalCell(record.getContent(), contentFont));
                    }
                    document.add(table);
                    break;

                // line
                case 1:
                    for (FileRecord record : recordList) {
                        Chunk chunk = new Chunk(StringUtils.formatLongToDate(record.getTime()) + " " +
                                record.getSpeaker() + ": " + record.getContent(), headerFont);

                        //Creating Paragraph to add...
                        Paragraph p = new Paragraph(chunk);
                        p.setAlignment(Element.ALIGN_LEFT);
                        document.add(p);

                    }
                    break;

                default:
                    break;

            }

            document.close();

        } catch (IOException | DocumentException e) {
            if (document.isOpen()) {
                document.close();
            }
            e.printStackTrace();
        }
    }

    public static PdfPCell getNormalCell(String string, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(string, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

}
