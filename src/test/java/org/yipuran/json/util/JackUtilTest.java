package org.yipuran.json.util;

import org.junit.Assert;
import org.junit.Test;
import org.yipuran.json.test.data.UserInfo;
import org.yipuran.json.test.data.Yard;
import org.yipuran.json.test.tool.IResources;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class JackUtilTest {

	public static void main(String[] args) {

		String jsonstr = IResources.readResource("sample3.json");

		System.out.println(jsonstr);

		ObjectMapper mapper = new ObjectMapper();
		//mapper.setPropertyNamingStrategy(new UpperSnakeCaseStrategy());
		mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		try {
			Yard y = mapper.readValue(jsonstr, Yard.class);
			System.out.println(y);
			String out = mapper.writeValueAsString(y);
			System.out.println(out);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	@Test(expected=JsonMappingException.class)
	public void getObjectMapperTest1() throws JsonMappingException {
		String jsonstr = IResources.readResource("user.json");
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserInfo u = mapper.readValue(jsonstr, UserInfo.class);
			System.out.println(u);
			String out = mapper.writeValueAsString(u);
			System.out.println(out);
		} catch (JsonMappingException e) {
			throw e;
		} catch (JsonProcessingException e) {
			Assert.fail();
		}
	}
	@Test
	public void getObjectMapperTest2()  {
		String jsonstr = IResources.readResource("user.json");
		UserInfo userinfo = new UserInfo();
		userinfo.setFirstName("太郎");
		userinfo.setLastName("山田");
		userinfo.setUId("A001");
		try {
			ObjectMapper mapper = JackUtil.getObjectMapper();
			UserInfo u = mapper.readValue(jsonstr, UserInfo.class);
			Assert.assertEquals(userinfo.toString(), u.toString());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	@Test
	public void getObjectMapperWithrSnakeTest()  {
		String jsonstr = IResources.readResource("sample3.json");
		Yard yard = new Yard();
		yard.setAId("A01");
		yard.setBeta("Beta");
		try {
			ObjectMapper mapper = JackUtil.getObjectMapperWithrSnake();
			Yard y = mapper.readValue(jsonstr, Yard.class);
			Assert.assertEquals(yard.toString(), y.toString());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	@Test
	public void getObjectMapperWithUpperSnakeTest()  {
		String jsonstr = IResources.readResource("sample4.json");
		Yard yard = new Yard();
		yard.setAId("A01");
		yard.setBeta("Beta");
		try {
			ObjectMapper mapper = JackUtil.getObjectMapperWithUpperSnake();
			Yard y = mapper.readValue(jsonstr, Yard.class);
			Assert.assertEquals(yard.toString(), y.toString());
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
