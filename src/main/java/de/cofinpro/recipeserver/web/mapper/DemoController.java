package de.cofinpro.recipeserver.web.mapper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class DemoController {

    private static final Map<String, BufferedImage> images = Map.of(
            "green", createImage(Color.GREEN),
            "magenta", createImage(Color.MAGENTA)
    );

    @GetMapping(path = "image")
    public ResponseEntity<BufferedImage> getImage(@RequestParam String name,
                                                  @RequestParam String mediaType) {
        BufferedImage image = images.get(name);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .body(image);
    }

    private static BufferedImage createImage(Color color) {
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 500, 500);
        g.dispose();

        return image;
    }
}
