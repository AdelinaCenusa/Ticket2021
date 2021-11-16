package uk.ac.solent.lunderground.controllerweb.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.solent.lunderground.controllerweb.WebObjectFactory;
import uk.ac.solent.lunderground.model.dto.TicketMachineConfig;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.service.LundergroundFacade;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(
        locations =
                {
                        "classpath:spring/controller-application-context.xml",
                        "classpath:spring/Lunderground-servlet.xml"
                }
)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RestEndPointControllerTest
{
    private static final String SOME_UUID = "some uuid";
    /**
     * Web Application Context used to allow the test cases to run.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * MockMvc instance used to request endpoints and verify that the correct outcomes occur.
     */
    private MockMvc mockMvc;

    /**
     * Code to be executed before each test case to ensure that each one starts froms the same configuration.
     */
    @Before
    public void setup()
    {
        mockMvc = webAppContextSetup(this.context).build();
    }

    @Test
    public void getTicketMachineConfigReturnsStatusOk() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            when(mockFacade.getTicketMachineConfig(anyString())).thenReturn(mock(TicketMachineConfig.class));

            mockMvc.perform(get("/rest/v1/ticketMachineConfig/{uuid}", "1234"))
                   .andExpect(status().isOk());
        }
    }
}
