package helpers.jasper_reports;

import helpers.dialog_messages.DialogMessages;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import view_models.tables.TransactionVM;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class CreateTransactionReport<T>  implements Runnable {

    private ObservableList<TransactionVM> transactionList;
    private String jrxmlFileName;
    private StackPane stackPane;


    public CreateTransactionReport(ObservableList<TransactionVM> transactionList, String jrxmlFileName, StackPane stackpane) {
        this.transactionList = transactionList;
        this.jrxmlFileName = jrxmlFileName;
        this.stackPane=stackpane;
    }

    public void GenerateReport() {
        try{
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(transactionList);

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", itemsJRBean);

            File currentDirectory = new File(new File(".").getAbsolutePath());
            String path=currentDirectory.getAbsolutePath();

            //Get current Project Directory
            String src=System.getProperty("user.dir");

            //read jrxml file and creating jasperdesign object
            InputStream input = new FileInputStream(new File(src + "\\src\\helpers\\jasper_reports\\jrxml_files\\" + jrxmlFileName + ".jrxml"));

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            /*compiling jrxml with help of JasperReport class*/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            /* Using jasperReport object to generate PDF */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            //Create mew file chooser
            FileChooser fileChooser = new FileChooser();

            //Set extension filter to .xlsx files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.xlsx)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(null);

            //If file is not null, write to file using output stream.
            if (file != null) {
                try (FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
                    /* Write content to PDF file */
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    DialogMessages dm= new DialogMessages(stackPane);
                    dm.ExportSuccessful();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public void run() {
        GenerateReport();
    }
}
