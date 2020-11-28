package be.butskri.playground.documentatie.domain.mappers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleNameResolverTest {

    private static final String URL = "http://somesite.someplace.org/my-page.html";

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private ArticleNameResolver nameResolver;

    @Test
    public void getArticleNameFromH1TagWhenPresent() {
        when(restTemplateMock.getForObject(URL, String.class))
                .thenReturn("<html><head><title>Blablabla</title></head><body><h1>My Article Name</h1></body></html>");
        Optional<String> name = nameResolver.resolveArticleName(URL);

        assertThat(name).contains("My Article Name");
    }

    @Test
    public void getArticleNameFromTitleWhenH1TagNotPresent() {
        when(restTemplateMock.getForObject(URL, String.class))
                .thenReturn("<html><head><title>My Article Name</title></head><body><div>other stuff</div></body></html>");
        Optional<String> name = nameResolver.resolveArticleName(URL);

        assertThat(name).contains("My Article Name");
    }
}