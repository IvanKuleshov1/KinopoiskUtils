package ivan.k;


import ivan.k.scraper.KinopoiskScraper;

public class Main {
    public static void main(String[] args) {
        // Инициализация и запуск сборщика рецензий
        KinopoiskScraper kinopoiskScraper = new KinopoiskScraper();
        kinopoiskScraper.scrapeAndSaveReviews();
    }
}