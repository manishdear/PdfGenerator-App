package com.ucd.pdfprint;
import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    TextView total;
    EditText item_name,hsn,price,qty,gstin,tax_rates,pan,date_trans;
    EditText seller,billing;
    Button btn,calc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        total = (TextView)findViewById(R.id.total);
        item_name = (EditText)findViewById(R.id.item_name);
        hsn = (EditText)findViewById(R.id.hsn);
        price = (EditText)findViewById(R.id.price);
        qty = (EditText)findViewById(R.id.qty);
        gstin = (EditText)findViewById(R.id.gstin);
        tax_rates = (EditText)findViewById(R.id.tax_rates);
        pan = (EditText)findViewById(R.id.pan);
        date_trans = (EditText)findViewById(R.id.date_of_trans);
        seller = (EditText)findViewById(R.id.seller_add);
        billing = (EditText)findViewById(R.id.bill_add);
        btn = (Button)findViewById(R.id.button);
        calc = findViewById(R.id.calculate);
        calc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                double Price = Double.parseDouble(price.getText().toString());
                int Qty =  Integer.parseInt(qty.getText().toString());
//                if(Price <= 0.0)
//                {
//                    price.setError("Price Required!");
//                    price.requestFocus();
//                    return;
//                }
//                if(Qty <= 0)
//                {
//                    qty.setError("Quantity must be > 0");
//                    qty.requestFocus();
//                    return;
//                }
                double Total = Price*Qty;
                total.setText(Double.toString(Total));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Price = Double.parseDouble(price.getText().toString());
                int Qty =  Integer.parseInt(qty.getText().toString());
                double Tax_rates = Double.parseDouble(tax_rates.getText().toString());
                String Item_name = item_name.getText().toString();
                String Hsn = hsn.getText().toString();
                String Gstin = gstin.getText().toString();
                String Pan = pan.getText().toString();
                String Date_trans = date_trans.getText().toString();
                String Seller = seller.getText().toString();
                String Billing = billing.getText().toString();
//                if(Price <= 0.0)
//                {
//                    price.setError("Price Required!");
//                    price.requestFocus();
//                    return;
//                }
//                if(Qty <= 0)
//                {
//                    qty.setError("Quantity must be > 0");
//                    qty.requestFocus();
//                    return;
//                }
//                if(Tax_rates <= 0.0)
//                {
//                    tax_rates.setError("Tax rates must be > 0");
//                    tax_rates.requestFocus();
//                    return;
//                }
//                if(Item_name.isEmpty())
//                {
//                    item_name.setError("Item Name Required!");
//                    item_name.requestFocus();
//                    return;
//                }
//                if(Hsn.isEmpty())
//                {
//                    hsn.setError("Hsn Required!");
//                    hsn.requestFocus();
//                    return;
//                }
//                if(Gstin.isEmpty())
//                {
//                    gstin.setError("Gstin Required!");
//                    gstin.requestFocus();
//                    return;
//                }
//                if(Pan.isEmpty())
//                {
//                    pan.setError("Pan card no Required!");
//                    pan.requestFocus();
//                    return;
//                }
//                if(Date_trans.isEmpty())
//                {
//                    date_trans.setError("Date of transaction Required!");
//                    date_trans.requestFocus();
//                    return;
//                }
//                if(Seller.isEmpty())
//                {
//                    seller.setError("Seller Details Required!");
//                    seller.requestFocus();
//                    return;
//                }
//                if(Billing.isEmpty())
//                {
//                    billing.setError("Billing Details Required!");
//                    billing.requestFocus();
//                    return;
//                }
                savePdf(Price,Qty,Tax_rates,Item_name,Hsn,Gstin,Pan,Date_trans,Seller,Billing);
            }
        });
    }
    public void savePdf(double price, int qty, double tax_rates, String item_name, String hsn, String gstin, String pan, String date_trans, String seller, String billing) {
        Document document = new Document();
        String fileName;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fileName = "Invoice "+ DateFormat.getDateTimeInstance().format(new Date());
        }
        else
        {
            fileName = "invoice default";
        }
        String file_path = Environment.getExternalStorageDirectory() + "/" + fileName + ".pdf";
        try{
            PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(file_path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addTitle("Invoice For "+item_name);
            document.addAuthor("APEIRON");

            //Assets
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;
            BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            //Creating Chunks to write altogether.
            Font invoiceTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            // Creating Chunk
            Chunk invoiceTitleChunk = new Chunk("INVOICE", invoiceTitleFont);
            // Creating Paragraph to add...
            Paragraph invoiceTitleParagraph = new Paragraph(invoiceTitleChunk);
            // Setting Alignment for Heading
            invoiceTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            // Finally Adding that Chunk
            document.add(invoiceTitleParagraph);


            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font gstinFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk gstinChunk = new Chunk("GSTIN:                                        PAN No:\n", gstinFont);
            Paragraph gstinParagraph = new Paragraph(gstinChunk);
            gstinParagraph.setAlignment(Element.ALIGN_LEFT);
            gstinParagraph.add(gstin+"                  "+pan);
            document.add(gstinParagraph);
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font hscFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk hscChunk = new Chunk("HSC No:                                    Date of Transaction\n", hscFont);
            Paragraph panParagraph = new Paragraph(hscChunk);
            panParagraph.setAlignment(Element.ALIGN_LEFT);
            panParagraph.add(hsn+"                                             "+date_trans);
            document.add(panParagraph);
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font sellerFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk sellerChunk = new Chunk("Seller info: \n", hscFont);
            Paragraph sellParagraph = new Paragraph(sellerChunk);
            sellParagraph.setAlignment(Element.ALIGN_LEFT);
            sellParagraph.add(seller);
            sellParagraph.add("\n\n");
            sellParagraph.add("Consumer Info:\n");
            sellParagraph.add(billing);
            document.add(sellParagraph);
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();
            Toast.makeText(this,fileName+".pdf is \n saved to \n"+file_path,Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}


























