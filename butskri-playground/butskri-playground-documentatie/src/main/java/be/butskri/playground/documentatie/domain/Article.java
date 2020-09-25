package be.butskri.playground.documentatie.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Article {

    private UUID id;
    private LocalDate date;
    private String category;
    private String link;
    private String name;
    private Status status;
    private int interest;
    private Integer score;
    private List<String> tags;

    public static Builder builder() {
        return new Builder();
    }

    private Article(Builder builder) {
        this.date = builder.date;
        this.category = builder.category;
        this.link = builder.link;
        this.name = builder.name;
        this.status = builder.status;
        this.interest = builder.interest;
        this.score = builder.score;
        this.tags = builder.tags;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public int getInterest() {
        return interest;
    }

    public Integer getScore() {
        return score;
    }

    public List<String> getTags() {
        return tags;
    }

    public static class Builder {
        private LocalDate date;
        private String category;
        private String link;
        private String name;
        private Status status;
        private int interest;
        private Integer score;
        private List<String> tags;

        public Article build() {
            return new Article(this);
        }

        public Builder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withInterest(int interest) {
            this.interest = interest;
            return this;
        }

        public Builder withScore(Integer score) {
            this.score = score;
            return this;
        }

        public Builder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }
    }
}
