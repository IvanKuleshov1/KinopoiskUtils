package ivan.k.utils;

import ivan.k.scraper.Review;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVWriter {
    public void writeReviewsToCSV(List<Review> reviews, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
//        try (FileWriter writer = new FileWriter(filePath)) {

            // Проверяем, существует ли файл
            if (!Files.exists(Paths.get(filePath)) || Files.size(Paths.get(filePath)) == 0) {
                // Запись заголовков, так как файл пуст или не существует
                writer.append("autorName,reviewTittle,reviewDate,reviewTime,reviewType,isUsefulVotes,isNotUsefulVotes\n");
            }

            // Запись рецензий
            for (Review review : reviews) {
                writer.append("\"" + review.getAutorName() + "\"" + ","
                        + "\"" + review.getReviewTittle() + "\"" + ","
                        + review.getReviewDate() + ","
                        + review.getReviewTime() + ","
                        + review.getType() + ","
                        + review.getIsUsefulVotes() + ","
                        + review.getIsNotUsefulVotes() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
