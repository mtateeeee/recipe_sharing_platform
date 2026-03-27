package com.recipeshare.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageProcessingService {

    private static final int MAX_WIDTH = 1200;
    private static final int MAX_HEIGHT = 800;
    private static final String ALLOWED_CONTENT = "image/";

    @Value("${app.upload-dir}")
    private String uploadDir;

    public String saveAndProcess(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        validateImage(file);
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        String ext = getExtension(file.getOriginalFilename());
        String filename = "recipe_" + UUID.randomUUID() + ext;
        Path target = dir.resolve(filename);
        BufferedImage img = ImageIO.read(file.getInputStream());
        if (img != null) {
            BufferedImage resized = resizeIfNeeded(img);
            ImageIO.write(resized, ext.replace(".", ""), target.toFile());
        } else {
            file.transferTo(target.toFile());
        }
        return "/uploads/" + filename;
    }

    public List<String> saveMultiple(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                String url = saveAndProcess(f);
                if (url != null) urls.add(url);
            }
        }
        return urls;
    }

    private void validateImage(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !type.toLowerCase().startsWith(ALLOWED_CONTENT)) {
            throw new IllegalArgumentException("Chỉ chấp nhận file ảnh");
        }
    }

    private String getExtension(String name) {
        if (name == null || !name.contains(".")) return ".jpg";
        return name.substring(name.lastIndexOf(".")).toLowerCase();
    }

    private BufferedImage resizeIfNeeded(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        if (w <= MAX_WIDTH && h <= MAX_HEIGHT) return img;
        double scale = Math.min((double) MAX_WIDTH / w, (double) MAX_HEIGHT / h);
        int nw = (int) (w * scale);
        int nh = (int) (h * scale);
        BufferedImage resized = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, nw, nh, null);
        g.dispose();
        return resized;
    }
}
