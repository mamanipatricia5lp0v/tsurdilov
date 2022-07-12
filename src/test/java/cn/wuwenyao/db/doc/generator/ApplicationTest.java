package cn.wuwenyao.db.doc.generator;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/***
 * 应用测试
 * 
 * @author wwy
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	
	@Autowired
	private Optional<JdbcTemplate> jdbcTemplate;
	
	@Test
	public void contextLoads() {
		Assert.assertTrue(jdbcTemplate.isPresent());
	}
	
}
