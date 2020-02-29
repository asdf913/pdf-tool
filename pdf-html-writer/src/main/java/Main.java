import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.pdf.PdfWriter;

public class Main {

	public static void main(final String[] args) {
		// step 1: creation of a document-object
		try (final Document document = new Document();
				final Reader reader = new StringReader("<html><h1>H1</h1><h2>h2</h2><h3>h3</h3><h4>h4</h4></html>")) {
			PdfWriter.getInstance(document, new FileOutputStream("parseHelloWorld.pdf"));
			// step 2: we open the document
			document.open();
			// step 3: parsing the HTML document to convert it in PDF
			HtmlParser.parse(document, reader);
		} catch (final DocumentException | IOException de) {
			System.err.println(de.getMessage());
		}
	}

}