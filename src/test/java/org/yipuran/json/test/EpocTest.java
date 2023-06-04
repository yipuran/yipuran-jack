package org.yipuran.json.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.yipuran.json.modules.EpochDatetimeDeserializer;
import org.yipuran.json.modules.EpochSqlTimestampDeserializer;
import org.yipuran.json.modules.SqlTimestampDeSerializer;
import org.yipuran.json.modules.SqlTimestampSerializer;
import org.yipuran.json.test.data.Foo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * EpocTest
 */
public class EpocTest{
	private LocalDateTime nowtime;
	private String jsonstr;
	private String secstr;
	private LocalDateTime nowtimesec;

	@Before
	public void setUp() throws Exception{
		nowtime = LocalDateTime.now();
		long epocmili = nowtime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		jsonstr = "{\"atime\":"+epocmili+",\"btime\":"+epocmili+"}";
		long epocsec = Instant.now().getEpochSecond();;
		secstr = "{\"atime\":"+epocsec+",\"btime\":"+epocsec+"}";
		nowtimesec = Instant.ofEpochSecond(epocsec).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	@Test
	public void test1() {
		JavaTimeModule jtm = new JavaTimeModule();
		jtm.addDeserializer(LocalDateTime.class, new EpochDatetimeDeserializer());
		jtm.addDeserializer(Timestamp.class, new EpochSqlTimestampDeserializer());
		jtm.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		jtm.addSerializer(Timestamp.class, new SqlTimestampSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

		ObjectMapper mapper = new ObjectMapper().registerModule(jtm);
		try{
			Foo foo = mapper.readValue(jsonstr, Foo.class);
			assertEquals(nowtime, foo.getAtime());
			assertEquals(nowtime, foo.getBtime().toLocalDateTime());

			String jsontxt = mapper.writeValueAsString(foo);
			String expstr = "{\"atime\":\""+nowtime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
			+"\",\"btime\":\""+nowtime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))+"\"}";
			assertEquals(expstr, jsontxt);

			JavaTimeModule jtm2 = new JavaTimeModule();
			jtm2.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
			jtm2.addDeserializer(Timestamp.class, new SqlTimestampDeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
			ObjectMapper mapper2 = new ObjectMapper().registerModule(jtm2);
			Foo foo2 = mapper2.readValue(jsontxt, Foo.class);
			assertEquals(nowtime, foo2.getAtime());
			assertEquals(nowtime, foo2.getBtime().toLocalDateTime());
		}catch (JsonMappingException e){
			e.printStackTrace();
		}catch (JsonProcessingException e){
			e.printStackTrace();
		}
	}
	@Test
	public void test2() {
		JavaTimeModule jtm = new JavaTimeModule();
		jtm.addDeserializer(LocalDateTime.class, new EpochDatetimeDeserializer());
		jtm.addDeserializer(Timestamp.class, new EpochSqlTimestampDeserializer());
		ObjectMapper mapper = new ObjectMapper().registerModule(jtm);
		try{
			Foo foo = mapper.readValue(secstr, Foo.class);
			assertEquals(nowtimesec, foo.getAtime());
			assertEquals(nowtimesec, foo.getBtime().toLocalDateTime());
		}catch (JsonMappingException e){
			e.printStackTrace();
		}catch (JsonProcessingException e){
			e.printStackTrace();
		}
	}
}
