package be.butskri.playground.documentatie.domain.main;

import be.butskri.playground.documentatie.domain.data.Article;
import be.butskri.playground.documentatie.domain.mappers.ArticleToMarkdownFormatter;
import be.butskri.playground.documentatie.domain.mappers.UrlToArticleMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleCsvLineGenerator {

    private static final File OUTPUT_CSV_FILE = new File("butskri-playground/butskri-playground-documentatie/src/main/resources/output.csv");

    private UrlToArticleMapper urlToArticleMapper = new UrlToArticleMapper();
    private ArticleToMarkdownFormatter formatter = new ArticleToMarkdownFormatter();

    public static void main(String[] args) throws IOException {
        List<String> urls = readUrls();

        new ArticleCsvLineGenerator().printArticleLines(urls);
    }

    private static List<String> readUrls() {
        InputStream inputStream = ArticleCsvLineGenerator.class.getResourceAsStream("/urls.txt");
        return IOUtils.readLines(inputStream, "UTF-8")
                .stream()
                .filter(url -> !url.startsWith("#"))
                .collect(Collectors.toList());
    }

    private void printArticleLines(List<String> urls) throws IOException {
        List<Article> articles = urlToArticleMapper.toArticles(urls);
        List<String> lines = articles.stream().map(this::toArticleLine).collect(Collectors.toList());
        FileUtils.writeLines(OUTPUT_CSV_FILE, "UTF-8", lines);
    }

    private String toArticleLine(Article article) {
        return formatter.format(article);
    }

}