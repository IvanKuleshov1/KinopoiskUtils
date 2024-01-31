package ivan.k.scraper;

public class Review {

    private String autorName;
    private String reviewTittle;
    //TODO переписать на таймстемпы
    private String reviewDate;
    private String reviewTime;
    private ReviewType type;
    private int isUsefulVotes;
    private int isNotUsefulVotes;

    /*//TODO распарсить текст, начинать с конца - триггеры:
                                                    - bold
                                                    - line break
                                                    - "value / 10"

    private int rating;
    */

    /* TODO:
         - писать в csv localpath до txt
         - ?добавить метаданные по количеству слов, символов и т.д.
    private String text;
    */

    public Review(String autorName, String reviewTittle, String reviewDate, String reviewTime, ReviewType type, int isUsefulVotes, int isNotUsefulVotes) {
        this.autorName = autorName;
        this.reviewTittle = reviewTittle;
        this.reviewDate = reviewDate;
        this.reviewTime = reviewTime;
        this.type = type;
        this.isUsefulVotes = isUsefulVotes;
        this.isNotUsefulVotes = isNotUsefulVotes;
    }

    public ReviewType getType() {
        return type;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public String getAutorName() {
        return autorName;
    }

    public void setAutorName(String autorName) {
        this.autorName = autorName;
    }

    public String getReviewTittle() {
        return reviewTittle;
    }

    public void setReviewTittle(String reviewTittle) {
        this.reviewTittle = reviewTittle;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }


    public int getIsUsefulVotes() {
        return isUsefulVotes;
    }

    public void setIsUsefulVotes(int isUsefulVotes) {
        this.isUsefulVotes = isUsefulVotes;
    }

    public int getIsNotUsefulVotes() {
        return isNotUsefulVotes;
    }

    public void setIsNotUsefulVotes(int isNotUsefulVotes) {
        this.isNotUsefulVotes = isNotUsefulVotes;
    }
}
