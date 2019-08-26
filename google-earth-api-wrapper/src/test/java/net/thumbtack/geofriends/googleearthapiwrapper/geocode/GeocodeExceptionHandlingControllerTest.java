package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.errors.InvalidRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static net.thumbtack.geofriends.googleearthapiwrapper.geocode.GeocodeExceptionHandlingController.ErrorCode.GOOGLE_API_ERROR;
import static net.thumbtack.geofriends.googleearthapiwrapper.geocode.GeocodeExceptionHandlingController.SOMETHING_WRONG_WITH_GOOGLE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class GeocodeExceptionHandlingControllerTest {
    @MockBean
    GeocodeService geocodeService;
    private MockMvc mvc;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.
                standaloneSetup(new GeocodeController(geocodeService)).
                setControllerAdvice(GeocodeExceptionHandlingController.class).
                build();
        mapper = new ObjectMapper();
    }

    @Test
    public void geocode_whenApiException_mustHttpStatus500() throws Exception {
        when(geocodeService.geocode(any())).thenThrow(new InvalidRequestException(""));

        ResultActions resultActions = mvc.perform(
                post("/api/geo/geocode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(new ArrayList<>())));

        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    public void geocode_whenApiException_mustCorrectResponseJsonWithErrorCode() throws Exception {
        when(geocodeService.geocode(any())).thenThrow(new InvalidRequestException(""));

        ResultActions resultActions = mvc.perform(
                post("/api/geo/geocode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(new ArrayList<>())));

        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorCode", is(GOOGLE_API_ERROR.name())))
                .andExpect(jsonPath("$.description", startsWith(SOMETHING_WRONG_WITH_GOOGLE)));
    }
}