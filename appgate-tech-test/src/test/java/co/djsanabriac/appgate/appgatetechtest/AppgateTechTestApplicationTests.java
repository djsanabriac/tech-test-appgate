package co.djsanabriac.appgate.appgatetechtest;

import co.djsanabriac.appgate.appgatetechtest.controller.OperationController;
import co.djsanabriac.appgate.appgatetechtest.model.dto.GeneralResponse;
import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import co.djsanabriac.appgate.appgatetechtest.service.StepService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest({OperationController.class,StepService.class})
class AppgateTechTestApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private OperationController controller;

	@Autowired
	private StepService stepService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
		Assertions.assertNotNull(stepService);
	}

	@Test
	void getSession() throws Exception {

		mvc.perform(
				MockMvcRequestBuilders
						.get("/operation/session")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
		;
	}

	@Test
	void addStep() throws Exception {

		MvcResult result = mvc.perform(
						MockMvcRequestBuilders
								.get("/operation/session")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
				.andReturn();

		String sid = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"20\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

	}

	@Test
	void addStepNoSession() throws Exception {

		String sid = "";

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", "")
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"20\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.FALSE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("INVALID_SID"))
				.andReturn();

	}

	@Test
	void sumSteps() throws Exception {

		MvcResult result = mvc.perform(
						MockMvcRequestBuilders
								.get("/operation/session")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
				.andReturn();

		String sid = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"20\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"40\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operation\",\r\n    \"value\": \"sum\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].type").value("result"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].value").value("60.0"))
				.andReturn();

	}

	@Test
	void sumResultRestSteps() throws Exception {

		MvcResult result = mvc.perform(
						MockMvcRequestBuilders
								.get("/operation/session")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
				.andReturn();

		String sid = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"20\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"40\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operation\",\r\n    \"value\": \"sum\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].type").value("result"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].value").value("60.0"))
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"15\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operation\",\r\n    \"value\": \"rest\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[6].type").value("result"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[6].value").value("45.0"))
				.andReturn();

	}

	@Test
	void zeroDivisionSteps() throws Exception {

		MvcResult result = mvc.perform(
						MockMvcRequestBuilders
								.get("/operation/session")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
				.andReturn();

		String sid = JsonPath.read(result.getResponse().getContentAsString(), "$.data");

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"20\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operand\",\r\n    \"value\": \"0\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andReturn();

		mvc.perform(
						MockMvcRequestBuilders
								.post("/operation/step")
								.header("sid", sid)
								.content("{\r\n    \"type\": \"operation\",\r\n    \"value\": \"division\"\r\n}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].type").value("exception"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[3].value").value("Operation not supported"))
				.andReturn();

	}

}
