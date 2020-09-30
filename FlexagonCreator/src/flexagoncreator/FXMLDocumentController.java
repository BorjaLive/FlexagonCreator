/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flexagoncreator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import static flexagoncreator.Flexagon.generatePDF;
import imageUtils.ImageLoader;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author borja
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private ImageView img1, img2, img3, img4, img5, img6, btnExit;
    @FXML
    private JFXButton btnSelect, btnGenerate;
    @FXML
    private JFXSpinner spnLoad;
    @FXML
    private TextField textFile;
    
    private imageUtils.Image[] images;
    
    @FXML
    private void handleButtonAction(MouseEvent event) {
        if(event.getTarget() == btnExit){
            Platform.exit();
        }else if(event.getTarget() == btnSelect){
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Document", "pdf");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String text = chooser.getSelectedFile().getAbsolutePath();
                if(!text.toLowerCase().endsWith(".pdf"))
                    text+=".pdf";
                textFile.setText(text);
            }
        }else if(event.getTarget() == btnGenerate){
            spnLoad.setVisible(true);
            if(Flexagon.generatePDF(images, textFile.getText())){
                try {
                    Desktop.getDesktop().open(new File(textFile.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            spnLoad.setVisible(false);
        }else if(event.getTarget() == img1){
            setImages(0);
        }else if(event.getTarget() == img2){
            setImages(1);
        }else if(event.getTarget() == img3){
            setImages(2);
        }else if(event.getTarget() == img4){
            setImages(3);
        }else if(event.getTarget() == img5){
            setImages(4);
        }else if(event.getTarget() == img6){
            setImages(5);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        images = new imageUtils.Image[6];
        spnLoad.setVisible(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setImages(int n){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Bitmap Image", "jpg", "png");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            int i = n;
            while(i-n < files.length && i < images.length){
                String path = files[i-n].getAbsolutePath();
                try {
                    imageUtils.Image img = ImageLoader.fromFile(new File(path));
                    img = img.getResizedToSquare(img.getWidth(), 0);
                    images[i] = img;
                    getImgByN(i).setImage(new Image(new ByteArrayInputStream(img.getBytes())));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
        }
    }
    private ImageView getImgByN(int i){ //Porque crear un array de ImageView no me funciona
        switch(i){
            case 0: return img1;
            case 1: return img2;
            case 2: return img3;
            case 3: return img4;
            case 4: return img5;
            case 5: return img6;
            default: return null;
        }
    }
}
