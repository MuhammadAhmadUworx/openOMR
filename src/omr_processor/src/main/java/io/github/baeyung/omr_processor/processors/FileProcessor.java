package io.github.baeyung.omr_processor.processors;

import io.github.baeyung.omr_processor.models.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileProcessor
{
    static String getNewFileName(int index, String month)
    {
        return "OMR_" + month + "_" + index;
    }

    static String getFileExt(String ext)
    {
        return switch (ext)
        {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> ".png";
        };
    }

    static byte[] zipAllFiles(List<File> files) throws IOException
    {
        // Create a byte output stream to hold zip content
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);

        for (File image : files)
        {
            // Add file to zip
            zos.putNextEntry(new ZipEntry(image.getFileName()));
            zos.write(image.getFile());
            zos.closeEntry();
        }

        zos.close();

        return byteArrayOutputStream.toByteArray();
    }
}
