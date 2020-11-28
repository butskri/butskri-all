package be.butskri.playground.documentatie.domain.mappers;

import be.butskri.playground.documentatie.domain.data.Article;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ArticleToMarkdownFormatter {
    public String format(Article article) {
        // Date added	Status	Category	Link	Interest	Score	Tags (Comma-separated)	Name	Hyperlink
        return new InnerFormatter()
                .append(article.getDate())
                .append(article.getStatus())
                .append(article.getCategory())
                .append("")
                .appendScore(article.getInterest())
                .appendScore(article.getScore())
                .append(article.getTags())
                .append(article.getName())
                .append(article.getLink())
                .asString();
    }

    private static class InnerFormatter {
        private String result = "";

        public InnerFormatter append(LocalDate localDate) {
            return append(localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }

        public InnerFormatter append(List<String> values) {
            if (values.isEmpty()) {
                return append("???");
            }
            return append(StringUtils.join(values, ", "));
        }

        public InnerFormatter append(Object obj) {
            return append(emptyValueIfNull(obj));
        }

        public InnerFormatter appendScore(Integer value) {
            return append(formatScore(value));
        }

        public InnerFormatter append(String str) {
            result = result
                    .concat(emptyValueIfNull(str))
                    .concat("|");
            return this;
        }

        public String asString() {
            return result;
        }

        private String formatScore(Integer value) {
            return emptyValueIfNull(value);
        }

        private String emptyValueIfNull(Object obj) {
            if (obj == null) {
                return "";
            }
            return String.valueOf(obj);
        }
    }
}
