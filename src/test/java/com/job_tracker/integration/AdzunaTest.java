package com.job_tracker.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.job_tracker.scheduler.AdzunaSchedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import java.math.BigDecimal;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"adzuna.url=http://localhost:8099",
        "adzuna.api-id=test_id",
        "adzuna.api-key=test_key",
        "jwt.secret=test_jwt_secret",
        "jwt.expiration=80000"})
public class AdzunaTest {

    @Autowired
    private AdzunaSchedule adzunaSchedule;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private WireMockServer wireMockServer;


    @BeforeEach
    void setUp(){
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8099));
        wireMockServer.start();
        configureFor("localhost", 8099);
        jdbcTemplate.execute("TRUNCATE TABLE email_queue, reminders, applications, vacancies RESTART IDENTITY CASCADE");
        stubFor(get(urlMatching("/jobs/pl/search/.*"))
                .withQueryParam("app_id", equalTo("test_id"))
                .withQueryParam("app_key", equalTo("test_key"))
                .withQueryParam("results_per_page", equalTo("10"))
                .withQueryParam("what", equalTo("java"))
                .withQueryParam("content-type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "count": 1,
                                  "results": [
                                               {
                                                "id": "someId",
                                                "title": "somePosition",
                                                "company": {
                                                 "display_name": "someCompany"
                                                  },
                                                "description": "someDescription",
                                                 "location": {
                                                  "area": ["Polska", "Mazowieckie", "Warszawa"]
                                                   },
                                                "redirect_url": "someRedirectUrl",
                                                "salary_max": 8000.00,
                                                "salary_min": 5000.00
                                                 }
                                               ]
                                  }
                                """)));
    }

    @AfterEach
    void tearDown(){
        wireMockServer.stop();
    }

    @Test
    void shouldReturnVacanciesSuccessfully() {
        adzunaSchedule.runImport();

        var result = jdbcTemplate.queryForMap("""
                SELECT v.external_id, v.position, v.company, v.description, v.location, v.redirect_url,
                       v.salary_max, v.salary_min
                FROM vacancies v
                WHERE v.external_id = 'someId'
                """);
        assertThat(result.get("position")).isEqualTo("somePosition");
        assertThat(result.get("company")).isEqualTo("someCompany");
        assertThat(result.get("description")).isEqualTo("someDescription");
        assertThat(result.get("location").toString()).isEqualTo("{Polska,Mazowieckie,Warszawa}");
        assertThat(result.get("redirect_url")).isEqualTo("someRedirectUrl");
        assertThat(result.get("salary_max")).isEqualTo(new BigDecimal("8000.00"));
        assertThat(result.get("salary_min")).isEqualTo(new BigDecimal("5000.00"));
    }

    @Test
    void isNotSaveIfExists(){
        adzunaSchedule.runImport();
        adzunaSchedule.runImport();
        var result = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM vacancies
                """, Integer.class);
        assertThat(result).isEqualTo(1);
    }
}
