package org.js.view;

import jakarta.enterprise.inject.Model;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Part;
import java.io.*;
@Model
public class UploadBean   {
    private Part part;
    public Part getPart() {
        return part;
    }
    public void setPart(Part part) {
        this.part = part;
    }


    public String uploadFile() {
        // Extract file name from content-disposition header of file part
        String fileName = getFileName(part);
        String basePath = "/tmp/";
        File outputFilePath = new File(basePath + fileName);
        // Copy uploaded file to destination path
        try (InputStream inputStream = part.getInputStream();
             OutputStream outputStream = new FileOutputStream(outputFilePath)){
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            printMessage("Success! File upload completed!");
        } catch (IOException e) {
            e.printStackTrace();
            printMessage("Error! File upload error!");
        }
        return null;
    }
    private void printMessage(String message) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    private String getFileName(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }
}
