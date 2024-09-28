package com.rushi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/test")
@EnableCaching
public class RedisApplication {

	private final Calculator calculator;

	public RedisApplication(Calculator calculator) {
		this.calculator = calculator;
	}

	@GetMapping("/cache/{number}")
	public int test(@PathVariable int number){
		return calculator.doubleIt(number);
	}

	@GetMapping("/cache/evict/{number}")
	public String evict(@PathVariable int number){
		calculator.evict(number);
		return "Evicted successfully";
	}

	record Student(int id , String name){}

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setValueSerializer(stringSerializer);
		template.setHashValueSerializer(stringSerializer);

		return template;
	}

	@Bean
	CommandLineRunner commandLineRunner(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper){
		return args -> {

		};
	}

	private static void incrementExample(RedisTemplate<String, Object> redisTemplate) {
		ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
		opsForValue.increment("cache_hit", 2);
	}

	private static void hashDataStructureExample(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) throws JsonProcessingException {
		HashOperations<String, Object, Object> opsForhash = redisTemplate.opsForHash();
		Student student = new Student(2, "abhi");
		String studentJsonString = objectMapper.writeValueAsString(student);
		Map<String, String> map = Map.of("reponse", studentJsonString);

		opsForhash.putAll("A125", map);
	}

	private static void simpleKeyValueExample(RedisTemplate<String, String> redisTemplate) {
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set("name", "abhi");
		String name = opsForValue.get("name");
		System.out.println("name = " + name);
	}

}
