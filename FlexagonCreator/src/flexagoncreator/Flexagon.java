package flexagoncreator;

import imageUtils.Image;
import imageUtils.ImageLoader;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Flexagon {
    private static final float SIZE_CONST = 2.83464582f;
    private static final float H = PDRectangle.A4.getHeight(), W = PDRectangle.A4.getWidth(), MARGIN = 10*SIZE_CONST, SIDE = (W-(MARGIN*2.0f))/4.0f;
    private static final int[][] POSITION = {{0,2,0,2},{0,0,1,0},{0,3,2,0},{0,1,3,2},{1,2,0,2},{1,0,1,0},{1,3,2,0},{1,1,3,2},{1,3,3,2},{1,2,3,2},{1,1,0,2},{1,0,0,2},{0,3,1,2},{0,0,3,2},{0,3,0,2},{0,0,2,2},{0,1,0,1},{0,3,3,1},{0,0,0,1},{0,2,3,1},{1,0,3,3},{1,0,2,3},{1,3,1,3},{1,3,0,3}}; //[porcion de imagen][dato{pagina, posX, posY, angulo/90}]
    
    //Las imagenes deben ser 6 y cuadradas
    public static boolean generatePDF(Image[] images, String file){
        if(images.length != 6) return false;
        
        try {
            PDDocument doc = new PDDocument();
            PDPage page1 = new PDPage(PDRectangle.A4), page2 = new PDPage(PDRectangle.A4);
            PDPageContentStream contents[] = {new PDPageContentStream(doc, page1), new PDPageContentStream(doc, page2)};
            
            for(int i = 0; i < images.length; i++){
                try {
                    Image img = images[i];
                    int w = img.getWidth(), h = img.getHeight();
                    
                    contents[POSITION[4*i+0][0]].drawImage(PDImageXObject.createFromByteArray(doc, img.crop(0, 0, w/2, h/2).rotate(POSITION[4*i+0][3]*90).getBytes(), ""), MARGIN+POSITION[4*i+0][1]*SIDE, H-MARGIN-SIDE-POSITION[4*i+0][2]*SIDE, SIDE, SIDE);
                    contents[POSITION[4*i+1][0]].drawImage(PDImageXObject.createFromByteArray(doc, img.crop(w/2, 0, w, h/2).rotate(POSITION[4*i+1][3]*90).getBytes(), ""), MARGIN+POSITION[4*i+1][1]*SIDE, H-MARGIN-SIDE-POSITION[4*i+1][2]*SIDE, SIDE, SIDE);
                    contents[POSITION[4*i+2][0]].drawImage(PDImageXObject.createFromByteArray(doc, img.crop(0, h/2, w/2, h).rotate(POSITION[4*i+2][3]*90).getBytes(), ""), MARGIN+POSITION[4*i+2][1]*SIDE, H-MARGIN-SIDE-POSITION[4*i+2][2]*SIDE, SIDE, SIDE);
                    contents[POSITION[4*i+3][0]].drawImage(PDImageXObject.createFromByteArray(doc, img.crop(w/2, h/2, w, h).rotate(POSITION[4*i+3][3]*90).getBytes(), ""), MARGIN+POSITION[4*i+3][1]*SIDE, H-MARGIN-SIDE-POSITION[4*i+3][2]*SIDE, SIDE, SIDE);
                } catch (IOException ex) {
                    return false;
                }
            }
            
            contents[0].close();
            contents[1].close();
            doc.addPage(page1);
            doc.addPage(page2);
            doc.save(file);
            doc.close();
            
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

}
