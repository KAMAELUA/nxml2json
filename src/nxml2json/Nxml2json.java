/*
 * The MIT License
 *
 * Copyright 2015 Andrey.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nxml2json;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrey Chugay
 */
public class Nxml2json {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length > 0 && args[0].compareTo("-auto") == 0) {
            String extension = args.length > 1 ? "." + args[1] : ".nxml";
            File[] files = finder(".", extension);
            
            if (files.length > 0)
            {
                System.out.println(files.length + " files were found.");
                for(File fi : files)
                {
                    String fileName = fi.getName();
                    fileName = fileName.substring(0, fileName.length() - extension.length());
                    convertArticle(fileName + extension, fileName + ".json");
                }
                System.out.println("All files were converted successfully.");
            }
            else{
                System.out.println("Files not found.");
            }
            

        } else {
            String inPath = args.length > 0 ? args[0] : "article.nxml";
            String outPath = args.length > 1 ? args[1] : "result.json";

            convertArticle(inPath, outPath);
        }

    }

    public static ArticleObject convertArticle(String input, String output) {
        DocumentBuilderFactory domFactory = null;
        DocumentBuilder builder = null;

        Document doc = null;
        ArticleObject value = null;
        domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(false);

        try {

            builder = domFactory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId)
                        throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
            });

            doc = builder.parse(input);
            
            DocumentType strrrr = doc.getDoctype();
            if(strrrr == null || strrrr.getName().compareTo("article") != 0)
            {
                System.out.println("\"" + input + "\" invalid XML file.");
                return null;
            }

        } catch (ParserConfigurationException pex) {
        } catch (SAXException sex) {
            System.out.println("\"" + input + "\" invalid XML file.");
            return null;
            //System.exit(0);
        } catch (IOException ioex) {
            System.out.println("File \"" + input + "\" not found.");
            System.exit(0);
        } finally {
            
        }
        System.out.println("Please wait a sec while \"" + input + "\" is parsing.");
        value = new ArticleObject(doc);
        Gson gson = new Gson();

        value.parse();
        String json = gson.toJson(value);

        try {
            FileWriter writer = new FileWriter(output);
            writer.write(json);
            writer.close();
        } catch (IOException ioex) {
            System.out.println("oops. We're sorry, but something went wrong.");
        } finally {
            System.out.println("File successfully saved as \"" + output + "\".");
        }

        return value;
    }

    public static File[] finder(String dirName, String ext) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(ext);
            }
        });

    }

}
