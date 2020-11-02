package Helpers;

import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Export<T>  implements Runnable{
    private TableView<T> tableView;
    private StackPane sp;

    public Export(TableView<T> tableView,StackPane sp) {
        this.tableView = tableView;
        this.sp=sp;
    }

    public void excel() {

        XSSFWorkbook xssfWorkbook=new XSSFWorkbook();
        XSSFSheet xssfSheet=  xssfWorkbook.createSheet("Sheet1");
        XSSFRow firstRow= xssfSheet.createRow(0);

        //set titles of columns
        for (int i=0; i<tableView.getColumns().size();i++){

            firstRow.createCell(i).setCellValue(tableView.getColumns().get(i).getText());

        }


        for (int row=0; row<tableView.getItems().size();row++){

            XSSFRow hssfRow= xssfSheet.createRow(row+1);

            for (int col=0; col<tableView.getColumns().size(); col++){

                Object celValue = tableView.getColumns().get(col).getCellObservableValue(row).getValue();

                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                    }
                } catch (  NumberFormatException e ){

                    hssfRow.createCell(col).setCellValue(celValue.toString());
                }

            }

        }
        //Terminate process if an excel sheet is generated
        int rowTotal = xssfSheet.getLastRowNum();
        if(rowTotal==0){
            DialogMessages dm= new DialogMessages(sp);
            dm.NoDataToExport();
            return;
        }

        //Create mew file chooser
        FileChooser fileChooser = new FileChooser();

        //Set extension filter to .xlsx files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        //If file is not null, write to file using output stream.
        if (file != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
                xssfWorkbook.write(outputStream);
                xssfWorkbook.close();
                DialogMessages dm= new DialogMessages(sp);
                dm.ExportSuccessful();
            }
            catch (IOException ex) {

            }

        }


    }

    @Override
    public void run() {
        excel();
    }
}
