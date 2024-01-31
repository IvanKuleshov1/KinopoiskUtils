package ivan.k.scraper;

import ivan.k.exceptions.ScraperException;
import ivan.k.selenium.WebDriverFactory;
import ivan.k.utils.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class KinopoiskScraper {
    private static final String KINOPOISK_URL = "https://www.kinopoisk.ru/film/326/reviews/";

    public void scrapeAndSaveReviews() {
        // Инициализация WebDriver
        WebDriver driver = initializeWebDriver();

        // Загрузка страницы
        navigateToPage(driver, KINOPOISK_URL);

        // Извлечение рецензий с текущей страницы
        List<Review> reviews = extractReviews(driver);

        // Сохранение рецензий в CSV
        saveReviewsToCSV(reviews);

        // Переход к следующей странице, если она доступна
        while (hasNextPage(driver)) {
            navigateToNextPage(driver);

            // Извлечение рецензий с новой страницы
            reviews = extractReviews(driver);

            // Сохранение рецензий в CSV
            saveReviewsToCSV(reviews);
        }

        // Закрытие WebDriver после обработки всех страниц
        driver.quit();
    }

    private boolean hasNextPage(WebDriver driver) {
        try {
            WebElement nextButton = driver.findElement(By.xpath("//ul[@class='list']//li[@class='arr']/a[contains(text(), '»')]"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void navigateToNextPage(WebDriver driver) {
        WebElement nextButton = driver.findElement(By.xpath("//ul[@class='list']//li[@class='arr']/a[contains(text(), '»')]"));
        nextButton.click();
    }
    private WebDriver initializeWebDriver() {
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        return webDriverFactory.getWebDriver();
    }

    private void navigateToPage(WebDriver driver, String url) {
        driver.get(url);
        // Дополнительная логика по ожиданию или другие манипуляции с браузером
    }

    private List<Review> extractReviews(WebDriver driver) {
        List<Review> reviews = new ArrayList<>();

        // Используем Jsoup для парсинга HTML-кода
        Document document = Jsoup.parse(driver.getPageSource());

        // Находим все элементы с классом "reviewItem" и "userReview"
        List<Element> reviewElements = document.select("div.reviewItem.userReview");

        // Обработка каждой рецензии
        for (Element reviewElement : reviewElements) {
            //String text = reviewElement.select("span[itemprop=reviewBody]").text();
            String author = reviewElement.select("p.profile_name a").text();
            String title = reviewElement.select("p.sub_title").text();
            String dateAndTime = reviewElement.select("span.date").text();
            ReviewType type = getReviewType(reviewElement);

            // Извлекаем дату и время из строки "DD MMMM YYYY | HH:MM"
            String[] dateTimeParts = dateAndTime.split("\\|");
            String reviewDate = dateTimeParts[0].trim();
            String reviewTime = dateTimeParts[1].trim();

            // Пример для получения полезных и неполезных голосов
            int[] votes = extractVotes(reviewElement);
            int isUsefulVotes = votes[0];
            int isNotUsefulVotes = votes[1];

            // Создаем объект Review и добавляем его в список
            Review review = new Review(author, title, reviewDate, reviewTime, type, isUsefulVotes, isNotUsefulVotes);


            reviews.add(review);
        }

        return reviews;
    }

    private int[] extractVotes(Element reviewElement) {
        // Находит элемент <li> с атрибутом id, начинающимся с "comment_num_vote_"
        Element usefulElement = reviewElement.select("li[id^=comment_num_vote_]").first();

        // Проверка, что элемент найден
        if (usefulElement != null) {
            // Получение текста из элемента и разделение на части
            String votesText = usefulElement.text();
            String[] votesArray = votesText.split("/");

            // Преобразование строк в целые числа
            int isUsefulVotes = Integer.parseInt(votesArray[0].trim());
            int isNotUsefulVotes = Integer.parseInt(votesArray[1].trim());

            return new int[]{isUsefulVotes, isNotUsefulVotes};
        } else {
            return new int[]{0, 0}; // Возвращаем значения по умолчанию, если элемент не найден
        }
    }

    private ReviewType getReviewType(Element reviewElement) {
        // Находим внутренний div с классом "response"
        Element responseDiv = reviewElement.selectFirst("div.response");

        // Проверяем наличие классов "good", "neutral", "bad" и возвращаем соответствующий тип
        if (responseDiv.hasClass("good")) {
            return ReviewType.POSITIVE;
        } else if (responseDiv.hasClass("neutral")) {
            return ReviewType.NEUTRAL;
        } else if (responseDiv.hasClass("bad")) {
            return ReviewType.NEGATIVE;
        }

        // Если не удалось определить тип, возвращаем UNKNOWN, мб потом добавить обработку
        return ReviewType.UNKNOWN;
    }


    private void saveReviewsToCSV(List<Review> reviews) {
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.writeReviewsToCSV(reviews, "reviews.csv");
    }
}