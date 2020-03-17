package stfXCore;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StfXCoreTests {

	@Test
	void exampleWebTest(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(get("/employees/1"))
				.andExpect(status().isOk())
				.andExpect(content().string(
					"{\"id\":1,\"name\":\"Bilbo Baggins\",\"role\":\"burglar\",\"_links\":{\"self\":{\"href\":\"http://localhost/employees/1\"},\"employees\":{\"href\":\"http://localhost/employees\"}}}"));
	}

	@Test
	void exampleServiceTest() {
		assertThat(3).isBetween(0, 5);
	}

//	@Autowired
//	private TestEntityManager entityManager;
//
//
//	@Autowired
//	private EmployeeRepository employeeRepository;
//
//	@Test
//	public void exampleWebTest2() {
//		// given
//		Employee alex = new Employee("alex", "tester");
//		entityManager.persist(alex);
//		entityManager.flush();
//
//		// when
//		Optional<Employee> found = employeeRepository.findById(3L);
//
//		// then
//		assertThat(found).isNotNull();
//	}
}