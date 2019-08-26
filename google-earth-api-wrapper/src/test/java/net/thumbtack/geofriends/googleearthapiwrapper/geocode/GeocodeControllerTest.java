package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GeocodeController.class)
public class GeocodeControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private GeocodeService geocodeService;

    @Test
    public void geocode_whenCorrect_mustHttpStatusOk() throws Exception {
        when(geocodeService.geocode(any())).thenReturn(new ArrayList<>());

        ResultActions resultActions = mvc.perform(
                post("/api/geo/geocode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(new ArrayList<>())));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void geocode_whenCorrect_mustCorrectJsonContent() throws Exception {
        List<City> cities = Collections.singletonList(new City(1, "name", "country", 2, 3));
        when(geocodeService.geocode(any())).thenReturn(cities);

        ResultActions resultActions = mvc.perform(
                post("/api/geo/geocode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(cities)));

        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(cities)));
    }
}