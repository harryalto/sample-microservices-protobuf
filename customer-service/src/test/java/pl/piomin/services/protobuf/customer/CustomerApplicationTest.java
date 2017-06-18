package pl.piomin.services.protobuf.customer;

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piomin.services.protobuf.customer.model.CustomerProto.Customer;
import pl.piomin.services.protobuf.customer.model.CustomerProto.Customers;
import pl.piomin.services.protobuf.customer.model.CustomerProto.Customer.CustomerType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CustomerApplicationTest {

	protected Logger logger = Logger.getLogger(CustomerApplicationTest.class.getName());
	
	@Autowired
	TestRestTemplate template;
	
	@Test
	public void testFindById() {
		Customer c = this.template.getForObject("/customers/{id}", Customer.class, 1);
		logger.info("Customer[\n" + c + "]");
	}
	
	@Test
	public void testFindByPesel() {
		Customer c = this.template.getForObject("/customers/pesel/{pesel}", Customer.class, "12346");
		logger.info("Customer[\n" + c + "]");
	}
	
	@Test
	public void testFindAll() {
		Customers c = this.template.getForObject("/customers", Customers.class);
		logger.info("Customers[\n" + c + "]");
	}
	
	@Test
	public void insertById() {
		//Customer c = this.template.postForObject("/customers", Customer.class, 1);
		Customer request = Customer.newBuilder().setId(10).setPesel("12345").setName("Harihar Nath")
		.setType(CustomerType.INDIVIDUAL).build();
		
		Customer c = this.template.postForObject("/customers", request, Customer.class);
		logger.info("Customer[\n" + c + "]");
	}

	@TestConfiguration
	static class Config {

		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().additionalMessageConverters(new ProtobufHttpMessageConverter());
		}

	}
	
}
