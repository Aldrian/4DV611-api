package se.travappar.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageHelper {

    private static final Logger logger = LogManager.getLogger(ImageHelper.class);
//    public static final String PATHNAME = "G:\\Tools\\apache-tomcat-8.0.28\\img\\";
    public static final String PATHNAME = "/home/zayats/images/";
    public static final String IMAGE_URL = "http://46.101.168.154:8080/static/";

    public String saveOfferImage(String imageSource, String eventID) throws IOException {
        Matcher matcher = Pattern.compile("data:image/(png|jpeg|jpg);").matcher(imageSource);
        String extension = "";
        String baseImage = "";
        if (matcher.find()) {
            String group = matcher.group();
            extension = group.substring(group.indexOf('/') + 1, group.indexOf(';'));
        } else {
            logger.error("No image is in OfferImageSource file. Should be image with png, jpg or jpeg formats.");
            throw new RuntimeException("No image is in OfferImageSource file. Should be image with png, jpg or jpeg formats.");
        }
        matcher = Pattern.compile("base64,(.)*").matcher(imageSource);
        if (matcher.find()) {
            baseImage = matcher.group().replace("base64,", "");
        } else {
            logger.error("OfferImageSource doesn't contain base64 file.");
            throw new RuntimeException("OfferImageSource doesn't contain base64 file.");
        }
        InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(baseImage.getBytes()));
        File imageFolder = new File(PATHNAME);
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        File imageFile = new File(imageFolder.getAbsolutePath() + File.separator + eventID + "." + extension);
        if (!imageFile.exists()) {
            imageFile.createNewFile();
        } else {
            imageFile.delete();
            imageFile.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        return IMAGE_URL + imageFile.getName();
    }

    public void removeOfferImage(String offerImage) {
        File image = new File(offerImage);
        File actualImage = new File(PATHNAME + image.getName());
        if(actualImage.exists()) {
            actualImage.delete();
        }
    }
}
