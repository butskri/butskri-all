package be.butskri.playground.documentatie.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleToMarkdownFormatter {

    public String format(Article article) {
        // Date added	Status	Category	Name	Interest	Score	Tags (Comma-separated)	Name	Hyperlink
        return new InnerFormatter()
                .append(article.getDate(), 1, 12)
                .append(article.getStatus(), 1, 8)
                .append(article.getCategory())
                .append("")
                .appendScore(article.getInterest(), 6, 10)
                .appendScore(article.getScore(), 3, 7)
                .append(article.getTags(), 1, 64)
                .append(article.getName(), 1, 95)
                .append(article.getLink(), 1, 118)
                .asString();
    }

    private static class InnerFormatter {
        private String result = "";

        public InnerFormatter append(LocalDate localDate, int precedingWhitespace, int totalLength) {
            return append(localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), precedingWhitespace, totalLength);
        }

        public InnerFormatter append(List<String> values, int precedingWhitespace, int totalLength) {
            if (values.isEmpty()) {
                return append("???");
            }
            return append(StringUtils.join(values, ", "), precedingWhitespace, totalLength);
        }

        public InnerFormatter append(Object obj, int precedingWhitespace, int totalLength) {
            return append(emptyValueIfNull(obj), precedingWhitespace, totalLength);
        }

        public InnerFormatter append(Object obj) {
            return append(emptyValueIfNull(obj), 1, 1);
        }

        public InnerFormatter appendScore(Integer value, int precedingWhitespace, int totalLength) {
            return append(formatScore(value), precedingWhitespace, totalLength);
        }

        public InnerFormatter append(String str, int precedingWhitespace, int totalLength) {
            result = result
                    .concat(emptyValueIfNull(str))
//                    .concat(StringUtils.repeat(" ", precedingWhitespace))
//                    .concat(StringUtils.rightPad(emptyValueIfNull(str), totalLength - precedingWhitespace, " "))
                    .concat("|");
            return this;
        }

        public String asString() {
            return result;
        }

        private String formatScore(Integer value) {
            return StringUtils.leftPad(emptyValueIfNull(value), 3, " ");
        }

        private String emptyValueIfNull(Object obj) {
            if (obj == null) {
                return "";
            }
            return String.valueOf(obj);
        }
    }
}
